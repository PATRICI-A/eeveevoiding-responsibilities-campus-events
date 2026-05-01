package edu.eci.patricia.DOWS_patricia.infrastructure.controller;

import edu.eci.patricia.DOWS_patricia.application.service.EventoService;
import edu.eci.patricia.DOWS_patricia.domain.model.CategoriaEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.EstadoEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.Evento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventoController.class)
@AutoConfigureMockMvc(addFilters = false)
class EventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventoService eventoService;

    @Test
    @DisplayName("Como estudiante, debo poder acceder al feed de eventos sin error")
    void consultarFeed_sinFiltros_retorna200() throws Exception {
        when(eventoService.obtenerFeed(any(), any())).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/eventos"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Como estudiante, debo poder filtrar el feed por categoria")
    void consultarFeed_filtroPorCategoria_retorna200() throws Exception {
        Evento evento = Evento.builder()
                .id("1")
                .nombre("Concierto")
                .categoria(CategoriaEvento.CULTURAL)
                .fechaHora(LocalDateTime.now().plusDays(1))
                .estado(EstadoEvento.ACTIVO)
                .build();
        when(eventoService.obtenerFeed(any(), any())).thenReturn(List.of(evento));

        mockMvc.perform(get("/api/v1/eventos").param("categoria", "CULTURAL"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Como estudiante, si envio una categoria invalida debo recibir error 400")
    void consultarFeed_categoriaInvalida_retorna400() throws Exception {
        mockMvc.perform(get("/api/v1/eventos").param("categoria", "INVALIDA"))
                .andExpect(status().isBadRequest());
    }
}
