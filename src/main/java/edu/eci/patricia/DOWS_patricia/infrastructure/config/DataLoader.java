package edu.eci.patricia.DOWS_patricia.infrastructure.config;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.CategoriaEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EstadoEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.Evento;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.TipoEvento;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final EventoRepository eventoRepository;

    @Override
    public void run(String... args) {
        if (eventoRepository.count() > 0) return;

        eventoRepository.saveAll(List.of(
            Evento.builder()
                .nombre("Charla: Inteligencia Artificial en la Industria")
                .descripcion("Conferencia sobre aplicaciones reales de IA en empresas colombianas.")
                .fechaHora(LocalDateTime.now().plusDays(3))
                .lugar("Auditorio Principal")
                .categoria(CategoriaEvento.ACADEMICO)
                .tipo(TipoEvento.ABIERTO)
                .organizadorId("org-001")
                .estado(EstadoEvento.ACTIVO)
                .creadoEn(LocalDateTime.now())
                .build(),
            Evento.builder()
                .nombre("Feria de Empresas DOSW 2025")
                .descripcion("Empresas del sector tecnológico visitan la Escuela para reclutar estudiantes.")
                .fechaHora(LocalDateTime.now().plusDays(7))
                .lugar("Patio Central")
                .categoria(CategoriaEvento.ACADEMICO)
                .tipo(TipoEvento.ABIERTO)
                .organizadorId("org-001")
                .estado(EstadoEvento.ACTIVO)
                .creadoEn(LocalDateTime.now())
                .build(),
            Evento.builder()
                .nombre("Taller de Liderazgo Estudiantil")
                .descripcion("Taller práctico para desarrollar habilidades de liderazgo y trabajo en equipo.")
                .fechaHora(LocalDateTime.now().plusDays(5))
                .lugar("Sala de Reuniones B2")
                .categoria(CategoriaEvento.ACADEMICO)
                .tipo(TipoEvento.CON_CUPO)
                .cupoMaximo(30)
                .cupoDisponible(30)
                .organizadorId("org-002")
                .estado(EstadoEvento.ACTIVO)
                .creadoEn(LocalDateTime.now())
                .build(),
            Evento.builder()
                .nombre("Concierto Coro Universitario")
                .descripcion("Presentación del coro de la Escuela con repertorio clásico y popular.")
                .fechaHora(LocalDateTime.now().plusDays(10))
                .lugar("Auditorio Principal")
                .categoria(CategoriaEvento.CULTURAL)
                .tipo(TipoEvento.ABIERTO)
                .organizadorId("org-003")
                .estado(EstadoEvento.ACTIVO)
                .creadoEn(LocalDateTime.now())
                .build(),
            Evento.builder()
                .nombre("Noche de Talentos ECI")
                .descripcion("Muestra artística donde estudiantes presentan sus talentos musicales, teatrales y de danza.")
                .fechaHora(LocalDateTime.now().plusDays(14))
                .lugar("Cafetería Central")
                .categoria(CategoriaEvento.CULTURAL)
                .tipo(TipoEvento.CON_CUPO)
                .cupoMaximo(100)
                .cupoDisponible(100)
                .organizadorId("org-003")
                .estado(EstadoEvento.ACTIVO)
                .creadoEn(LocalDateTime.now())
                .build(),
            Evento.builder()
                .nombre("Torneo Interno de Fútbol")
                .descripcion("Fase clasificatoria del torneo interno de fútbol por facultades.")
                .fechaHora(LocalDateTime.now().plusDays(2))
                .lugar("Cancha Principal")
                .categoria(CategoriaEvento.DEPORTIVO)
                .tipo(TipoEvento.ABIERTO)
                .organizadorId("org-004")
                .estado(EstadoEvento.ACTIVO)
                .creadoEn(LocalDateTime.now())
                .build(),
            Evento.builder()
                .nombre("Clase Abierta de Yoga")
                .descripcion("Sesión de yoga para todos los niveles, perfecta para liberar el estrés académico.")
                .fechaHora(LocalDateTime.now().plusDays(1))
                .lugar("Zona Verde")
                .categoria(CategoriaEvento.DEPORTIVO)
                .tipo(TipoEvento.CON_CUPO)
                .cupoMaximo(25)
                .cupoDisponible(25)
                .organizadorId("org-004")
                .estado(EstadoEvento.ACTIVO)
                .creadoEn(LocalDateTime.now())
                .build(),
            Evento.builder()
                .nombre("Maratón de Programación")
                .descripcion("Hackathon de 8 horas con retos de algoritmos y desarrollo de software.")
                .fechaHora(LocalDateTime.now().plusDays(6))
                .lugar("Laboratorio de Sistemas")
                .categoria(CategoriaEvento.ACADEMICO)
                .tipo(TipoEvento.CON_CUPO)
                .cupoMaximo(40)
                .cupoDisponible(40)
                .organizadorId("org-001")
                .estado(EstadoEvento.ACTIVO)
                .creadoEn(LocalDateTime.now())
                .build(),
            Evento.builder()
                .nombre("Taller de Mindfulness y Bienestar")
                .descripcion("Sesión introductoria de mindfulness para mejorar la concentración y reducir la ansiedad.")
                .fechaHora(LocalDateTime.now().plusDays(4))
                .lugar("Sala de Bienestar")
                .categoria(CategoriaEvento.BIENESTAR)
                .tipo(TipoEvento.CON_CUPO)
                .cupoMaximo(20)
                .cupoDisponible(20)
                .organizadorId("org-005")
                .estado(EstadoEvento.ACTIVO)
                .creadoEn(LocalDateTime.now())
                .build(),
            Evento.builder()
                .nombre("Exposición de Arte Estudiantil")
                .descripcion("Muestra de obras de arte creadas por estudiantes durante el semestre.")
                .fechaHora(LocalDateTime.now().plusDays(12))
                .lugar("Hall de Entrada")
                .categoria(CategoriaEvento.CULTURAL)
                .tipo(TipoEvento.ABIERTO)
                .organizadorId("org-003")
                .estado(EstadoEvento.ACTIVO)
                .creadoEn(LocalDateTime.now())
                .build()
        ));
    }
}
