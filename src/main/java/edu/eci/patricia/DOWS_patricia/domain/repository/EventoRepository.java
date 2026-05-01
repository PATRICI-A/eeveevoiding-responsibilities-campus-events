package edu.eci.patricia.DOWS_patricia.domain.repository;

import edu.eci.patricia.DOWS_patricia.domain.model.CategoriaEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.EstadoEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.Evento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventoRepository extends MongoRepository<Evento, String> {

    List<Evento> findByEstadoOrderByFechaHoraAsc(EstadoEvento estado);

    List<Evento> findByEstadoAndCategoriaOrderByFechaHoraAsc(EstadoEvento estado, CategoriaEvento categoria);
}
