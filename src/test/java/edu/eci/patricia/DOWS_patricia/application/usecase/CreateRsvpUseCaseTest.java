package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequestRsvp;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotAvailableException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRsvpUseCaseTest {

    @Mock
    private EventRsvpRepositoryPort rsvpRepository;

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventRsvpMapper rsvpMapper;

    @InjectMocks
    private CreateRsvpUseCase createRsvpUseCase;

    private Event event;
    private EventRequestRsvp request;
    private EventRsvp rsvp;
    private EventResponseRsvp response;
    private final String EVENT_ID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setUp() {
        event = Event.builder()
                .id(new EventId(EVENT_ID))
                .name("Tech Talk")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .availableSpots(10)
                .organizerId(new OrganizerId("org-001"))
                .status(EventStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        request = EventRequestRsvp.builder()
                .eventId(EVENT_ID)
                .studentId("student-001")
                .status(RsvpStatus.CONFIRMED)
                .build();

        rsvp = EventRsvp.builder()
                .id(new RsvpId("rsvp-001"))
                .eventId(new EventId(EVENT_ID))
                .studentId(new StudentId("student-001"))
                .confirmedAt(LocalDateTime.now())
                .status(RsvpStatus.CONFIRMED)
                .build();

        response = EventResponseRsvp.builder()
                .id("rsvp-001")
                .eventId(EVENT_ID)
                .studentId("student-001")
                .status(RsvpStatus.CONFIRMED)
                .build();
    }

    @Test
    void shouldCreateRsvpSuccessfully() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(event));
        when(rsvpMapper.toDomain(request)).thenReturn(rsvp);
        when(rsvpRepository.save(rsvp)).thenReturn(rsvp);
        when(rsvpMapper.toResponse(rsvp)).thenReturn(response);

        EventResponseRsvp result = createRsvpUseCase.execute(request);

        assertNotNull(result);
        assertEquals("rsvp-001", result.getId());
        verify(eventRepository).save(event);
        verify(rsvpRepository).save(rsvp);
    }

    @Test
    void shouldDecrementAvailableSpotsWhenRsvpCreated() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(event));
        when(rsvpMapper.toDomain(request)).thenReturn(rsvp);
        when(rsvpRepository.save(rsvp)).thenReturn(rsvp);
        when(rsvpMapper.toResponse(rsvp)).thenReturn(response);

        createRsvpUseCase.execute(request);

        assertEquals(9, event.getAvailableSpots());
    }

    @Test
    void shouldSetEventStatusToFullWhenNoSpotsRemain() {
        event.setAvailableSpots(1);
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(event));
        when(rsvpMapper.toDomain(request)).thenReturn(rsvp);
        when(rsvpRepository.save(rsvp)).thenReturn(rsvp);
        when(rsvpMapper.toResponse(rsvp)).thenReturn(response);

        createRsvpUseCase.execute(request);

        assertEquals(0, event.getAvailableSpots());
        assertEquals(EventStatus.FULL, event.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenEventNotFound() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> createRsvpUseCase.execute(request));
        verify(rsvpRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenEventIsCancelled() {
        event.setStatus(EventStatus.CANCELLED);
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(event));

        assertThrows(EventNotAvailableException.class, () -> createRsvpUseCase.execute(request));
        verify(rsvpRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenEventIsFull() {
        event.setStatus(EventStatus.FULL);
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(event));

        assertThrows(EventNotAvailableException.class, () -> createRsvpUseCase.execute(request));
        verify(rsvpRepository, never()).save(any());
    }
}
