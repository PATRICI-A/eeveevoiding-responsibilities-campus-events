package edu.eci.patricia.DOWS_patricia.entrypoints.rest.controller;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequestRsvp;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CancelRsvpPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CreateRsvpPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRsvpControllerTest {

    @Mock
    private CreateRsvpPort createRsvpPort;

    @Mock
    private CancelRsvpPort cancelRsvpPort;

    @InjectMocks
    private EventRsvpController controller;

    private EventRequestRsvp rsvpRequest;
    private EventResponseRsvp rsvpResponse;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        rsvpRequest = EventRequestRsvp.builder()
                .eventId("event-id-456")
                .studentId("student-id-789")
                .confirmedAt(now)
                .status(RsvpStatus.CONFIRMED)
                .build();

        rsvpResponse = EventResponseRsvp.builder()
                .id("rsvp-id-123")
                .eventId("event-id-456")
                .studentId("student-id-789")
                .confirmedAt(now)
                .status(RsvpStatus.CONFIRMED)
                .build();
    }

    @Test
    void createRsvp_ShouldReturnCreatedStatus() {
        when(createRsvpPort.execute(rsvpRequest)).thenReturn(rsvpResponse);

        ResponseEntity<EventResponseRsvp> result = controller.createRsvp(rsvpRequest);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(rsvpResponse, result.getBody());
        verify(createRsvpPort).execute(rsvpRequest);
    }

    @Test
    void cancelRsvp_ShouldReturnOkStatus() {
        when(cancelRsvpPort.execute("rsvp-id-123")).thenReturn(rsvpResponse);

        ResponseEntity<EventResponseRsvp> result = controller.cancelRsvp("rsvp-id-123");

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(rsvpResponse, result.getBody());
        verify(cancelRsvpPort).execute("rsvp-id-123");
    }
}
