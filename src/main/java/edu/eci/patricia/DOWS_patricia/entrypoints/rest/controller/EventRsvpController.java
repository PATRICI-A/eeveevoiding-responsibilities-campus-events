package edu.eci.patricia.DOWS_patricia.infrastructure.entrypoints.rest.controller;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequestRsvp;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpAction;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CancelRsvpPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.in.CreateRsvpPort;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRsvpRepositoryPort;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventRsvpMapper;
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
    private final EventRsvpRepositoryPort rsvpRepository;
    private final EventRsvpMapper rsvpMapper;
    private final EventRsvpRepositoryPort eventRsvpRepository;

    // RF22 — Confirm or cancel attendance
    @PostMapping("/{eventId}/rsvp")
    public ResponseEntity<EventResponseRsvp> rsvp(
            @PathVariable UUID eventId,
            @Valid @RequestBody EventRequestRsvp request,
            @RequestHeader("X-User-Id") UUID studentId) {
        if (request.getAction() == RsvpAction.CONFIRM) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createRsvpPort.execute(eventId, studentId));
        } else {
            cancelRsvpPort.execute(eventId, studentId);
            return ResponseEntity.noContent().build();
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