package edu.eci.patricia.DOWS_patricia.entrypoints.rest.controller;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CancelEventPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CreateEventPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.GetEventByIdPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.GetEventsPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private CreateEventPort createEventPort;

    @Mock
    private CancelEventPort cancelEventPort;

    @Mock
    private GetEventByIdPort getEventByIdPort;

    @Mock
    private GetEventsPort getEventsPort;

    @InjectMocks
    private EventController controller;

    private EventRequest eventRequest;
    private EventResponse eventResponse;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        eventRequest = EventRequest.builder()
                .name("Tech Talk")
                .description("A tech event")
                .dateTime(now)
                .location("Room 101")
                .category(EventCategory.CULTURAL)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .organizerId("organizer-id-456")
                .build();

        eventResponse = EventResponse.builder()
                .id("event-id-123")
                .name("Tech Talk")
                .description("A tech event")
                .dateTime(now)
                .location("Room 101")
                .category(EventCategory.CULTURAL)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .availableSpots(100)
                .organizerId("organizer-id-456")
                .status(EventStatus.ACTIVE)
                .createdAt(now)
                .build();
    }

    @Test
    void createEvent_ShouldReturnCreatedStatus() {
        when(createEventPort.execute(eventRequest)).thenReturn(eventResponse);

        ResponseEntity<EventResponse> result = controller.createEvent(eventRequest);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(eventResponse, result.getBody());
        verify(createEventPort).execute(eventRequest);
    }

    @Test
    void cancelEvent_ShouldReturnOkStatus() {
        when(cancelEventPort.execute("event-id-123")).thenReturn(eventResponse);

        ResponseEntity<EventResponse> result = controller.cancelEvent("event-id-123");

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(eventResponse, result.getBody());
        verify(cancelEventPort).execute("event-id-123");
    }

    @Test
    void getEventById_ShouldReturnOkStatus() {
        when(getEventByIdPort.execute("event-id-123")).thenReturn(eventResponse);

        ResponseEntity<EventResponse> result = controller.getEventById("event-id-123");

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(eventResponse, result.getBody());
        verify(getEventByIdPort).execute("event-id-123");
    }

    @Test
    void getEvents_ShouldReturnOkStatusWithList() {
        when(getEventsPort.execute()).thenReturn(List.of(eventResponse));

        ResponseEntity<List<EventResponse>> result = controller.getEvents();

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(eventResponse, result.getBody().get(0));
        verify(getEventsPort).execute();
    }

    @Test
    void getEvents_WhenNoEvents_ShouldReturnEmptyList() {
        when(getEventsPort.execute()).thenReturn(List.of());

        ResponseEntity<List<EventResponse>> result = controller.getEvents();

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
        verify(getEventsPort).execute();
    }
}
