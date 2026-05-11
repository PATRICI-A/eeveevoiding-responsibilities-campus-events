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
class GetEventByIdUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private GetEventByIdUseCase getEventByIdUseCase;

    private UUID eventUUID;
    private Event event;
    private EventResponse eventResponse;

    @BeforeEach
    void setUp() {
        eventUUID = UUID.randomUUID();
        EventId eventId = new EventId(eventUUID);

        event = Event.builder()
                .id(eventId)
                .name("Tech Talk")
                .description("Academic event")
                .dateTime(LocalDateTime.now().plusDays(5))
                .durationMinutes(90)
                .location("Auditorium A")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(100)
                .availableCapacity(100)
                .status(EventStatus.ACTIVE)
                .organizerId(UUID.randomUUID())
                .build();

        eventResponse = EventResponse.builder()
                .id(eventUUID)
                .name("Tech Talk")
                .status(EventStatus.ACTIVE)
                .build();
    }

    @Test
    void shouldReturnEventResponseWhenEventExists() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(event));
        when(eventMapper.toDTO(event)).thenReturn(eventResponse);

        EventResponse result = getEventByIdUseCase.execute(eventUUID);

        assertNotNull(result);
        assertEquals(eventUUID, result.getId());
        assertEquals("Tech Talk", result.getName());
        verify(eventRepository).findById(any(EventId.class));
        verify(eventMapper).toDTO(event);
    }

    @Test
    void shouldThrowEventNotFoundExceptionWhenEventDoesNotExist() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> getEventByIdUseCase.execute(eventUUID));
        verify(eventRepository).findById(any(EventId.class));
        verify(eventMapper, never()).toDTO(any());
    }
}
