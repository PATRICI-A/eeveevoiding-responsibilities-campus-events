package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventMapper;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventAlreadyCancelledException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
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
class CancelEventUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private CancelEventUseCase cancelEventUseCase;

    private Event event;
    private EventResponse response;
    private final String EVENT_ID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setUp() {
        event = Event.builder()
                .id(new EventId(EVENT_ID))
                .name("Tech Talk")
                .description("A tech talk event")
                .dateTime(LocalDateTime.of(2026, 6, 15, 10, 0))
                .location("ECI Auditorium")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .availableSpots(100)
                .organizerId(new OrganizerId("org-001"))
                .status(EventStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        response = EventResponse.builder()
                .id(EVENT_ID)
                .name("Tech Talk")
                .status(EventStatus.CANCELLED)
                .build();
    }

    @Test
    void shouldCancelEventSuccessfully() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(event));
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toResponse(event)).thenReturn(response);

        EventResponse result = cancelEventUseCase.execute(EVENT_ID);

        assertNotNull(result);
        assertEquals(EventStatus.CANCELLED, result.getStatus());
        verify(eventRepository).save(event);
    }

    @Test
    void shouldSetEventStatusToCancelledBeforeSaving() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(event));
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toResponse(event)).thenReturn(response);

        cancelEventUseCase.execute(EVENT_ID);

        assertEquals(EventStatus.CANCELLED, event.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenEventNotFound() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> cancelEventUseCase.execute(EVENT_ID));
        verify(eventRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenEventAlreadyCancelled() {
        event.setStatus(EventStatus.CANCELLED);
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(event));

        assertThrows(EventAlreadyCancelledException.class, () -> cancelEventUseCase.execute(EVENT_ID));
        verify(eventRepository, never()).save(any());
    }
}
