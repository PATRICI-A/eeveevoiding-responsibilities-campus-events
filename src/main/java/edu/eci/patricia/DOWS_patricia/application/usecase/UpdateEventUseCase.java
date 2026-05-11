package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventUpdateRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventMapper;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotActiveException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.UnauthorizedOrganizerException;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.UpdateEventPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
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

        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setDateTime(request.getDateTime());
        event.setDurationMinutes(request.getDurationMinutes());
        event.setLocation(request.getLocation());
        event.setCategory(request.getCategory());
        event.setUpdatedAt(LocalDateTime.now());

        return eventMapper.toDTO(eventRepository.save(event));
    }
}