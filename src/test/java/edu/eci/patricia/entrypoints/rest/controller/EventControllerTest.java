package edu.eci.patricia.entrypoints.rest.controller;

import edu.eci.patricia.application.dto.request.EventFeedRequest;
import edu.eci.patricia.application.dto.request.EventRequest;
import edu.eci.patricia.application.dto.request.EventUpdateRequest;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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

    private UUID organizerId;
    private UUID eventId;
    private EventResponse eventResponse;

    @BeforeEach
    void setUp() {
        organizerId = UUID.randomUUID();
        eventId = UUID.randomUUID();
        eventResponse = EventResponse.builder()
                .id(eventId)
                .name("Tech Talk")
                .category(EventCategory.CULTURAL)
                .type(EventType.OPEN)
                .status(EventStatus.ACTIVE)
                .organizerId(organizerId)
                .build();
    }

    @Test
    void create_shouldReturn201_withEventResponse() {
        EventRequest request = EventRequest.builder()
                .name("Tech Talk")
                .dateTime(LocalDateTime.now().plusDays(5))
                .durationMinutes(60)
                .location("Room A")
                .category(EventCategory.CULTURAL)
                .type(EventType.OPEN)
                .build();

        when(createEventPort.execute(request, organizerId)).thenReturn(eventResponse);

        ResponseEntity<EventResponse> response = controller.create(request, organizerId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(eventResponse, response.getBody());
        verify(createEventPort).execute(request, organizerId);
    }

    @Test
    void getAll_shouldReturn200_withEventList_whenEventsExist() {
        EventFeedRequest filters = EventFeedRequest.builder()
                .category(EventCategory.CULTURAL)
                .build();

        when(getEventsPort.execute(EventCategory.CULTURAL, null)).thenReturn(List.of(eventResponse));

        ResponseEntity<?> response = controller.getAll(filters);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(List.class, response.getBody());
        List<?> body = (List<?>) response.getBody();
        assertEquals(1, body.size());
        verify(getEventsPort).execute(EventCategory.CULTURAL, null);
    }

    @Test
    void getAll_shouldReturnMessage_whenNoEventsFound() {
        EventFeedRequest filters = new EventFeedRequest();

        when(getEventsPort.execute(null, null)).thenReturn(List.of());

        ResponseEntity<?> response = controller.getAll(filters);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("No events available at this time", body.get("message"));
    }

    @Test
    void getAll_shouldPassCategoryAndDateToPort() {
        LocalDate date = LocalDate.now().plusDays(1);
        EventFeedRequest filters = EventFeedRequest.builder()
                .category(EventCategory.CULTURAL)
                .date(date)
                .build();

        when(getEventsPort.execute(EventCategory.CULTURAL, date)).thenReturn(List.of(eventResponse));

        controller.getAll(filters);

        verify(getEventsPort).execute(EventCategory.CULTURAL, date);
    }

    @Test
    void update_shouldReturn200_withUpdatedEventResponse() {
        EventUpdateRequest updateRequest = EventUpdateRequest.builder()
                .name("Updated Talk")
                .dateTime(LocalDateTime.now().plusDays(10))
                .durationMinutes(90)
                .location("Room B")
                .category(EventCategory.CULTURAL)
                .build();

        when(updateEventPort.execute(eventId, updateRequest, organizerId)).thenReturn(eventResponse);

        ResponseEntity<EventResponse> response = controller.update(eventId, updateRequest, organizerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eventResponse, response.getBody());
        verify(updateEventPort).execute(eventId, updateRequest, organizerId);
    }

    @Test
    void getById_shouldReturn200_withEventResponse() {
        when(getEventByIdPort.execute(eventId)).thenReturn(eventResponse);

        ResponseEntity<EventResponse> response = controller.getById(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eventResponse, response.getBody());
        verify(getEventByIdPort).execute(eventId);
    }

    @Test
    void cancel_shouldReturn204_andInvokePort() {
        doNothing().when(cancelEventPort).execute(eventId, organizerId);

        ResponseEntity<Void> response = controller.cancel(eventId, organizerId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(cancelEventPort).execute(eventId, organizerId);
    }
}
