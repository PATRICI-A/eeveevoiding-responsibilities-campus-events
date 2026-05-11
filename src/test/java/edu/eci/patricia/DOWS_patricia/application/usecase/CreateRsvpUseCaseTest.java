package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventCapacityFullException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotActiveException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.RsvpAlreadyExistsException;
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
class CreateRsvpUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventRsvpRepositoryPort rsvpRepository;

    @Mock
    private EventRsvpMapper rsvpMapper;

    @InjectMocks
    private CreateRsvpUseCase createRsvpUseCase;

    private UUID eventUUID;
    private UUID studentId;
    private Event activeOpenEvent;
    private Event activeCapacityEvent;
    private EventRsvp savedRsvp;
    private EventResponseRsvp rsvpResponse;

    @BeforeEach
    void setUp() {
        eventUUID = UUID.randomUUID();
        studentId = UUID.randomUUID();

        activeOpenEvent = Event.builder()
                .id(new EventId(eventUUID))
                .name("Open Seminar")
                .category(EventCategory.CULTURAL)
                .type(EventType.OPEN)
                .status(EventStatus.ACTIVE)
                .organizerId(UUID.randomUUID())
                .dateTime(LocalDateTime.now().plusDays(5))
                .build();

        activeCapacityEvent = Event.builder()
                .id(new EventId(eventUUID))
                .name("Workshop")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(10)
                .availableCapacity(5)
                .status(EventStatus.ACTIVE)
                .organizerId(UUID.randomUUID())
                .dateTime(LocalDateTime.now().plusDays(5))
                .build();

        savedRsvp = EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(new EventId(eventUUID))
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .confirmedAt(LocalDateTime.now())
                .build();

        rsvpResponse = EventResponseRsvp.builder()
                .id(UUID.randomUUID())
                .eventId(eventUUID)
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .build();
    }

    @Test
    void shouldCreateRsvpForOpenEventSuccessfully() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(activeOpenEvent));
        when(rsvpRepository.findByEventIdAndStudentId(any(EventId.class), eq(studentId))).thenReturn(Optional.empty());
        when(rsvpRepository.save(any(EventRsvp.class))).thenReturn(savedRsvp);
        when(rsvpMapper.toDTO(savedRsvp)).thenReturn(rsvpResponse);

        EventResponseRsvp result = createRsvpUseCase.execute(eventUUID, studentId);

        assertNotNull(result);
        assertEquals(studentId, result.getStudentId());
        assertEquals(RsvpStatus.CONFIRMED, result.getStatus());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void shouldCreateRsvpForWithCapacityEventAndDecreaseCapacity() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(activeCapacityEvent));
        when(rsvpRepository.findByEventIdAndStudentId(any(EventId.class), eq(studentId))).thenReturn(Optional.empty());
        when(rsvpRepository.save(any(EventRsvp.class))).thenReturn(savedRsvp);
        when(rsvpMapper.toDTO(savedRsvp)).thenReturn(rsvpResponse);

        createRsvpUseCase.execute(eventUUID, studentId);

        assertEquals(4, activeCapacityEvent.getAvailableCapacity());
        verify(eventRepository).save(activeCapacityEvent);
    }

    @Test
    void shouldThrowEventNotFoundExceptionWhenEventDoesNotExist() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> createRsvpUseCase.execute(eventUUID, studentId));

        verify(rsvpRepository, never()).save(any());
    }

    @Test
    void shouldThrowEventNotActiveExceptionWhenEventIsCancelled() {
        activeOpenEvent.setStatus(EventStatus.CANCELLED);
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(activeOpenEvent));

        assertThrows(EventNotActiveException.class,
                () -> createRsvpUseCase.execute(eventUUID, studentId));

        verify(rsvpRepository, never()).save(any());
    }

    @Test
    void shouldThrowRsvpAlreadyExistsExceptionWhenStudentAlreadyRegistered() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(activeOpenEvent));
        when(rsvpRepository.findByEventIdAndStudentId(any(EventId.class), eq(studentId)))
                .thenReturn(Optional.of(savedRsvp));

        assertThrows(RsvpAlreadyExistsException.class,
                () -> createRsvpUseCase.execute(eventUUID, studentId));

        verify(rsvpRepository, never()).save(any());
    }

    @Test
    void shouldThrowEventCapacityFullExceptionWhenNoCapacityAvailable() {
        activeCapacityEvent.setAvailableCapacity(0);
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(activeCapacityEvent));
        when(rsvpRepository.findByEventIdAndStudentId(any(EventId.class), eq(studentId))).thenReturn(Optional.empty());

        assertThrows(EventCapacityFullException.class,
                () -> createRsvpUseCase.execute(eventUUID, studentId));

        verify(rsvpRepository, never()).save(any());
    }

    @Test
    void shouldThrowEventCapacityFullExceptionWhenCapacityIsNull() {
        activeCapacityEvent.setAvailableCapacity(null);
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(activeCapacityEvent));
        when(rsvpRepository.findByEventIdAndStudentId(any(EventId.class), eq(studentId))).thenReturn(Optional.empty());

        assertThrows(EventCapacityFullException.class,
                () -> createRsvpUseCase.execute(eventUUID, studentId));

        verify(rsvpRepository, never()).save(any());
    }
}
