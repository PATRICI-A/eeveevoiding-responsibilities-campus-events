package edu.eci.patricia.application.usecase;

import edu.eci.patricia.domain.exceptions.EventNotActiveException;
import edu.eci.patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.domain.exceptions.UnauthorizedOrganizerException;
import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.domain.valueobjects.EventId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelEventUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @InjectMocks
    private CancelEventUseCase cancelEventUseCase;

    private UUID eventId;
    private UUID organizerId;
    private Event activeEvent;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        organizerId = UUID.randomUUID();

        activeEvent = Event.builder()
                .id(new EventId(eventId))
                .name("Evento Test")
                .status(EventStatus.ACTIVE)
                .organizerId(organizerId)
                .type(EventType.OPEN)
                .category(EventCategory.ACADEMIC)
                .build();
    }


    @Test
    void execute_shouldThrowException_whenEventNotFound() {
        when(eventRepository.findById(new EventId(eventId))).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> cancelEventUseCase.execute(eventId, organizerId));

        verify(eventRepository, never()).save(any());
    }

    @Test
    void execute_shouldThrowException_whenEventIsNotActive() {
        activeEvent.setStatus(EventStatus.CANCELLED);
        when(eventRepository.findById(new EventId(eventId))).thenReturn(Optional.of(activeEvent));

        assertThrows(EventNotActiveException.class,
                () -> cancelEventUseCase.execute(eventId, organizerId));

        verify(eventRepository, never()).save(any());
    }

    @Test
    void execute_shouldThrowException_whenOrganizerIsNotOwner() {
        UUID otherOrganizer = UUID.randomUUID();
        when(eventRepository.findById(new EventId(eventId))).thenReturn(Optional.of(activeEvent));

        assertThrows(UnauthorizedOrganizerException.class,
                () -> cancelEventUseCase.execute(eventId, otherOrganizer));

        verify(eventRepository, never()).save(any());
    }
}
