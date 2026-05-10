package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventMapper;
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
import java.util.List;

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
                .id(new EventId("id-001"))
                .name("Tech Talk")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .availableSpots(100)
                .organizerId(new OrganizerId("org-001"))
                .status(EventStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        event2 = Event.builder()
                .id(new EventId("id-002"))
                .name("Workshop")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .maxCapacity(50)
                .availableSpots(50)
                .organizerId(new OrganizerId("org-002"))
                .status(EventStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        response1 = EventResponse.builder().id("id-001").name("Tech Talk").build();
        response2 = EventResponse.builder().id("id-002").name("Workshop").build();
    }

    @Test
    void shouldReturnAllEvents() {
        when(eventRepository.findAll()).thenReturn(List.of(event1, event2));
        when(eventMapper.toResponse(event1)).thenReturn(response1);
        when(eventMapper.toResponse(event2)).thenReturn(response2);

        List<EventResponse> result = getEventsUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Tech Talk", result.get(0).getName());
        assertEquals("Workshop", result.get(1).getName());
    }

    @Test
    void shouldReturnEmptyListWhenNoEvents() {
        when(eventRepository.findAll()).thenReturn(List.of());

        List<EventResponse> result = getEventsUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCallMapperForEachEvent() {
        when(eventRepository.findAll()).thenReturn(List.of(event1, event2));
        when(eventMapper.toResponse(event1)).thenReturn(response1);
        when(eventMapper.toResponse(event2)).thenReturn(response2);

        getEventsUseCase.execute();

        verify(eventMapper).toResponse(event1);
        verify(eventMapper).toResponse(event2);
    }
}
