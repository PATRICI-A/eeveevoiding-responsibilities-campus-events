package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.mapper.EventMapper;
import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.ports.in.GetEventsPort;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Caso de uso para obtener un listado de eventos activos.
 * <p>
 * Permite filtrar por categoría y fecha. Solo retorna eventos en estado ACTIVO.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class GetEventsUseCase implements GetEventsPort {

    private final EventRepositoryPort eventRepository;
    private final EventMapper eventMapper;

    /**
     * Ejecuta la búsqueda de eventos con filtros opcionales.
     *
     * @param category categoría del evento (puede ser null para todas)
     * @param date     fecha del evento (puede ser null para todas)
     * @return lista de eventos activos que cumplen los filtros
     */
    @Override
    public List<EventFeedResponse> execute(EventCategory category, LocalDate date) {
        return eventRepository.findActiveEvents(category, date)
                .stream()
                .filter(event -> event.getStatus().equals(EventStatus.ACTIVE))
                .map(eventMapper::toFeedDTO)
                .toList();
    }
}