package edu.eci.patricia.DOWS_patricia.application.service;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.CategoriaEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EstadoEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.Evento;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;

    public List<Evento> obtenerFeed(CategoriaEvento categoria) {
        if (categoria != null) {
            return eventoRepository.findByEstadoAndCategoriaOrderByFechaHoraAsc(EstadoEvento.ACTIVO, categoria);
        }
        return eventoRepository.findByEstadoOrderByFechaHoraAsc(EstadoEvento.ACTIVO);
    }
}
