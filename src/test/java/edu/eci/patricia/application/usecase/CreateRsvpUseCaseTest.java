package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.domain.exceptions.*;
import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.domain.ports.out.EventRsvpRepositoryPort;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.domain.valueobjects.RsvpId;
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
class CreateRsvpUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventRsvpRepositoryPort rsvpRepository;

    @Mock
    private EventRsvpMapper rsvpMapper;

    @InjectMocks
    private CreateRsvpUseCase createRsvpUseCase;

    private UUID eventId;
    private UUID studentId;
    private EventId evId;
    private Event activeOpenEvent;
    private Event activeWithCapacityEvent;
    private EventRsvp confirmedRsvp;
    private EventRsvp cancelledRsvp;
    private EventResponseRsvp rsvpResponse;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        studentId = UUID.randomUUID();
        evId = new EventId(eventId);

        activeOpenEvent = Event.builder()
                .id(evId)
                .status(EventStatus.ACTIVE)
                .type(EventType.OPEN)
                .availableCapacity(null)
                .build();

        activeWithCapacityEvent = Event.builder()
                .id(evId)
                .status(EventStatus.ACTIVE)
                .type(EventType.WITH_CAPACITY)
                .availableCapacity(10)
                .build();

        confirmedRsvp = EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(evId)
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .build();

        cancelledRsvp = EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(evId)
                .studentId(studentId)
                .status(RsvpStatus.CANCELLED)
                .build();

        rsvpResponse = EventResponseRsvp.builder()
                .id(UUID.randomUUID())
                .eventId(eventId)
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .build();
    }



    @Test
    void execute_shouldDecreaseCapacity_whenNewStudentAndWithCapacityEvent() {
        when(eventRepository.findById(evId)).thenReturn(Optional.of(activeWithCapacityEvent));
        when(rsvpRepository.existsByEventIdAndStudentId(evId, studentId)).thenReturn(false);
        when(rsvpRepository.save(any(EventRsvp.class))).thenReturn(confirmedRsvp);
        when(eventRepository.save(any(Event.class))).thenReturn(activeWithCapacityEvent);
        when(rsvpMapper.toDTO(any(EventRsvp.class))).thenReturn(rsvpResponse);

        createRsvpUseCase.execute(eventId, studentId);

        assertEquals(9, activeWithCapacityEvent.getAvailableCapacity());
        verify(eventRepository).save(activeWithCapacityEvent);
    }


    @Test
    void execute_shouldThrowException_whenEventNotFound() {
        when(eventRepository.findById(evId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> createRsvpUseCase.execute(eventId, studentId));
    }

    @Test
    void execute_shouldThrowException_whenEventIsNotActive() {
        activeOpenEvent.setStatus(EventStatus.CANCELLED);
        when(eventRepository.findById(evId)).thenReturn(Optional.of(activeOpenEvent));

        assertThrows(EventNotActiveException.class,
                () -> createRsvpUseCase.execute(eventId, studentId));
    }

    @Test
    void execute_shouldThrowException_whenEventIsFull() {
        activeWithCapacityEvent.setAvailableCapacity(0);
        when(eventRepository.findById(evId)).thenReturn(Optional.of(activeWithCapacityEvent));

        assertThrows(EventCapacityFullException.class,
                () -> createRsvpUseCase.execute(eventId, studentId));
    }

}
