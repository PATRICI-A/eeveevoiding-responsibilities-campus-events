package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.request.EventUpdateRequest;
import edu.eci.patricia.application.dto.response.EventResponse;
import edu.eci.patricia.application.mapper.EventMapper;
import edu.eci.patricia.domain.exceptions.EventDomainException;
import edu.eci.patricia.domain.exceptions.EventNotActiveException;
import edu.eci.patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.domain.exceptions.UnauthorizedOrganizerException;
import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.ports.in.UpdateEventPort;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.domain.ports.out.EventRsvpRepositoryPort;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.infrastructure.messaging.EventChangePublisher;
import edu.eci.patricia.infrastructure.messaging.dto.EventChangeEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Caso de uso para actualizar un evento existente.
 * <p>
 * Solo permite modificar eventos ACTIVOS y siempre que el organizador sea el propietario.
 * Al actualizar, notifica a todos los estudiantes con reserva confirmada sobre el cambio.
 * Valida que los eventos con capacidad tengan un máximo válido (mínimo 2).
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UpdateEventUseCase implements UpdateEventPort {

    private final EventRepositoryPort eventRepository;
    private final EventMapper eventMapper;
    private final EventRsvpRepositoryPort rsvpRepository;
    private final EventChangePublisher eventChangePublisher;

    /**
     * Ejecuta la actualización de un evento.
     *
     * @param eventId     identificador del evento a modificar
     * @param request     nuevos datos del evento
     * @param organizerId identificador del organizador que solicita el cambio
     * @return el evento actualizado
     * @throws EventNotFoundException       si el evento no existe
     * @throws EventNotActiveException      si el evento no está ACTIVO
     * @throws UnauthorizedOrganizerException si el organizador no es el propietario
     * @throws EventDomainException         si los datos de capacidad son inválidos
     */
    @Override
    public EventResponse execute(UUID eventId, EventUpdateRequest request, UUID organizerId) {

        Event event = eventRepository.findById(new EventId(eventId))
                .orElseThrow(() -> new EventNotFoundException(eventId.toString()));

        if (event.getStatus() != EventStatus.ACTIVE) {
            throw new EventNotActiveException("Can´t modify no ACTIVE event");
        }

        if (!event.getOrganizerId().equals(organizerId)) {
            throw new UnauthorizedOrganizerException();
        }

        if (request.getType() == EventType.WITH_CAPACITY && request.getMaxCapacity() == null) {
            throw new EventDomainException("Max capacity is required for WITH_CAPACITY events");
        }

        if (request.getType() == EventType.WITH_CAPACITY && request.getMaxCapacity() < 2) {
            throw new EventDomainException("Minimum of 2 spots for WITH_CAPACITY events");
        }

        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setDateTime(request.getDateTime());
        event.setStartTime(eventMapper.stringToLocalTime(request.getStartTime()));
        event.setDurationMinutes(request.getDuration());
        event.setLocation(request.getLocation());
        event.setCategory(request.getCategory());
        event.setType(request.getType());

        if (request.getType() == EventType.OPEN) {
            event.setMaxCapacity(null);
        } else {
            event.setMaxCapacity(request.getMaxCapacity());
        }

        List<EventRsvp> rsvps = rsvpRepository.findConfirmedByEventId(new EventId(eventId));
        for (EventRsvp rsvp : rsvps) {
            eventChangePublisher.publish(EventChangeEventDto.builder()
                    .targetUserId(rsvp.getStudentId())
                    .eventId(eventId)
                    .eventName(event.getName())
                    .changeDescription("modificado")
                    .build());
        }

        return eventMapper.toDTO(eventRepository.save(event));
    }
}