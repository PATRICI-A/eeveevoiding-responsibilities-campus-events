package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.domain.exceptions.*;
import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.domain.ports.in.CreateRsvpPort;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.domain.ports.out.EventRsvpRepositoryPort;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.domain.valueobjects.RsvpId;
import edu.eci.patricia.infrastructure.notification.NotificationServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Caso de uso para crear o reactivar una reserva (RSVP) a un evento.
 * <p>
 * Si el estudiante no tenía reserva, se crea una nueva (CONFIRMED).
 * Si tenía una reserva cancelada, se reactiva a CONFIRMED.
 * Valida que el evento esté ACTIVO y que haya capacidad disponible.
 * Al confirmar, registra un recordatorio en el servicio de notificaciones.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CreateRsvpUseCase implements CreateRsvpPort {

    private final EventRepositoryPort eventRepository;
    private final EventRsvpRepositoryPort rsvpRepository;
    private final EventRsvpMapper rsvpMapper;
    private final NotificationServiceClient notificationServiceClient;

    /**
     * Ejecuta la creación o reactivación de una reserva.
     *
     * @param eventId   identificador del evento
     * @param studentId identificador del estudiante
     * @return la reserva confirmada
     * @throws EventNotFoundException      si el evento no existe
     * @throws EventNotActiveException     si el evento no está ACTIVO
     * @throws EventCapacityFullException  si no hay cupos disponibles
     * @throws RsvpAlreadyExistsException  si ya existe una reserva confirmada
     */
    @Override
    public EventResponseRsvp execute(UUID eventId, UUID studentId) {

        EventId evId = new EventId(eventId);

        Event event = eventRepository.findById(evId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        if (event.getStatus() != EventStatus.ACTIVE) {
            throw new EventNotActiveException("Can´t modify no ACTIVE event");
        }

        if (event.getAvailableCapacity() == 0) {
            throw new EventCapacityFullException("Event capacity is FULL");
        }

        if (!rsvpRepository.existsByEventIdAndStudentId(evId, studentId)) {
            EventRsvp rsvp = EventRsvp.builder()
                    .id(new RsvpId(UUID.randomUUID()))
                    .eventId(evId)
                    .studentId(studentId)
                    .status(RsvpStatus.CONFIRMED)
                    .build();

            if (event.getType() == EventType.WITH_CAPACITY) {
                event.setAvailableCapacity(event.getAvailableCapacity() - 1);
                eventRepository.save(event);
            }
            rsvpRepository.save(rsvp);

            LocalDateTime eventDate = LocalDateTime.of(event.getDateTime(), event.getStartTime());
            notificationServiceClient.registerEventReminder(
                    studentId,
                    eventId,
                    eventDate
            );

            return rsvpMapper.toDTO(rsvpRepository.save(rsvp));
        }

        EventRsvp existingRsvp = rsvpRepository.findByEventIdAndStudentId(eventId, studentId)
                .orElseThrow(() -> new RsvpNotFoundException("RSVP not found for this event and student"));

        if (existingRsvp.getStatus() == RsvpStatus.CONFIRMED) {
            throw new RsvpAlreadyExistsException("RSVP is already confirmed for event: " + eventId);
        } else if (existingRsvp.getStatus() == RsvpStatus.CANCELLED) {
            existingRsvp.setStatus(RsvpStatus.CONFIRMED);
        }

        if (event.getType() == EventType.WITH_CAPACITY) {
            event.setAvailableCapacity(event.getAvailableCapacity() - 1);
            eventRepository.save(event);
        }

        return rsvpMapper.toDTO(rsvpRepository.save(existingRsvp));
    }
}