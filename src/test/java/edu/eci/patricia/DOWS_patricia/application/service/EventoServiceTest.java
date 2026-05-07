package edu.eci.patricia.DOWS_patricia.application.service;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.CategoriaEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EstadoEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.Evento;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private EventoService eventoService;

    @Test
    @DisplayName("Como estudiante, debo poder ver todos los eventos activos del campus")
    void obtenerFeed_sinFiltros_retornaEventosActivos() {
        Evento e1 = evento("1", "Charla IA", CategoriaEvento.ACADEMICO);
        Evento e2 = evento("2", "Concierto", CategoriaEvento.CULTURAL);
        when(eventoRepository.findByEstadoOrderByFechaHoraAsc(EstadoEvento.ACTIVO))
                .thenReturn(List.of(e1, e2));

        List<Evento> resultado = eventoService.obtenerFeed(null);

        assertThat(resultado).hasSize(2);
    }

    @Test
    @DisplayName("Como estudiante, debo poder filtrar eventos por categoria CULTURAL")
    void obtenerFeed_filtroPorCategoria_retornaSoloEsaCategoria() {
        Evento e1 = evento("2", "Concierto", CategoriaEvento.CULTURAL);
        when(eventoRepository.findByEstadoAndCategoriaOrderByFechaHoraAsc(EstadoEvento.ACTIVO, CategoriaEvento.CULTURAL))
                .thenReturn(List.of(e1));

        List<Evento> resultado = eventoService.obtenerFeed(CategoriaEvento.CULTURAL);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCategoria()).isEqualTo(CategoriaEvento.CULTURAL);
    }

    @Test
    @DisplayName("Como estudiante, si no hay eventos activos debo recibir una lista vacía")
    void obtenerFeed_sinEventos_retornaListaVacia() {
        when(eventoRepository.findByEstadoOrderByFechaHoraAsc(EstadoEvento.ACTIVO))
                .thenReturn(List.of());

        List<Evento> resultado = eventoService.obtenerFeed(null);

        assertThat(resultado).isEmpty();
    }

    private Evento evento(String id, String nombre, CategoriaEvento categoria) {
        return Evento.builder()
                .id(id)
                .nombre(nombre)
                .categoria(categoria)
                .fechaHora(LocalDateTime.now().plusDays(1))
                .estado(EstadoEvento.ACTIVO)
                .build();
    }
}
