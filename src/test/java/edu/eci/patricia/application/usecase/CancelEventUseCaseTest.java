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

import java.time.LocalDateTime;
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

    private UUID eventUUID;
    private UUID organizerId;
    private Event activeEvent;

    @BeforeEach
    void setUp() {
        eventUUID = UUID.randomUUID();
        organizerId = UUID.randomUUID();

        activeEvent = Event.builder()
                .id(new EventId(eventUUID))
                .name("Tech Talk")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .status(EventStatus.ACTIVE)
                .organizerId(organizerId)
                .dateTime(LocalDateTime.now().plusDays(5))
                .build();
    }

    @Test
    void shouldCancelEventSuccessfully() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(activeEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(activeEvent);

        cancelEventUseCase.execute(eventUUID, organizerId);

        assertEquals(EventStatus.CANCELLED, activeEvent.getStatus());
        assertNotNull(activeEvent.getUpdatedAt());
        verify(eventRepository).save(activeEvent);
    }

    @Test
    void shouldThrowEventNotFoundExceptionWhenEventDoesNotExist() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> cancelEventUseCase.execute(eventUUID, organizerId));

        verify(eventRepository, never()).save(any());
    }

    @Test
    void shouldThrowEventNotActiveExceptionWhenEventAlreadyCancelled() {
        activeEvent.setStatus(EventStatus.CANCELLED);
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(activeEvent));

        assertThrows(EventNotActiveException.class,
                () -> cancelEventUseCase.execute(eventUUID, organizerId));

        verify(eventRepository, never()).save(any());
    }

    @Test
    void shouldThrowUnauthorizedOrganizerExceptionWhenOrganizerDoesNotMatch() {
        UUID differentOrganizer = UUID.randomUUID();
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(activeEvent));

        assertThrows(UnauthorizedOrganizerException.class,
                () -> cancelEventUseCase.execute(eventUUID, differentOrganizer));

        verify(eventRepository, never()).save(any());
    }

    @Test
    void shouldSetUpdatedAtWhenCancelling() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(activeEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(activeEvent);

        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        cancelEventUseCase.execute(eventUUID, organizerId);

        assertTrue(activeEvent.getUpdatedAt().isAfter(before));
    }
}
