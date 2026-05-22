package edu.eci.patricia.entrypoints.rest.controller;

import edu.eci.patricia.application.dto.request.EventFeedRequest;
import edu.eci.patricia.application.dto.request.EventRequest;
import edu.eci.patricia.application.dto.request.EventUpdateRequest;
import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.dto.response.EventResponse;
import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.ports.in.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private CreateEventPort createEventPort;

    @Mock
    private GetEventsPort getEventsPort;

    @Mock
    private UpdateEventPort updateEventPort;

    @Mock
    private CancelEventPort cancelEventPort;

    @Mock
    private GetEventByIdPort getEventByIdPort;

    @InjectMocks
    private EventController controller;

    private UUID eventId;
    private UUID organizerId;
    private EventRequest eventRequest;
    private EventUpdateRequest updateRequest;
    private EventResponse eventResponse;
    private EventFeedResponse feedResponse;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        organizerId = UUID.randomUUID();

        eventRequest = EventRequest.builder()
                .name("Test Event")
                .description("Description")
                .dateTime(LocalDate.now().plusDays(1))
                .startTime("10:00")
                .duration(60)
                .location("Test Location")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .build();

        updateRequest = EventUpdateRequest.builder()
                .name("Updated Event")
                .description("Updated Description")
                .dateTime(LocalDate.now().plusDays(2))
                .startTime("11:00")
                .duration(90)
                .location("Updated Location")
                .category(EventCategory.CULTURAL)
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(200)
                .build();

        eventResponse = EventResponse.builder()
                .id(eventId)
                .name("Test Event")
                .dateTime(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .duration(60)
                .location("Test Location")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .status(EventStatus.ACTIVE)
                .qrCode("qr123")
                .build();

        feedResponse = EventFeedResponse.builder()
                .id(eventId)
                .name("Test Event")
                .description("Description")
                .dateTime(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(10, 0))
                .durationMinutes(60)
                .location("Test Location")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .availableCapacity(100)
                .status(EventStatus.ACTIVE)
                .qrCode("qr123")
                .build();
    }

    @Test
    void createShouldReturnCreatedStatusWithEventResponse() {
        when(createEventPort.execute(eventRequest, organizerId)).thenReturn(eventResponse);

        ResponseEntity<EventResponse> response = controller.create(eventRequest, organizerId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(eventResponse, response.getBody());
        verify(createEventPort).execute(eventRequest, organizerId);
    }

    @Test
    void getAllShouldReturnListOfEventsWhenNotEmpty() {
        EventFeedRequest filters = EventFeedRequest.builder()
                .category(EventCategory.ACADEMIC)
                .date(LocalDate.now())
                .build();

        List<EventFeedResponse> events = List.of(feedResponse);

        when(getEventsPort.execute(filters.getCategory(), filters.getDate())).thenReturn(events);

        ResponseEntity<?> response = controller.getAll(filters);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        @SuppressWarnings("rawtypes")
        List body = (List) response.getBody();
        assertEquals(1, body.size());
        verify(getEventsPort).execute(filters.getCategory(), filters.getDate());
    }

    @Test
    void getAllShouldReturnMessageWhenEmpty() {
        EventFeedRequest filters = EventFeedRequest.builder().build();

        when(getEventsPort.execute(filters.getCategory(), filters.getDate())).thenReturn(List.of());

        ResponseEntity<?> response = controller.getAll(filters);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertEquals("No events available at this time", body.get("message"));
        verify(getEventsPort).execute(filters.getCategory(), filters.getDate());
    }

    @Test
    void getAllWithNullFiltersShouldWork() {
        EventFeedRequest filters = EventFeedRequest.builder()
                .category(null)
                .date(null)
                .build();

        when(getEventsPort.execute(null, null)).thenReturn(List.of(feedResponse));

        ResponseEntity<?> response = controller.getAll(filters);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(getEventsPort).execute(null, null);
    }

    @Test
    void updateShouldReturnOkWithUpdatedEvent() {
        when(updateEventPort.execute(eventId, updateRequest, organizerId)).thenReturn(eventResponse);

        ResponseEntity<EventResponse> response = controller.update(eventId, updateRequest, organizerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(eventResponse, response.getBody());
        verify(updateEventPort).execute(eventId, updateRequest, organizerId);
    }

    @Test
    void getByIdShouldReturnEventFeedResponse() {
        when(getEventByIdPort.execute(eventId)).thenReturn(feedResponse);

        ResponseEntity<EventFeedResponse> response = controller.getById(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(feedResponse, response.getBody());
        verify(getEventByIdPort).execute(eventId);
    }

    @Test
    void cancelShouldReturnNoContent() {
        doNothing().when(cancelEventPort).execute(eventId, organizerId);

        ResponseEntity<Void> response = controller.cancel(eventId, organizerId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(cancelEventPort).execute(eventId, organizerId);
    }

    @Test
    void createShouldHandleEventWithNullMaxCapacity() {
        EventRequest requestWithoutCapacity = EventRequest.builder()
                .name("No Capacity Event")
                .description("Description")
                .dateTime(LocalDate.now().plusDays(1))
                .startTime("10:00")
                .duration(60)
                .location("Location")
                .category(EventCategory.SPORTS)
                .type(EventType.OPEN)
                .maxCapacity(null)
                .build();

        when(createEventPort.execute(requestWithoutCapacity, organizerId)).thenReturn(eventResponse);

        ResponseEntity<EventResponse> response = controller.create(requestWithoutCapacity, organizerId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(createEventPort).execute(requestWithoutCapacity, organizerId);
    }
}