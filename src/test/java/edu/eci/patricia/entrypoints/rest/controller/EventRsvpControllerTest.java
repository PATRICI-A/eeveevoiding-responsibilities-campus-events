package edu.eci.patricia.entrypoints.rest.controller;

import edu.eci.patricia.application.dto.request.EventRequestRsvp;
import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.model.enums.RsvpAction;
import edu.eci.patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.domain.ports.in.CancelRsvpPort;
import edu.eci.patricia.domain.ports.in.CreateRsvpPort;
import edu.eci.patricia.domain.ports.in.GetRsvpPort;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRsvpControllerTest {

    @Mock
    private CreateRsvpPort createRsvpPort;

    @Mock
    private CancelRsvpPort cancelRsvpPort;

    @Mock
    private GetRsvpPort getRsvpPort;

    @InjectMocks
    private EventRsvpController controller;

    private UUID eventId;
    private UUID studentId;
    private EventResponseRsvp rsvpResponse;
    private EventFeedResponse feedResponse;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        studentId = UUID.randomUUID();

        rsvpResponse = EventResponseRsvp.builder()
                .id(UUID.randomUUID())
                .eventId(eventId)
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .build();

        feedResponse = EventFeedResponse.builder()
                .id(UUID.randomUUID())
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
    void rsvpWithConfirmActionShouldReturnCreatedStatus() {
        when(createRsvpPort.execute(eventId, studentId)).thenReturn(rsvpResponse);

        ResponseEntity<EventResponseRsvp> response = controller.rsvp(eventId, RsvpAction.CONFIRM, studentId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(rsvpResponse, response.getBody());
        verify(createRsvpPort).execute(eventId, studentId);
        verify(cancelRsvpPort, never()).execute(any(), any());
    }

    @Test
    void rsvpWithCancelActionShouldReturnCreatedStatus() {
        EventResponseRsvp cancelledResponse = EventResponseRsvp.builder()
                .id(UUID.randomUUID())
                .eventId(eventId)
                .studentId(studentId)
                .status(RsvpStatus.CANCELLED)
                .build();

        when(cancelRsvpPort.execute(eventId, studentId)).thenReturn(cancelledResponse);

        ResponseEntity<EventResponseRsvp> response = controller.rsvp(eventId, RsvpAction.CANCEL, studentId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(RsvpStatus.CANCELLED, response.getBody().getStatus());
        verify(cancelRsvpPort).execute(eventId, studentId);
        verify(createRsvpPort, never()).execute(any(), any());
    }

    @Test
    void getAgendaShouldReturnListOfEventsWhenNotEmpty() {
        List<EventFeedResponse> events = List.of(feedResponse);

        when(getRsvpPort.execute(studentId)).thenReturn(events);

        ResponseEntity<?> response = controller.getAgenda(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        @SuppressWarnings("rawtypes")
        List body = (List) response.getBody();
        assertEquals(1, body.size());
        verify(getRsvpPort).execute(studentId);
    }

    @Test
    void getAgendaShouldReturnMessageWhenEmpty() {
        when(getRsvpPort.execute(studentId)).thenReturn(List.of());

        ResponseEntity<?> response = controller.getAgenda(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertEquals("No events available at this time", body.get("message"));
        verify(getRsvpPort).execute(studentId);
    }

    @Test
    void getAgendaShouldHandleMultipleEvents() {
        EventFeedResponse event2 = EventFeedResponse.builder()
                .id(UUID.randomUUID())
                .name("Event 2")
                .description("Description 2")
                .dateTime(LocalDate.now().plusDays(2))
                .startTime(LocalTime.of(14, 0))
                .durationMinutes(120)
                .location("Location 2")
                .category(EventCategory.SPORTS)
                .type(EventType.WITH_CAPACITY)
                .availableCapacity(50)
                .status(EventStatus.ACTIVE)
                .build();

        List<EventFeedResponse> events = List.of(feedResponse, event2);

        when(getRsvpPort.execute(studentId)).thenReturn(events);

        ResponseEntity<?> response = controller.getAgenda(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        @SuppressWarnings("rawtypes")
        List body = (List) response.getBody();
        assertEquals(2, body.size());
        verify(getRsvpPort).execute(studentId);
    }
    
}