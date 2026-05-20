package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.request.EventUpdateRequest;
import edu.eci.patricia.application.dto.response.EventResponse;
import edu.eci.patricia.application.mapper.EventMapper;
import edu.eci.patricia.domain.exceptions.EventDomainException;
import edu.eci.patricia.domain.exceptions.EventNotActiveException;
import edu.eci.patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.domain.exceptions.UnauthorizedOrganizerException;
import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.ports.in.UpdateEventPort;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.domain.valueobjects.EventId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateEventUseCase implements UpdateEventPort {

    private final EventRepositoryPort eventRepository;
    private final EventMapper eventMapper;

    @Override
    public EventResponse execute(UUID eventId, EventUpdateRequest request, UUID organizerId) {

        Event event = eventRepository.findById(new EventId(eventId))
                .orElseThrow(() -> new EventNotFoundException(eventId.toString()));

        if (event.getStatus() != EventStatus.ACTIVE) {
            throw new EventNotActiveException(eventId.toString());
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



        return eventMapper.toDTO(eventRepository.save(event));
    }
}