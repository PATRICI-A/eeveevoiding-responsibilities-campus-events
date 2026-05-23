package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.request.EventRequest;
import edu.eci.patricia.application.dto.response.EventResponse;
import edu.eci.patricia.application.mapper.EventMapper;
import edu.eci.patricia.domain.exceptions.EventDomainException;
import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.ports.in.CreateEventPort;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.domain.valueobjects.EventId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Caso de uso para crear un nuevo evento.
 * <p>
 * Valida que el nombre no esté repetido, que los eventos con capacidad tengan
 * un máximo válido (mínimo 2), y asigna estado ACTIVO, organizador y QR.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CreateEventUseCase implements CreateEventPort {

    private final EventRepositoryPort eventRepository;
    private final EventMapper eventMapper;

    /**
     * Ejecuta la creación de un evento.
     *
     * @param request     datos del evento a crear
     * @param organizerId identificador del organizador propietario
     * @return el evento creado con sus datos completos (incluyendo ID y QR)
     * @throws EventDomainException si el nombre ya existe, falta capacidad máxima,
     *                              o la capacidad es menor a 2
     */
    @Override
    public EventResponse execute(EventRequest request, UUID organizerId) {

        if (request.getType() == EventType.WITH_CAPACITY && request.getMaxCapacity() == null) {
            throw new EventDomainException("Max capacity is required for WITH_CAPACITY events");
        }

        if (request.getType() == EventType.WITH_CAPACITY && request.getMaxCapacity() < 2) {
            throw new EventDomainException("Minimum of 2 spots for WITH_CAPACITY events");
        }

        if (eventRepository.existsByName(request.getName())) {
            throw new EventDomainException("An event with the name '" + request.getName() + "' already exists");
        }

        Event event = eventMapper.toDomain(request);
        event.setId(EventId.generate());
        event.setOrganizerId(organizerId);
        event.setStatus(EventStatus.ACTIVE);
        event.setAvailableCapacity(request.getType() == EventType.WITH_CAPACITY
                ? request.getMaxCapacity() : null);

        Event saved = eventRepository.save(event);

        saved.setQrCode(generateQrCode(saved.getId().getValue()));
        saved = eventRepository.save(saved);

        return eventMapper.toDTO(saved);
    }

    /**
     * Genera un código QR simple basado en el ID del evento.
     *
     * @param eventId identificador del evento
     * @return código QR como string
     */
    private String generateQrCode(UUID eventId) {
        return "QR-" + eventId;
    }
}