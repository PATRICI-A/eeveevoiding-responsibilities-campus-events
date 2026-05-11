package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.response.EventResponse;
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
import java.time.LocalDateTime;
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

    private Event event1;
    private Event event2;
    private EventResponse response1;
    private EventResponse response2;

    @BeforeEach
    void setUp() {
        event1 = Event.builder()
                .id(EventId.generate())
                .name("Event One")
                .category(EventCategory.ACADEMIC)
                .status(EventStatus.ACTIVE)
                .type(EventType.OPEN)
                .organizerId(UUID.randomUUID())
                .dateTime(LocalDateTime.now().plusDays(3))
                .build();

        event2 = Event.builder()
                .id(EventId.generate())
                .name("Event Two")
                .category(EventCategory.CULTURAL)
                .status(EventStatus.ACTIVE)
                .type(EventType.OPEN)
                .organizerId(UUID.randomUUID())
                .dateTime(LocalDateTime.now().plusDays(5))
                .build();

        response1 = EventResponse.builder().id(UUID.randomUUID()).name("Event One").build();
        response2 = EventResponse.builder().id(UUID.randomUUID()).name("Event Two").build();
    }

    @Test
    void shouldReturnAllActiveEventsWhenNoCategoryOrDateFilter() {
        when(eventRepository.findActiveEvents(null, null)).thenReturn(List.of(event1, event2));
        when(eventMapper.toDTO(event1)).thenReturn(response1);
        when(eventMapper.toDTO(event2)).thenReturn(response2);

        List<EventResponse> result = getEventsUseCase.execute(null, null);

        assertEquals(2, result.size());
        verify(eventRepository).findActiveEvents(null, null);
        verify(eventMapper, times(2)).toDTO(any(Event.class));
    }

    @Test
    void shouldReturnFilteredEventsByCategory() {
        when(eventRepository.findActiveEvents(EventCategory.ACADEMIC, null)).thenReturn(List.of(event1));
        when(eventMapper.toDTO(event1)).thenReturn(response1);

        List<EventResponse> result = getEventsUseCase.execute(EventCategory.ACADEMIC, null);

        assertEquals(1, result.size());
        assertEquals("Event One", result.get(0).getName());
        verify(eventRepository).findActiveEvents(EventCategory.ACADEMIC, null);
    }

    @Test
    void shouldReturnFilteredEventsByDate() {
        LocalDate date = LocalDate.now().plusDays(3);
        when(eventRepository.findActiveEvents(null, date)).thenReturn(List.of(event1));
        when(eventMapper.toDTO(event1)).thenReturn(response1);

        List<EventResponse> result = getEventsUseCase.execute(null, date);

        assertEquals(1, result.size());
        verify(eventRepository).findActiveEvents(null, date);
    }

    @Test
    void shouldReturnFilteredEventsByCategoryAndDate() {
        LocalDate date = LocalDate.now().plusDays(3);
        when(eventRepository.findActiveEvents(EventCategory.ACADEMIC, date)).thenReturn(List.of(event1));
        when(eventMapper.toDTO(event1)).thenReturn(response1);

        List<EventResponse> result = getEventsUseCase.execute(EventCategory.ACADEMIC, date);

        assertEquals(1, result.size());
        verify(eventRepository).findActiveEvents(EventCategory.ACADEMIC, date);
    }

    @Test
    void shouldReturnEmptyListWhenNoEventsFound() {
        when(eventRepository.findActiveEvents(EventCategory.SPORTS, null)).thenReturn(List.of());

        List<EventResponse> result = getEventsUseCase.execute(EventCategory.SPORTS, null);

        assertTrue(result.isEmpty());
        verify(eventMapper, never()).toDTO(any());
    }
}
