package edu.eci.patricia.DOWS_patricia.application.service;

import edu.eci.patricia.DOWS_patricia.domain.model.CategoriaEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.EstadoEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.Evento;
import edu.eci.patricia.DOWS_patricia.domain.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;

    public List<Evento> obtenerFeed(CategoriaEvento categoria, LocalDate fecha) {
        if (categoria != null) {
            return eventoRepository.findByEstadoAndCategoria(EstadoEvento.ACTIVO, categoria);
        }
        if (fecha != null) {
            return eventoRepository.findByEstadoAndFechaHora(EstadoEvento.ACTIVO, fecha.atStartOfDay());
        }
        return eventoRepository.findByEstadoOrderByFechaHoraAsc(EstadoEvento.ACTIVO);
    }
}
