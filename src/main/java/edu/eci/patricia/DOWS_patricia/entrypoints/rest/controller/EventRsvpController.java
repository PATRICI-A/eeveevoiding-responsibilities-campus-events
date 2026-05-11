package edu.eci.patricia.DOWS_patricia.entrypoints.rest.controller;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CancelRsvpPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CreateRsvpPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rsvp")
@RequiredArgsConstructor
public class EventRsvpController {

    private final CreateRsvpPort createRsvpPort;
    private final CancelRsvpPort cancelRsvpPort;

    @PostMapping
    public ResponseEntity<EventResponseRsvp> createRsvp(@RequestBody EventRequestRsvp request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createRsvpPort.execute(request));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<EventResponseRsvp> cancelRsvp(@PathVariable String id) {
        return ResponseEntity.ok(cancelRsvpPort.execute(id));
    }
}
