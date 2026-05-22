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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(
        name = "Event RSVPs",
        description = "Endpoints for managing student RSVPs to university events. " +
                "Students can confirm or cancel their attendance to active events, " +
                "and retrieve their personal event agenda based on confirmed RSVPs."
)
@SecurityRequirement(name = "bearerAuth")
public class EventRsvpController {

    private final CreateRsvpPort createRsvpPort;
    private final CancelRsvpPort cancelRsvpPort;
    private final GetRsvpPort getRsvpPort;
    private final EventRsvpMapper rsvpMapper;
    private final EventRsvpRepositoryPort eventRsvpRepository;

    @PostMapping("/{eventId}/rsvp")
    @Operation(
            summary = "Confirm or cancel attendance to an event",
            description = "Allows an authenticated student to register or cancel their attendance to a specific university event. " +
                    "The action is controlled by the 'action' query parameter: use CONFIRM to register attendance, or CANCEL to withdraw it. " +
                    "The student's identity is automatically extracted from the JWT token. " +
                    "Important validations: the event must exist and be in ACTIVE status, " +
                    "the event must not have reached its maximum capacity (for CONFIRM), " +
                    "and a student cannot confirm attendance to the same event twice. " +
                    "Returns the RSVP record with its ID, event ID, student ID, and current status."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "RSVP registered or cancelled successfully. Returns the RSVP record with status CONFIRMED or CANCELLED.",
                    content = @Content(schema = @Schema(implementation = EventResponseRsvp.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request. The 'action' parameter is missing or not a valid RsvpAction value (CONFIRM or CANCEL).",
                    content = @Content(schema = @Schema(example = "{\"status\": 400, \"message\": \"Action is required\"}"))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. No valid JWT Bearer token was provided in the request.",
                    content = @Content(schema = @Schema(example = "{\"status\": 401, \"message\": \"Unauthorized\"}"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found. No event exists with the provided eventId.",
                    content = @Content(schema = @Schema(example = "{\"status\": 404, \"message\": \"Event not found\"}"))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict. Possible causes: the event is not active, the event has reached maximum capacity, " +
                            "or the student already has a confirmed RSVP for this event.",
                    content = @Content(schema = @Schema(example = "{\"status\": 409, \"message\": \"Event capacity is full\"}"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error.",
                    content = @Content(schema = @Schema(example = "{\"status\": 500, \"message\": \"An unexpected error occurred\"}"))
            )
    })
    public ResponseEntity<EventResponseRsvp> rsvp(
            @Parameter(description = "UUID of the event the student wants to attend or withdraw from", required = true)
            @PathVariable UUID eventId,
            @Parameter(description = "Action to perform: CONFIRM to register attendance, CANCEL to withdraw it", required = true)
            @Valid RsvpAction action,
            @Parameter(hidden = true) @AuthenticationPrincipal String studentId) {

        if (action == RsvpAction.CONFIRM) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createRsvpPort.execute(eventId, UUID.fromString(studentId)));
        } else {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(cancelRsvpPort.execute(eventId, UUID.fromString(studentId)));
        }
    }

    @GetMapping("/rsvp/agenda")
    @Operation(
            summary = "Get the student's personal event agenda",
            description = "Returns the list of university events for which the authenticated student has a confirmed RSVP. " +
                    "This endpoint acts as the student's personal agenda, showing all upcoming events they plan to attend. " +
                    "The student's identity is automatically extracted from the JWT token — no manual header is required. " +
                    "If the student has no confirmed RSVPs, a descriptive message is returned instead of an empty list. " +
                    "Each event in the response includes full details such as date, time, location, category, and available capacity."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agenda retrieved successfully. Returns a list of events or a message if the agenda is empty.",
                    content = @Content(schema = @Schema(implementation = EventFeedResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. No valid JWT Bearer token was provided.",
                    content = @Content(schema = @Schema(example = "{\"status\": 401, \"message\": \"Unauthorized\"}"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No RSVPs found for the authenticated student.",
                    content = @Content(schema = @Schema(example = "{\"status\": 404, \"message\": \"No RSVPs found for this student\"}"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error.",
                    content = @Content(schema = @Schema(example = "{\"status\": 500, \"message\": \"An unexpected error occurred\"}"))
            )
    })
    public ResponseEntity<?> getAgenda(
            @Parameter(hidden = true) @AuthenticationPrincipal String studentId) {
        List<EventFeedResponse> events = getRsvpPort.execute(UUID.fromString(studentId));
        if (events.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No events available at this time"));
        }
        return ResponseEntity.ok(events);
    }
}
