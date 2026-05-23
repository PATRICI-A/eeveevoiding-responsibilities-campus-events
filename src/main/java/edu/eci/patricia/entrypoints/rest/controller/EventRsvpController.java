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

/**
 * REST controller for managing student RSVPs (event attendance registrations).
 * <p>
 * Provides endpoints for students to confirm or cancel their attendance to events,
 * and to retrieve their personal agenda of confirmed events.
 * All endpoints require JWT authentication with the ESTUDIANTE role.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(
        name = "Event RSVPs",
        description = """
                Endpoints for managing student RSVPs to university events. Students can confirm or cancel \
                their attendance to active events, and retrieve their personal event agenda based on confirmed RSVPs."""
)
@SecurityRequirement(name = "bearerAuth")
public class EventRsvpController {

    private final CreateRsvpPort createRsvpPort;
    private final CancelRsvpPort cancelRsvpPort;
    private final GetRsvpPort getRsvpPort;
    private final EventRsvpMapper rsvpMapper;
    private final EventRsvpRepositoryPort eventRsvpRepository;

    /**
     * Confirms or cancels a student's attendance to an event.
     * <p>
     * Supports two actions:
     * <ul>
     *   <li>CONFIRM - Registers the student for the event (subject to capacity and status)</li>
     *   <li>CANCEL - Withdraws a previously confirmed attendance</li>
     * </ul>
     * The student ID is extracted from the JWT token.
     * </p>
     *
     * @param eventId   the UUID of the event
     * @param action    the action to perform (CONFIRM or CANCEL)
     * @param studentId the student ID extracted from JWT token
     * @return the RSVP record with updated status
     */
    @PostMapping("/{eventId}/rsvp")
    @Operation(
            operationId = "manageRsvp",
            summary = "Confirm or cancel attendance to an event",
            description = """
                    Allows an authenticated student to register or cancel their attendance to a specific university event.
                    
                    **Actions:**
                    - `CONFIRM` — Register attendance to the event
                    - `CANCEL` — Withdraw previously confirmed attendance
                    
                    **Validation rules for CONFIRM:**
                    - Event must exist and be in ACTIVE status
                    - Event must not have reached its maximum capacity
                    - Student cannot have an existing CONFIRMED RSVP for this event
                    
                    **Validation rules for CANCEL:**
                    - Student must have an existing CONFIRMED RSVP for this event
                    
                    **Response:** Returns the RSVP record with its ID, event ID, student ID, and current status.
                    
                    **HTTP Status:** Always 201 Created for both CONFIRM and CANCEL operations.
                    
                    **Identity resolution:** Student ID is automatically extracted from the JWT `sub` claim."""
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = """
                            RSVP operation completed successfully.
                            - For CONFIRM: Returns RSVP with status CONFIRMED
                            - For CANCEL: Returns RSVP with status CANCELLED""",
                    content = @Content(schema = @Schema(implementation = EventResponseRsvp.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = """
                            Invalid request. Possible causes:
                            - Missing or invalid `action` parameter (must be CONFIRM or CANCEL)
                            - Malformed UUID in path parameter""",
                    content = @Content(schema = @Schema(example = "{\"status\": 400, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Action is required\"}"))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. No valid JWT Bearer token was provided in the request.",
                    content = @Content(schema = @Schema(example = "{\"status\": 401, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Unauthorized\"}"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. The authenticated user does not have the ESTUDIANTE role.",
                    content = @Content(schema = @Schema(example = "{\"status\": 403, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Access denied\"}"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found. No event exists with the provided eventId.",
                    content = @Content(schema = @Schema(example = "{\"status\": 404, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Event not found\"}"))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = """
                            Conflict. Possible causes:
                            - Event is not active (status != ACTIVE)
                            - Event has reached maximum capacity (for CONFIRM)
                            - Student already has a confirmed RSVP for this event (for CONFIRM)
                            - Student has no RSVP to cancel (for CANCEL)""",
                    content = @Content(schema = @Schema(example = "{\"status\": 409, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Event capacity is full\"}"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error. Retry the request or contact support.",
                    content = @Content(schema = @Schema(example = "{\"status\": 500, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"An unexpected error occurred\"}"))
            )
    })
    public ResponseEntity<EventResponseRsvp> rsvp(
            @Parameter(
                    description = "UUID of the event the student wants to attend or withdraw from",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID eventId,
            @Parameter(
                    description = """
                            Action to perform on the RSVP:
                            - `CONFIRM` — Register attendance (subject to capacity and event status validation)
                            - `CANCEL` — Withdraw previously confirmed attendance""",
                    required = true,
                    schema = @Schema(implementation = RsvpAction.class)
            )
            @Valid RsvpAction action,
            @Parameter(
                    description = "Student ID extracted from JWT token (not required in request body)",
                    hidden = true
            )
            @AuthenticationPrincipal String studentId) {

        if (action == RsvpAction.CONFIRM) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createRsvpPort.execute(eventId, UUID.fromString(studentId)));
        } else {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(cancelRsvpPort.execute(eventId, UUID.fromString(studentId)));
        }
    }

    /**
     * Retrieves the authenticated student's personal event agenda.
     * <p>
     * Returns all events for which the student has a confirmed RSVP
     * and that are still active. Acts as the "My Events" view in the UI.
     * </p>
     *
     * @param studentId the student ID extracted from JWT token
     * @return a list of events the student is attending, or a message if empty
     */
    @GetMapping("/rsvp/agenda")
    @Operation(
            operationId = "getStudentAgenda",
            summary = "Get the student's personal event agenda",
            description = """
                    Returns the list of university events for which the authenticated student has a confirmed RSVP.
                    
                    **Behaviour:**
                    - Acts as the student's personal agenda — shows all upcoming events they plan to attend
                    - Student identity is automatically extracted from the JWT token
                    - Results are sorted by event date (ascending) and then by creation time
                    
                    **Empty agenda handling:**
                    If the student has no confirmed RSVPs, returns HTTP 200 with message:
                    `{"message": "No events available at this time"}`
                    
                    **Use case:** Called on app startup to populate the "My Events" tab in the student dashboard.
                    
                    **Note:** Only events with status ACTIVE and future dates are included in the agenda."""
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = """
                            Agenda retrieved successfully. Returns a list of events or a message if the agenda is empty.
                            - With events: Returns array of EventFeedResponse objects
                            - Without events: Returns `{"message": "No events available at this time"}`""",
                    content = @Content(schema = @Schema(oneOf = {EventFeedResponse.class, Map.class}))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. No valid JWT Bearer token was provided.",
                    content = @Content(schema = @Schema(example = "{\"status\": 401, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Unauthorized\"}"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. The authenticated user does not have the ESTUDIANTE role.",
                    content = @Content(schema = @Schema(example = "{\"status\": 403, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Access denied\"}"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error. Retry the request or contact support.",
                    content = @Content(schema = @Schema(example = "{\"status\": 500, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"An unexpected error occurred\"}"))
            )
    })
    public ResponseEntity<?> getAgenda(
            @Parameter(
                    description = "Student ID extracted from JWT token (not required as query parameter)",
                    hidden = true
            )
            @AuthenticationPrincipal String studentId) {
        List<EventFeedResponse> events = getRsvpPort.execute(UUID.fromString(studentId));
        if (events.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No events available at this time"));
        }
        return ResponseEntity.ok(events);
    }
}