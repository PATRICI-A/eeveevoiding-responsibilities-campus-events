package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelRsvpUseCaseTest {

    @Mock
    private EventRsvpRepositoryPort rsvpRepository;

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventRsvpMapper rsvpMapper;

    @InjectMocks
    private CancelRsvpUseCase cancelRsvpUseCase;

    private Event event;
    private EventRsvp rsvp;
    private EventResponseRsvp response;
    private final String EVENT_ID = "123e4567-e89b-12d3-a456-426614174000";
    private final String RSVP_ID = "rsvp-001";

    @BeforeEach
    void setUp() {
        event = Event.builder()
                .id(new EventId(EVENT_ID))
                .name("Tech Talk")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .availableSpots(99)
                .organizerId(new OrganizerId("org-001"))
                .status(EventStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        rsvp = EventRsvp.builder()
                .id(new RsvpId(RSVP_ID))
                .eventId(new EventId(EVENT_ID))
                .studentId(new StudentId("student-001"))
                .confirmedAt(LocalDateTime.now())
                .status(RsvpStatus.CONFIRMED)
                .build();

        response = EventResponseRsvp.builder()
                .id(RSVP_ID)
                .eventId(EVENT_ID)
                .studentId("student-001")
                .status(RsvpStatus.CANCELLED)
                .build();
    }

    @Test
    void shouldCancelRsvpSuccessfully() {
        when(rsvpRepository.findById(any(RsvpId.class))).thenReturn(Optional.of(rsvp));
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(event));
        when(rsvpRepository.save(rsvp)).thenReturn(rsvp);
        when(rsvpMapper.toResponse(rsvp)).thenReturn(response);

        EventResponseRsvp result = cancelRsvpUseCase.execute(RSVP_ID);

        assertNotNull(result);
        assertEquals(RsvpStatus.CANCELLED, result.getStatus());
        verify(rsvpRepository).save(rsvp);
        verify(eventRepository).save(event);
    }

    @Test
    void shouldRestoreAvailableSpotWhenCancellingRsvp() {
        when(rsvpRepository.findById(any(RsvpId.class))).thenReturn(Optional.of(rsvp));
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(event));
        when(rsvpRepository.save(rsvp)).thenReturn(rsvp);
        when(rsvpMapper.toResponse(rsvp)).thenReturn(response);

        cancelRsvpUseCase.execute(RSVP_ID);

        assertEquals(100, event.getAvailableSpots());
    }

    @Test
    void shouldSetEventStatusToActiveWhenWasFullAndRsvpCancelled() {
        event.setAvailableSpots(0);
        event.setStatus(EventStatus.FULL);
        when(rsvpRepository.findById(any(RsvpId.class))).thenReturn(Optional.of(rsvp));
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(event));
        when(rsvpRepository.save(rsvp)).thenReturn(rsvp);
        when(rsvpMapper.toResponse(rsvp)).thenReturn(response);

        cancelRsvpUseCase.execute(RSVP_ID);

        assertEquals(EventStatus.ACTIVE, event.getStatus());
        assertEquals(1, event.getAvailableSpots());
    }

    @Test
    void shouldSetRsvpStatusToCancelledBeforeSaving() {
        when(rsvpRepository.findById(any(RsvpId.class))).thenReturn(Optional.of(rsvp));
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(event));
        when(rsvpRepository.save(rsvp)).thenReturn(rsvp);
        when(rsvpMapper.toResponse(rsvp)).thenReturn(response);

        cancelRsvpUseCase.execute(RSVP_ID);

        assertEquals(RsvpStatus.CANCELLED, rsvp.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenRsvpNotFound() {
        when(rsvpRepository.findById(any(RsvpId.class))).thenReturn(Optional.empty());

        assertThrows(RsvpNotFoundException.class, () -> cancelRsvpUseCase.execute(RSVP_ID));
        verify(eventRepository, never()).findById(any());
        verify(rsvpRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenEventNotFound() {
        when(rsvpRepository.findById(any(RsvpId.class))).thenReturn(Optional.of(rsvp));
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> cancelRsvpUseCase.execute(RSVP_ID));
        verify(rsvpRepository, never()).save(any());
    }
}
