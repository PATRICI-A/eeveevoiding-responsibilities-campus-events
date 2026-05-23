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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    private EventController eventController;

    private UUID organizerId;
    private UUID eventId;
    private EventRequest eventRequest;
    private EventUpdateRequest eventUpdateRequest;
    private EventResponse eventResponse;
    private EventFeedResponse eventFeedResponse;

    @BeforeEach
    void setUp() {
        organizerId = UUID.randomUUID();
        eventId = UUID.randomUUID();

        eventRequest = EventRequest.builder()
                .name("Tech Talk 2025")
                .description("A great tech event")
                .dateTime(LocalDate.now().plusDays(10))
                .startTime("10:00")
                .duration(60)
                .location("Auditorium A")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(100)
                .build();

        eventUpdateRequest = EventUpdateRequest.builder()
                .name("Tech Talk Updated")
                .description("Updated description")
                .dateTime(LocalDate.now().plusDays(15))
                .startTime("11:00")
                .duration(90)
                .location("Auditorium B")
                .category(EventCategory.CULTURAL)
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(200)
                .build();

        eventResponse = EventResponse.builder()
                .id(eventId)
                .name("Tech Talk 2025")
                .dateTime(LocalDate.now().plusDays(10))
                .startTime(LocalTime.of(10, 0))
                .duration(60)
                .location("Auditorium A")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .status(EventStatus.ACTIVE)
                .qrCode("qr-code-data")
                .build();

        eventFeedResponse = EventFeedResponse.builder()
                .id(eventId)
                .name("Tech Talk 2025")
                .dateTime(LocalDate.now().plusDays(10))
                .startTime(LocalTime.of(10, 0))
                .durationMinutes(60)
                .location("Auditorium A")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .availableCapacity(100)
                .status(EventStatus.ACTIVE)
                .build();
    }

    @Test
    void create_debeRetornar201YEventResponse() {
        when(createEventPort.execute(any(EventRequest.class), eq(organizerId)))
                .thenReturn(eventResponse);

        ResponseEntity<EventResponse> response = eventController.create(eventRequest, organizerId.toString());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(eventResponse.getId(), response.getBody().getId());
        assertEquals(eventResponse.getName(), response.getBody().getName());
        verify(createEventPort).execute(any(EventRequest.class), eq(organizerId));
    }

    @Test
    void create_debeInvocarCreateEventPortConOrganizadorCorrecto() {
        when(createEventPort.execute(any(EventRequest.class), eq(organizerId)))
                .thenReturn(eventResponse);

        eventController.create(eventRequest, organizerId.toString());

        verify(createEventPort, times(1)).execute(eventRequest, organizerId);
    }

    @Test
    void getAll_conEventosDisponibles_debeRetornar200YListaDeEventos() {
        EventFeedRequest filters = EventFeedRequest.builder()
                .category(EventCategory.ACADEMIC)
                .date(LocalDate.now().plusDays(1))
                .build();

        when(getEventsPort.execute(EventCategory.ACADEMIC, filters.getDate()))
                .thenReturn(List.of(eventFeedResponse));

        ResponseEntity<?> response = eventController.getAll(filters);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(List.class, response.getBody());
        List<?> body = (List<?>) response.getBody();
        assertEquals(1, body.size());
    }

    @Test
    void getAll_sinEventos_debeRetornarMensaje() {
        EventFeedRequest filters = EventFeedRequest.builder().build();

        when(getEventsPort.execute(null, null))
                .thenReturn(Collections.emptyList());

        ResponseEntity<?> response = eventController.getAll(filters);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("No events available at this time", body.get("message"));
    }

    @Test
    void getAll_sinFiltros_debeInvocarGetEventsPortConNulos() {
        EventFeedRequest filters = EventFeedRequest.builder().build();

        when(getEventsPort.execute(null, null)).thenReturn(Collections.emptyList());

        eventController.getAll(filters);

        verify(getEventsPort).execute(null, null);
    }

    @Test
    void update_debeRetornar200YEventoActualizado() {
        when(updateEventPort.execute(eq(eventId), any(EventUpdateRequest.class), eq(organizerId)))
                .thenReturn(eventResponse);

        ResponseEntity<EventResponse> response = eventController.update(eventId, eventUpdateRequest, organizerId.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(eventResponse.getId(), response.getBody().getId());
        verify(updateEventPort).execute(eq(eventId), any(EventUpdateRequest.class), eq(organizerId));
    }

    @Test
    void update_debeInvocarUpdateEventPortConParametrosCorrectos() {
        when(updateEventPort.execute(eq(eventId), eq(eventUpdateRequest), eq(organizerId)))
                .thenReturn(eventResponse);

        eventController.update(eventId, eventUpdateRequest, organizerId.toString());

        verify(updateEventPort, times(1)).execute(eventId, eventUpdateRequest, organizerId);
    }

    @Test
    void getById_debeRetornar200YEventoEncontrado() {
        when(getEventByIdPort.execute(eventId)).thenReturn(eventFeedResponse);

        ResponseEntity<EventFeedResponse> response = eventController.getById(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(eventFeedResponse.getId(), response.getBody().getId());
        verify(getEventByIdPort).execute(eventId);
    }

    @Test
    void getById_debeInvocarGetEventByIdPortConIdCorrecto() {
        when(getEventByIdPort.execute(eventId)).thenReturn(eventFeedResponse);

        eventController.getById(eventId);

        verify(getEventByIdPort, times(1)).execute(eventId);
    }

    @Test
    void cancel_debeRetornar204SinContenido() {
        doNothing().when(cancelEventPort).execute(eq(eventId), eq(organizerId));

        ResponseEntity<Void> response = eventController.cancel(eventId, organizerId.toString());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(cancelEventPort).execute(eq(eventId), eq(organizerId));
    }

    @Test
    void cancel_debeInvocarCancelEventPortConParametrosCorrectos() {
        doNothing().when(cancelEventPort).execute(eq(eventId), eq(organizerId));

        eventController.cancel(eventId, organizerId.toString());

        verify(cancelEventPort, times(1)).execute(eventId, organizerId);
    }
}
