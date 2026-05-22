package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.mapper.EventMapper;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetEventsUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private GetEventsUseCase getEventsUseCase;

    private Event activeEvent1;
    private Event activeEvent2;
    private Event cancelledEvent;
    private EventFeedResponse feedResponse1;
    private EventFeedResponse feedResponse2;

    @BeforeEach
    void setUp() {
        activeEvent1 = Event.builder()
                .id(EventId.generate())
                .name("Evento 1")
                .status(EventStatus.ACTIVE)
                .type(EventType.OPEN)
                .category(EventCategory.ACADEMIC)
                .build();

        activeEvent2 = Event.builder()
                .id(EventId.generate())
                .name("Evento 2")
                .status(EventStatus.ACTIVE)
                .type(EventType.OPEN)
                .category(EventCategory.ACADEMIC)
                .build();

        cancelledEvent = Event.builder()
                .id(EventId.generate())
                .name("Evento Cancelado")
                .status(EventStatus.CANCELLED)
                .type(EventType.OPEN)
                .category(EventCategory.ACADEMIC)
                .build();

        feedResponse1 = EventFeedResponse.builder()
                .id(UUID.randomUUID())
                .name("Evento 1")
                .status(EventStatus.ACTIVE)
                .build();

        feedResponse2 = EventFeedResponse.builder()
                .id(UUID.randomUUID())
                .name("Evento 2")
                .status(EventStatus.ACTIVE)
                .build();
    }

    @Test
    void execute_shouldReturnAllActiveEvents_whenNoFilters() {
        when(eventRepository.findActiveEvents(null, null)).thenReturn(List.of(activeEvent1, activeEvent2));
        when(eventMapper.toFeedDTO(activeEvent1)).thenReturn(feedResponse1);
        when(eventMapper.toFeedDTO(activeEvent2)).thenReturn(feedResponse2);

        List<EventFeedResponse> result = getEventsUseCase.execute(null, null);

        assertEquals(2, result.size());
    }

    @Test
    void execute_shouldReturnEmptyList_whenNoEventsFound() {
        when(eventRepository.findActiveEvents(null, null)).thenReturn(List.of());

        List<EventFeedResponse> result = getEventsUseCase.execute(null, null);

        assertTrue(result.isEmpty());
    }

    @Test
    void execute_shouldFilterByCategory_whenCategoryProvided() {
        when(eventRepository.findActiveEvents(EventCategory.ACADEMIC, null)).thenReturn(List.of(activeEvent1));
        when(eventMapper.toFeedDTO(activeEvent1)).thenReturn(feedResponse1);

        List<EventFeedResponse> result = getEventsUseCase.execute(EventCategory.ACADEMIC, null);

        assertEquals(1, result.size());
        verify(eventRepository).findActiveEvents(EventCategory.ACADEMIC, null);
    }

    @Test
    void execute_shouldFilterByDate_whenDateProvided() {
        LocalDate date = LocalDate.now().plusDays(1);
        when(eventRepository.findActiveEvents(null, date)).thenReturn(List.of(activeEvent2));
        when(eventMapper.toFeedDTO(activeEvent2)).thenReturn(feedResponse2);

        List<EventFeedResponse> result = getEventsUseCase.execute(null, date);

        assertEquals(1, result.size());
        verify(eventRepository).findActiveEvents(null, date);
    }

    @Test
    void execute_shouldExcludeCancelledEvents_whenRepositoryReturnsMixed() {
        when(eventRepository.findActiveEvents(null, null)).thenReturn(List.of(activeEvent1, cancelledEvent));
        when(eventMapper.toFeedDTO(activeEvent1)).thenReturn(feedResponse1);

        List<EventFeedResponse> result = getEventsUseCase.execute(null, null);

        assertEquals(1, result.size());
    }
}
