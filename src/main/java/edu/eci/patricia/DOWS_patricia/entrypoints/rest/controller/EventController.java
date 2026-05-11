package edu.eci.patricia.DOWS_patricia.entrypoints.rest.controller;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventFeedRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequestRsvp;
import edu.eci.patricia.DOWS_patricia.application.dto.request.EventUpdateRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CancelEventPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CancelRsvpPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CreateEventPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CreateRsvpPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.GetEventsPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.UpdateEventPort;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpAction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final CreateEventPort createEventPort;
    private final GetEventsPort getEventsPort;
    private final UpdateEventPort updateEventPort;
    private final CancelEventPort cancelEventPort;

    @PostMapping
    public ResponseEntity<EventResponse> create(
            @Valid @RequestBody EventRequest request,
            @RequestHeader("X-User-Id") UUID organizerId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createEventPort.execute(request, organizerId));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAll(
            @Valid EventFeedRequest filters) {
        return ResponseEntity.ok(
                getEventsPort.execute(filters.getCategory(), filters.getDate()));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> update(
            @PathVariable UUID eventId,
            @Valid @RequestBody EventUpdateRequest request,
            @RequestHeader("X-User-Id") UUID organizerId) {
        return ResponseEntity.ok(updateEventPort.execute(eventId, request, organizerId));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> cancel(
            @PathVariable UUID eventId,
            @RequestHeader("X-User-Id") UUID organizerId) {
        cancelEventPort.execute(eventId, organizerId);
        return ResponseEntity.noContent().build();
    }
}