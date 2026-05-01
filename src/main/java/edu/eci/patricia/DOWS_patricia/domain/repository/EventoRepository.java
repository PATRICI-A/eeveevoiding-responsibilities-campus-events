package edu.eci.patricia.DOWS_patricia.domain.repository;

import edu.eci.patricia.DOWS_patricia.domain.model.CategoriaEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.EstadoEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.Evento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventoRepository extends MongoRepository<Evento, String> {

    List<Evento> findByEstadoOrderByFechaHoraAsc(EstadoEvento estado);

    List<Evento> findByEstadoAndCategoria(EstadoEvento estado, CategoriaEvento categoria);

    List<Evento> findByEstadoAndFechaHora(EstadoEvento estado, LocalDateTime fecha);
}
