package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventMapper;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.OrganizerId;
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
class GetEventByIdUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private GetEventByIdUseCase getEventByIdUseCase;

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
                .status(EventStatus.ACTIVE)
                .build();
    }

    @Test
    void shouldReturnEventWhenFound() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(event));
        when(eventMapper.toResponse(event)).thenReturn(response);

        EventResponse result = getEventByIdUseCase.execute(EVENT_ID);

        assertNotNull(result);
        assertEquals(EVENT_ID, result.getId());
        assertEquals("Tech Talk", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenEventNotFound() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> getEventByIdUseCase.execute(EVENT_ID));
    }

    @Test
    void shouldCallMapperToResponseWhenEventFound() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(event));
        when(eventMapper.toResponse(event)).thenReturn(response);

        getEventByIdUseCase.execute(EVENT_ID);

        verify(eventMapper).toResponse(event);
    }
}
