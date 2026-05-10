package edu.eci.patricia.DOWS_patricia.entrypoints.rest.controller;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CancelEventPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CreateEventPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.GetEventsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final CreateEventPort createEventPort;
    private final CancelEventPort cancelEventPort;
    private final GetEventByIdPort getEventByIdPort;
    private final GetEventsPort getEventsPort;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createEventPort.execute(request));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<EventResponse> cancelEvent(@PathVariable String id) {
        return ResponseEntity.ok(cancelEventPort.execute(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable String id) {
        return ResponseEntity.ok(getEventByIdPort.execute(id));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getEvents() {
        return ResponseEntity.ok(getEventsPort.execute());
    }
}