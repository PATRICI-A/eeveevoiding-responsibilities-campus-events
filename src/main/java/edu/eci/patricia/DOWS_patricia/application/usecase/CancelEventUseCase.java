package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotActiveException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.UnauthorizedOrganizerException;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CancelEventPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancelEventUseCase implements CancelEventPort {

    private final EventRepositoryPort eventRepository;

    @Override
    public void execute(UUID eventId, UUID organizerId) {

        Event event = eventRepository.findById(new EventId(eventId))
                .orElseThrow(() -> new EventNotFoundException(eventId.toString()));

        if (event.getStatus() != EventStatus.ACTIVE) {
            throw new EventNotActiveException(eventId.toString());
        }

        if (!event.getOrganizerId().equals(organizerId)) {
            throw new UnauthorizedOrganizerException();
        }

        event.setStatus(EventStatus.CANCELLED);
        event.setUpdatedAt(LocalDateTime.now());

        eventRepository.save(event);
    }
}