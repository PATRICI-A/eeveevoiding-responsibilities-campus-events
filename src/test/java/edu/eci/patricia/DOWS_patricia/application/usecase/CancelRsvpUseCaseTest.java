package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.RsvpAlreadyExistsException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.RsvpNotFoundException;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRsvpRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
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
class CancelRsvpUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventRsvpRepositoryPort rsvpRepository;

    @InjectMocks
    private CancelRsvpUseCase cancelRsvpUseCase;

    private UUID eventUUID;
    private UUID studentId;
    private Event openEvent;
    private Event capacityEvent;
    private EventRsvp confirmedRsvp;
    private EventRsvp cancelledRsvp;

    @BeforeEach
    void setUp() {
        eventUUID = UUID.randomUUID();
        studentId = UUID.randomUUID();

        openEvent = Event.builder()
                .id(new EventId(eventUUID))
                .name("Open Event")
                .category(EventCategory.CULTURAL)
                .type(EventType.OPEN)
                .status(EventStatus.ACTIVE)
                .organizerId(UUID.randomUUID())
                .dateTime(LocalDateTime.now().plusDays(5))
                .build();

        capacityEvent = Event.builder()
                .id(new EventId(eventUUID))
                .name("Capacity Event")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(10)
                .availableCapacity(4)
                .status(EventStatus.ACTIVE)
                .organizerId(UUID.randomUUID())
                .dateTime(LocalDateTime.now().plusDays(5))
                .build();

        confirmedRsvp = EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(new EventId(eventUUID))
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .confirmedAt(LocalDateTime.now().minusDays(1))
                .build();

        cancelledRsvp = EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(new EventId(eventUUID))
                .studentId(studentId)
                .status(RsvpStatus.CANCELLED)
                .confirmedAt(LocalDateTime.now().minusDays(2))
                .cancelledAt(LocalDateTime.now().minusDays(1))
                .build();
    }

    @Test
    void shouldCancelRsvpForOpenEventSuccessfully() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(openEvent));
        when(rsvpRepository.findByEventIdAndStudentId(any(EventId.class), eq(studentId)))
                .thenReturn(Optional.of(confirmedRsvp));

        cancelRsvpUseCase.execute(eventUUID, studentId);

        assertEquals(RsvpStatus.CANCELLED, confirmedRsvp.getStatus());
        assertNotNull(confirmedRsvp.getCancelledAt());
        verify(rsvpRepository).save(confirmedRsvp);
        verify(eventRepository, never()).save(any());
    }

    @Test
    void shouldCancelRsvpForWithCapacityEventAndRestoreCapacity() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(capacityEvent));
        when(rsvpRepository.findByEventIdAndStudentId(any(EventId.class), eq(studentId)))
                .thenReturn(Optional.of(confirmedRsvp));

        cancelRsvpUseCase.execute(eventUUID, studentId);

        assertEquals(RsvpStatus.CANCELLED, confirmedRsvp.getStatus());
        assertEquals(5, capacityEvent.getAvailableCapacity());
        verify(rsvpRepository).save(confirmedRsvp);
        verify(eventRepository).save(capacityEvent);
    }

    @Test
    void shouldThrowEventNotFoundExceptionWhenEventDoesNotExist() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> cancelRsvpUseCase.execute(eventUUID, studentId));

        verify(rsvpRepository, never()).save(any());
    }

    @Test
    void shouldThrowRsvpNotFoundExceptionWhenRsvpDoesNotExist() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(openEvent));
        when(rsvpRepository.findByEventIdAndStudentId(any(EventId.class), eq(studentId)))
                .thenReturn(Optional.empty());

        assertThrows(RsvpNotFoundException.class,
                () -> cancelRsvpUseCase.execute(eventUUID, studentId));

        verify(rsvpRepository, never()).save(any());
    }

    @Test
    void shouldThrowRsvpAlreadyExistsExceptionWhenRsvpAlreadyCancelled() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(openEvent));
        when(rsvpRepository.findByEventIdAndStudentId(any(EventId.class), eq(studentId)))
                .thenReturn(Optional.of(cancelledRsvp));

        assertThrows(RsvpAlreadyExistsException.class,
                () -> cancelRsvpUseCase.execute(eventUUID, studentId));

        verify(rsvpRepository, never()).save(any());
    }

    @Test
    void shouldSetCancelledAtTimestampOnCancellation() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(openEvent));
        when(rsvpRepository.findByEventIdAndStudentId(any(EventId.class), eq(studentId)))
                .thenReturn(Optional.of(confirmedRsvp));

        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        cancelRsvpUseCase.execute(eventUUID, studentId);

        assertTrue(confirmedRsvp.getCancelledAt().isAfter(before));
    }

    @Test
    void shouldNotRestoreCapacityForOpenEventOnCancellation() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(openEvent));
        when(rsvpRepository.findByEventIdAndStudentId(any(EventId.class), eq(studentId)))
                .thenReturn(Optional.of(confirmedRsvp));

        cancelRsvpUseCase.execute(eventUUID, studentId);

        verify(eventRepository, never()).save(any());
    }
}
