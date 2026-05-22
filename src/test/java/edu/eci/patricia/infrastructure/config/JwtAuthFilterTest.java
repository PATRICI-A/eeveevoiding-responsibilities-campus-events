package edu.eci.patricia.infrastructure.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    private static final String SECRET = "mi-clave-super-secreta-para-tests-de-jwt-12345678";

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthFilter jwtAuthFilter;
    private SecretKey signingKey;

    @BeforeEach
    void setUp() {
        jwtAuthFilter = new JwtAuthFilter(SECRET);
        signingKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        SecurityContextHolder.clearContext();
    }

    private String buildToken(String subject, Object roles, long expirationMs) {
        var builder = Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(signingKey);

        if (roles instanceof List) {
            builder.claim("roles", roles);
        } else if (roles instanceof String) {
            builder.claim("role", roles);
        }

        return builder.compact();
    }

    @Test
    void doFilterInternal_sinHeaderAuthorization_debeContinuarSinAutenticar() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_headerSinPrefixBearer_debeContinuarSinAutenticar() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic sometoken");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_tokenValido_debeAutenticarUsuario() throws ServletException, IOException {
        String userId = UUID.randomUUID().toString();
        String token = buildToken(userId, List.of("USER"), 60_000);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(userId, auth.getPrincipal());
    }

    @Test
    void doFilterInternal_tokenConRolLista_debeAsignarAuthoritiesConPrefixROLE() throws ServletException, IOException {
        String userId = UUID.randomUUID().toString();
        String token = buildToken(userId, List.of("STUDENT"), 60_000);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertTrue(auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT")));
    }

    @Test
    void doFilterInternal_tokenConRolString_debeAsignarAuthority() throws ServletException, IOException {
        String userId = UUID.randomUUID().toString();
        String token = buildToken(userId, "ORGANIZER", 60_000);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertTrue(auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ORGANIZER")));
    }

    @Test
    void doFilterInternal_tokenExpirado_noDebeAutenticar() throws ServletException, IOException {
        String userId = UUID.randomUUID().toString();
        String token = buildToken(userId, List.of("USER"), -1_000);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_tokenMalformado_noDebeAutenticar() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer token.invalido.aqui");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_tokenFirmadoConOtraClave_noDebeAutenticar() throws ServletException, IOException {
        SecretKey otraKey = Keys.hmacShaKeyFor(
                "otra-clave-completamente-diferente-para-tests-1234".getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .subject("user-id")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(otraKey)
                .compact();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_tokenSinRoles_debeAutenticarConAuthoritiesVacias() throws ServletException, IOException {
        String userId = UUID.randomUUID().toString();
        String token = Jwts.builder()
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(signingKey)
                .compact();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(userId, auth.getPrincipal());
        assertTrue(auth.getAuthorities().isEmpty());
    }

    @Test
    void doFilterInternal_tokenConMultiplesRoles_debeAsignarTodasLasAuthorities() throws ServletException, IOException {
        String userId = UUID.randomUUID().toString();
        String token = buildToken(userId, List.of("USER", "ADMIN"), 60_000);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(2, auth.getAuthorities().size());
        assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void doFilterInternal_siempreInvokaFilterChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }
}
