package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.dto.response.EventResponse;
import edu.eci.patricia.application.mapper.EventMapper;
import edu.eci.patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.ports.in.GetEventByIdPort;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.domain.valueobjects.EventId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Caso de uso para obtener un evento por su identificador.
 * <p>
 * Solo retorna eventos en estado ACTIVO. Si el evento existe pero no está activo,
 * se considera como no encontrado.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class GetEventByIdUseCase implements GetEventByIdPort {

    private final EventRepositoryPort eventRepository;
    private final EventMapper eventMapper;

    /**
     * Ejecuta la búsqueda de un evento por ID.
     *
     * @param eventId identificador del evento
     * @return datos del evento para el feed (público)
     * @throws EventNotFoundException si el evento no existe o no está ACTIVO
     */
    @Override
    public EventFeedResponse execute(UUID eventId) {
        return eventRepository.findById(new EventId(eventId))
                .filter(event -> event.getStatus().equals(EventStatus.ACTIVE))
                .map(eventMapper::toFeedDTO)
                .orElseThrow(() -> new EventNotFoundException(eventId.toString()));
    }
}