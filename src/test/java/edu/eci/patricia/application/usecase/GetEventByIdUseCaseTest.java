package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.mapper.EventMapper;
import edu.eci.patricia.domain.exceptions.EventNotFoundException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetEventByIdUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private GetEventByIdUseCase getEventByIdUseCase;

    private UUID eventId;
    private EventId evId;
    private Event activeEvent;
    private Event cancelledEvent;
    private EventFeedResponse feedResponse;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        evId = new EventId(eventId);

        activeEvent = Event.builder()
                .id(evId)
                .name("Evento Activo")
                .status(EventStatus.ACTIVE)
                .type(EventType.OPEN)
                .category(EventCategory.ACADEMIC)
                .build();

        cancelledEvent = Event.builder()
                .id(evId)
                .name("Evento Cancelado")
                .status(EventStatus.CANCELLED)
                .type(EventType.OPEN)
                .category(EventCategory.ACADEMIC)
                .build();

        feedResponse = EventFeedResponse.builder()
                .id(eventId)
                .name("Evento Activo")
                .status(EventStatus.ACTIVE)
                .build();
    }

    @Test
    void execute_shouldReturnEvent_whenEventExistsAndIsActive() {
        when(eventRepository.findById(evId)).thenReturn(Optional.of(activeEvent));
        when(eventMapper.toFeedDTO(activeEvent)).thenReturn(feedResponse);

        EventFeedResponse result = getEventByIdUseCase.execute(eventId);

        assertNotNull(result);
        assertEquals("Evento Activo", result.getName());
    }

    @Test
    void execute_shouldThrowException_whenEventNotFound() {
        when(eventRepository.findById(evId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> getEventByIdUseCase.execute(eventId));
    }

    @Test
    void execute_shouldThrowException_whenEventIsNotActive() {
        when(eventRepository.findById(evId)).thenReturn(Optional.of(cancelledEvent));

        assertThrows(EventNotFoundException.class,
                () -> getEventByIdUseCase.execute(eventId));
    }
}
