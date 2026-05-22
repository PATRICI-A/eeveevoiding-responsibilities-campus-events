package edu.eci.patricia.entrypoints.rest.controller;

import edu.eci.patricia.application.dto.request.EventRequestRsvp;
import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.domain.model.enums.RsvpAction;
import edu.eci.patricia.domain.ports.in.CancelRsvpPort;
import edu.eci.patricia.domain.ports.in.CreateRsvpPort;
import edu.eci.patricia.domain.ports.in.GetRsvpPort;
import edu.eci.patricia.domain.ports.out.EventRsvpRepositoryPort;
import edu.eci.patricia.application.mapper.EventRsvpMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventRsvpController {

    private final CreateRsvpPort createRsvpPort;
    private final CancelRsvpPort cancelRsvpPort;
    private final GetRsvpPort getRsvpPort;
    private final EventRsvpMapper rsvpMapper;
    private final EventRsvpRepositoryPort eventRsvpRepository;


    @PostMapping("/{eventId}/rsvp")
    public ResponseEntity<EventResponseRsvp> rsvp(
            @PathVariable UUID eventId,
            @Valid RsvpAction action,
            @RequestHeader("X-User-Id") UUID studentId) {

        if ( action == RsvpAction.CONFIRM) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createRsvpPort.execute(eventId, studentId));
        } else {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(cancelRsvpPort.execute(eventId, studentId));
        }
    }

    @GetMapping("/rsvp/agenda")
    public ResponseEntity<?> getAgenda(
            @RequestHeader("X-User-Id") UUID studentId) {
        List<EventFeedResponse> events = getRsvpPort.execute(studentId);
        if (events.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No events available at this time"));
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("/internal/rsvp-count/{userId}")
    public ResponseEntity<Integer> getUserRsvpCount(@PathVariable UUID userId) {
        return ResponseEntity.ok(getRsvpPort.execute(userId).size());
    }
}