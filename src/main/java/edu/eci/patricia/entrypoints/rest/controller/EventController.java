package edu.eci.patricia.entrypoints.rest.controller;

import edu.eci.patricia.application.dto.request.EventFeedRequest;
import edu.eci.patricia.application.dto.request.EventRequest;
import edu.eci.patricia.application.dto.request.EventUpdateRequest;
import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.dto.response.EventResponse;
import edu.eci.patricia.domain.ports.in.*;
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
        name = "Events",
        description = "Endpoints for managing university events. Allows organizers to create, update, and cancel events, " +
                "and provides a public feed for students to browse available events by category or date."
)
@SecurityRequirement(name = "bearerAuth")
public class EventController {

    private final CreateEventPort createEventPort;
    private final GetEventsPort getEventsPort;
    private final UpdateEventPort updateEventPort;
    private final CancelEventPort cancelEventPort;
    private final GetEventByIdPort getEventByIdPort;

    @PostMapping
    @Operation(
            summary = "Create a new university event",
            description = "Allows an authenticated organizer to create a new university event. " +
                    "The organizer's identity is automatically extracted from the JWT token — no manual header is required. " +
                    "The request must include all required fields such as event name, date (must be in the future), " +
                    "start time, duration (minimum 15 minutes), location, category, and type. " +
                    "Optionally, a maximum capacity can be set to limit attendance. " +
                    "On success, returns the created event with its generated ID, QR code, and initial status."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Event created successfully. Returns the full event details including ID, QR code, and ACTIVE status.",
                    content = @Content(schema = @Schema(implementation = EventResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error. One or more required fields are missing, malformed, or violate constraints " +
                            "(e.g. date in the past, name too long, duration below 15 minutes).",
                    content = @Content(schema = @Schema(example = "{\"status\": 400, \"errors\": {\"dateTime\": \"Event date must be in the future\"}}"))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. The request does not include a valid JWT Bearer token.",
                    content = @Content(schema = @Schema(example = "{\"status\": 401, \"message\": \"Unauthorized\"}"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error. Please contact the system administrator.",
                    content = @Content(schema = @Schema(example = "{\"status\": 500, \"message\": \"An unexpected error occurred\"}"))
            )
    })
    public ResponseEntity<EventResponse> create(
            @Valid @RequestBody EventRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal String organizerId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createEventPort.execute(request, UUID.fromString(organizerId)));
    }

    @GetMapping
    @Operation(
            summary = "Retrieve the event feed",
            description = "Returns a list of all active university events available for students to browse. " +
                    "Supports optional filtering by event category (e.g. ACADEMIC, CULTURAL, SPORTS) and/or a specific date. " +
                    "If no filters are provided, all upcoming active events are returned. " +
                    "The date filter must not be in the past. " +
                    "If no events match the filters, a descriptive message is returned instead of an empty array."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of events returned successfully. May include a message object if no events are found.",
                    content = @Content(schema = @Schema(implementation = EventFeedResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid filter parameters. For example, the date filter is set to a past date.",
                    content = @Content(schema = @Schema(example = "{\"status\": 400, \"errors\": {\"date\": \"Date filter cannot be in the past\"}}"))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. No valid JWT token was provided.",
                    content = @Content(schema = @Schema(example = "{\"status\": 401, \"message\": \"Unauthorized\"}"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error.",
                    content = @Content(schema = @Schema(example = "{\"status\": 500, \"message\": \"An unexpected error occurred\"}"))
            )
    })
    public ResponseEntity<?> getAll(@Valid EventFeedRequest filters) {
        List<EventFeedResponse> events = getEventsPort.execute(filters.getCategory(), filters.getDate());
        if (events.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No events available at this time"));
        }
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{eventId}")
    @Operation(
            summary = "Update an existing event",
            description = "Allows the original organizer of an event to update its details. " +
                    "The organizer's identity is extracted automatically from the JWT token and validated against the event's owner. " +
                    "All fields in the request body are required and subject to the same validation rules as creation. " +
                    "Only the organizer who originally created the event is authorized to perform this operation. " +
                    "Returns the updated event details on success."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Event updated successfully. Returns the full updated event details.",
                    content = @Content(schema = @Schema(implementation = EventResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error in the request body. Check field constraints such as future date, name length, or minimum duration.",
                    content = @Content(schema = @Schema(example = "{\"status\": 400, \"errors\": {\"duration\": \"Duration must be at least 15 minutes\"}}"))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. No valid JWT token provided.",
                    content = @Content(schema = @Schema(example = "{\"status\": 401, \"message\": \"Unauthorized\"}"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. The authenticated user is not the organizer of this event and cannot modify it.",
                    content = @Content(schema = @Schema(example = "{\"status\": 403, \"message\": \"You are not authorized to modify this event\"}"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found. No event exists with the provided eventId.",
                    content = @Content(schema = @Schema(example = "{\"status\": 404, \"message\": \"Event not found\"}"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error.",
                    content = @Content(schema = @Schema(example = "{\"status\": 500, \"message\": \"An unexpected error occurred\"}"))
            )
    })
    public ResponseEntity<EventResponse> update(
            @Parameter(description = "UUID of the event to update", required = true)
            @PathVariable UUID eventId,
            @Valid @RequestBody EventUpdateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal String organizerId) {
        return ResponseEntity.ok(updateEventPort.execute(eventId, request, UUID.fromString(organizerId)));
    }

    @GetMapping("/{eventId}")
    @Operation(
            summary = "Get event details by ID",
            description = "Retrieves the full details of a specific university event identified by its UUID. " +
                    "This endpoint is useful for viewing event details before confirming an RSVP, " +
                    "or for displaying event information in a detail view. " +
                    "Returns all available fields including capacity, status, QR code, and schedule information."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Event found and returned successfully.",
                    content = @Content(schema = @Schema(implementation = EventFeedResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. No valid JWT token provided.",
                    content = @Content(schema = @Schema(example = "{\"status\": 401, \"message\": \"Unauthorized\"}"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found. No event exists with the provided UUID.",
                    content = @Content(schema = @Schema(example = "{\"status\": 404, \"message\": \"Event not found\"}"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error.",
                    content = @Content(schema = @Schema(example = "{\"status\": 500, \"message\": \"An unexpected error occurred\"}"))
            )
    })
    public ResponseEntity<EventFeedResponse> getById(
            @Parameter(description = "UUID of the event to retrieve", required = true)
            @PathVariable UUID eventId) {
        return ResponseEntity.ok(getEventByIdPort.execute(eventId));
    }

    @PatchMapping("/{eventId}")
    @Operation(
            summary = "Cancel an event",
            description = "Allows the original organizer of an event to cancel it. " +
                    "Once cancelled, the event will no longer be visible in the public feed and students will not be able to RSVP. " +
                    "The organizer's identity is extracted from the JWT token and validated to ensure only the event owner can perform this action. " +
                    "This operation is irreversible. Returns no content on success (HTTP 204)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Event cancelled successfully. No content is returned."
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. No valid JWT token provided.",
                    content = @Content(schema = @Schema(example = "{\"status\": 401, \"message\": \"Unauthorized\"}"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. The authenticated user is not the organizer of this event.",
                    content = @Content(schema = @Schema(example = "{\"status\": 403, \"message\": \"You are not authorized to cancel this event\"}"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found. No event exists with the provided UUID.",
                    content = @Content(schema = @Schema(example = "{\"status\": 404, \"message\": \"Event not found\"}"))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict. The event is already cancelled or in a state that does not allow cancellation.",
                    content = @Content(schema = @Schema(example = "{\"status\": 409, \"message\": \"Event is not in an active state\"}"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error.",
                    content = @Content(schema = @Schema(example = "{\"status\": 500, \"message\": \"An unexpected error occurred\"}"))
            )
    })
    public ResponseEntity<Void> cancel(
            @Parameter(description = "UUID of the event to cancel", required = true)
            @PathVariable UUID eventId,
            @Parameter(hidden = true) @AuthenticationPrincipal String organizerId) {
        cancelEventPort.execute(eventId, UUID.fromString(organizerId));
        return ResponseEntity.noContent().build();
    }
}
