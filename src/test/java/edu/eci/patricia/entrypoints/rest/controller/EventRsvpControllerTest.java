package edu.eci.patricia.entrypoints.rest.controller;

import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.domain.model.enums.*;
import edu.eci.patricia.domain.ports.in.CancelRsvpPort;
import edu.eci.patricia.domain.ports.in.CreateRsvpPort;
import edu.eci.patricia.domain.ports.in.GetRsvpPort;
import edu.eci.patricia.domain.ports.out.EventRsvpRepositoryPort;
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
import java.util.Collections;
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

    @Mock
    private EventRsvpMapper rsvpMapper;

    @Mock
    private EventRsvpRepositoryPort eventRsvpRepository;

    @InjectMocks
    private EventRsvpController eventRsvpController;

    private UUID studentId;
    private UUID eventId;
    private EventResponseRsvp confirmedRsvp;
    private EventResponseRsvp cancelledRsvp;
    private EventFeedResponse eventFeedResponse;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        eventId = UUID.randomUUID();

        confirmedRsvp = EventResponseRsvp.builder()
                .id(UUID.randomUUID())
                .eventId(eventId)
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .build();

        cancelledRsvp = EventResponseRsvp.builder()
                .id(UUID.randomUUID())
                .eventId(eventId)
                .studentId(studentId)
                .status(RsvpStatus.CANCELLED)
                .build();

        eventFeedResponse = EventFeedResponse.builder()
                .id(eventId)
                .name("Tech Talk 2025")
                .dateTime(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(9, 0))
                .durationMinutes(60)
                .location("Main Hall")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .availableCapacity(50)
                .status(EventStatus.ACTIVE)
                .build();
    }

    @Test
    void rsvp_confirm_debeRetornar201YRsvpConfirmado() {
        when(createRsvpPort.execute(eventId, studentId)).thenReturn(confirmedRsvp);

        ResponseEntity<EventResponseRsvp> response = eventRsvpController.rsvp(
                eventId, RsvpAction.CONFIRM, studentId.toString());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(RsvpStatus.CONFIRMED, response.getBody().getStatus());
        assertEquals(eventId, response.getBody().getEventId());
        assertEquals(studentId, response.getBody().getStudentId());
    }

    @Test
    void rsvp_confirm_debeInvocarCreateRsvpPortYNoInvocarCancelRsvpPort() {
        when(createRsvpPort.execute(eventId, studentId)).thenReturn(confirmedRsvp);

        eventRsvpController.rsvp(eventId, RsvpAction.CONFIRM, studentId.toString());

        verify(createRsvpPort, times(1)).execute(eventId, studentId);
        verify(cancelRsvpPort, never()).execute(any(), any());
    }

    @Test
    void rsvp_cancel_debeRetornar201YRsvpCancelado() {
        when(cancelRsvpPort.execute(eventId, studentId)).thenReturn(cancelledRsvp);

        ResponseEntity<EventResponseRsvp> response = eventRsvpController.rsvp(
                eventId, RsvpAction.CANCEL, studentId.toString());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(RsvpStatus.CANCELLED, response.getBody().getStatus());
    }

    @Test
    void rsvp_cancel_debeInvocarCancelRsvpPortYNoInvocarCreateRsvpPort() {
        when(cancelRsvpPort.execute(eventId, studentId)).thenReturn(cancelledRsvp);

        eventRsvpController.rsvp(eventId, RsvpAction.CANCEL, studentId.toString());

        verify(cancelRsvpPort, times(1)).execute(eventId, studentId);
        verify(createRsvpPort, never()).execute(any(), any());
    }

    @Test
    void rsvp_confirm_debeInvocarCreateRsvpPortConUUIDsCorrectos() {
        when(createRsvpPort.execute(eventId, studentId)).thenReturn(confirmedRsvp);

        eventRsvpController.rsvp(eventId, RsvpAction.CONFIRM, studentId.toString());

        verify(createRsvpPort).execute(eventId, studentId);
    }

    @Test
    void rsvp_cancel_debeInvocarCancelRsvpPortConUUIDsCorrectos() {
        when(cancelRsvpPort.execute(eventId, studentId)).thenReturn(cancelledRsvp);

        eventRsvpController.rsvp(eventId, RsvpAction.CANCEL, studentId.toString());

        verify(cancelRsvpPort).execute(eventId, studentId);
    }

    @Test
    void getAgenda_conEventosConfirmados_debeRetornar200YLista() {
        when(getRsvpPort.execute(studentId)).thenReturn(List.of(eventFeedResponse));

        ResponseEntity<?> response = eventRsvpController.getAgenda(studentId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(List.class, response.getBody());
        List<?> body = (List<?>) response.getBody();
        assertEquals(1, body.size());
    }

    @Test
    void getAgenda_sinEventos_debeRetornarMensaje() {
        when(getRsvpPort.execute(studentId)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = eventRsvpController.getAgenda(studentId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("No events available at this time", body.get("message"));
    }

    @Test
    void getAgenda_debeInvocarGetRsvpPortConStudentIdCorrecto() {
        when(getRsvpPort.execute(studentId)).thenReturn(Collections.emptyList());

        eventRsvpController.getAgenda(studentId.toString());

        verify(getRsvpPort, times(1)).execute(studentId);
    }

    @Test
    void getAgenda_conMultiplesEventos_debeRetornarTodos() {
        EventFeedResponse otroEvento = EventFeedResponse.builder()
                .id(UUID.randomUUID())
                .name("Cultural Fair")
                .category(EventCategory.CULTURAL)
                .status(EventStatus.ACTIVE)
                .build();

        when(getRsvpPort.execute(studentId)).thenReturn(List.of(eventFeedResponse, otroEvento));

        ResponseEntity<?> response = eventRsvpController.getAgenda(studentId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<?> body = (List<?>) response.getBody();
        assertEquals(2, body.size());
    }
}
