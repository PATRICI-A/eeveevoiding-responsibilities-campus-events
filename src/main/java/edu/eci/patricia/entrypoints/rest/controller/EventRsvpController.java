package edu.eci.patricia.entrypoints.rest.controller;

import edu.eci.patricia.application.dto.request.EventRequestRsvp;
import edu.eci.patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.domain.model.enums.RsvpAction;
import edu.eci.patricia.domain.ports.in.CancelRsvpPort;
import edu.eci.patricia.domain.ports.in.CreateRsvpPort;
import edu.eci.patricia.domain.ports.out.EventRsvpRepositoryPort;
import edu.eci.patricia.application.mapper.EventRsvpMapper;
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
public class EventRsvpController {

    private final CreateRsvpPort createRsvpPort;
    private final CancelRsvpPort cancelRsvpPort;
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

    // RF22 — Get personal agenda (confirmed RSVPs)
    @GetMapping("/rsvp/agenda")
    public ResponseEntity<List<EventResponseRsvp>> getAgenda(
            @RequestHeader("X-User-Id") UUID studentId) {
        return ResponseEntity.ok(
                eventRsvpRepository.findConfirmedByStudentId(studentId)
                        .stream()
                        .map(rsvpMapper::toDTO)
                        .toList()
        );
    }
}