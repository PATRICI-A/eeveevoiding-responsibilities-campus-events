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

/**
 * REST controller for event management operations.
 * <p>
 * Provides endpoints for creating, reading, updating, and canceling university events.
 * All endpoints require JWT authentication with appropriate roles
 * (organizer for write operations, student/organizer for read operations).
 * </p>
 */
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(
        name = "Events",
        description = """
                Endpoints for managing university events. Allows organizers to create, update, and cancel events, \
                and provides a public feed for students to browse available events by category or date."""
)
@SecurityRequirement(name = "bearerAuth")
public class EventController {

    private final CreateEventPort createEventPort;
    private final GetEventsPort getEventsPort;
    private final UpdateEventPort updateEventPort;
    private final CancelEventPort cancelEventPort;
    private final GetEventByIdPort getEventByIdPort;

    /**
     * Creates a new university event.
     * <p>
     * Validates that the event date is in the future, duration is at least 15 minutes,
     * and the event name is unique. The organizer ID is extracted from the JWT token.
     * Upon successful creation, a QR code is generated for attendance tracking.
     * </p>
     *
     * @param request     the event creation payload
     * @param organizerId the organizer ID extracted from JWT token
     * @return the created event with generated ID, QR code, and metadata
     */
    @PostMapping
    @Operation(
            operationId = "createEvent",
            summary = "Create a new university event",
            description = """
                    Allows an authenticated organizer to create a new university event.
                    
                    **Validation rules:**
                    - Event date must be in the future (not today or earlier)
                    - Duration must be at least 15 minutes
                    - Name cannot exceed 100 characters
                    - Location cannot exceed 200 characters
                    - Maximum capacity (if provided) must be ≥ 1
                    
                    **QR Code:** Upon successful creation, a unique QR code is generated for attendance tracking.
                    
                    **Event status:** Newly created events have status `ACTIVE` by default.
                    
                    **Identity resolution:** The organizer ID is automatically extracted from the JWT `sub` claim.
                    
                    **Response:** Returns the created event with generated ID, QR code, and metadata."""
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Event created successfully. Returns full event details including ID, QR code, and ACTIVE status.",
                    content = @Content(schema = @Schema(implementation = EventResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error. One or more required fields are missing or violate constraints.",
                    content = @Content(schema = @Schema(example = "{\"status\": 400, \"timestamp\": \"2025-06-15T10:00:00\", \"errors\": {\"dateTime\": \"Event date must be in the future\"}}"))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. No valid JWT Bearer token was provided in the Authorization header.",
                    content = @Content(schema = @Schema(example = "{\"status\": 401, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Unauthorized\"}"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. The authenticated user does not have the ORGANIZADOR role.",
                    content = @Content(schema = @Schema(example = "{\"status\": 403, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Access denied\"}"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error. Retry the request or contact support.",
                    content = @Content(schema = @Schema(example = "{\"status\": 500, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"An unexpected error occurred\"}"))
            )
    })
    public ResponseEntity<EventResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Event creation payload. All fields are required unless marked optional.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EventRequest.class))
            )
            @Valid @RequestBody EventRequest request,
            @Parameter(
                    description = "Organizer ID extracted from JWT token (not required in request body)",
                    hidden = true
            )
            @AuthenticationPrincipal String organizerId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createEventPort.execute(request, UUID.fromString(organizerId)));
    }

    /**
     * Retrieves a paginated feed of active events with optional filters.
     * <p>
     * Supports filtering by category and date. Returns an empty message
     * instead of an empty list for better UX.
     * </p>
     *
     * @param filters the filter parameters (category and date, both optional)
     * @return a list of active events or a message if none are found
     */
    @GetMapping
    @Operation(
            operationId = "getEventFeed",
            summary = "Retrieve the event feed",
            description = """
                    Returns a list of all active university events available for students to browse.
                    
                    **Filters (all optional):**
                    - `category` — Filter by event category (ACADEMIC, CULTURAL, SPORTS, etc.)
                    - `date` — Filter by specific date (ISO-8601 format: yyyy-MM-dd)
                    
                    **Behaviour:**
                    - If no filters provided → returns all upcoming active events
                    - Date filter cannot be in the past
                    - If no events match filters → returns HTTP 200 with message: "No events available at this time"
                    
                    **Sorting:** Results are sorted by date (ascending) and then by creation time.
                    
                    **Use case:** Called to populate the main events screen in the mobile app or web UI."""
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = """
                            Events retrieved successfully. Returns a list of events (max 50 per request) \
                            or a message object if no events are found.""",
                    content = @Content(schema = @Schema(oneOf = {EventFeedResponse.class, Map.class}))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid filter parameters. Example: date filter is set to a past date.",
                    content = @Content(schema = @Schema(example = "{\"status\": 400, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Date filter cannot be in the past\"}"))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. No valid JWT Bearer token was provided.",
                    content = @Content(schema = @Schema(example = "{\"status\": 401, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Unauthorized\"}"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error. Retry the request or contact support.",
                    content = @Content(schema = @Schema(example = "{\"status\": 500, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"An unexpected error occurred\"}"))
            )
    })
    public ResponseEntity<?> getAll(
            @Parameter(
                    description = """
                            Optional filters for the event feed:
                            - `category`: Filter by event category (e.g., ACADEMIC, CULTURAL, SPORTS)
                            - `date`: Filter by specific date in ISO-8601 format (yyyy-MM-dd)""",
                    schema = @Schema(implementation = EventFeedRequest.class)
            )
            @Valid EventFeedRequest filters) {
        List<EventFeedResponse> events = getEventsPort.execute(filters.getCategory(), filters.getDate());
        if (events.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No events available at this time"));
        }
        return ResponseEntity.ok(events);
    }

    /**
     * Updates an existing event.
     * <p>
     * Only the original organizer can update the event. All fields are required
     * (no partial updates). When updated, all confirmed RSVP holders are notified
     * about the change via the messaging system.
     * </p>
     *
     * @param eventId     the UUID of the event to update
     * @param request     the event update payload
     * @param organizerId the organizer ID extracted from JWT token
     * @return the updated event details
     */
    @PutMapping("/{eventId}")
    @Operation(
            operationId = "updateEvent",
            summary = "Update an existing event",
            description = """
                    Allows the original organizer of an event to update its details.
                    
                    **Authorization:** Only the organizer who created the event can update it. \
                    The organizer ID is extracted from the JWT token and validated against the event's owner.
                    
                    **Validation:** All fields in the request body are required and subject to the same \
                    validation rules as creation (future date, min duration 15 min, etc.).
                    
                    **Partial updates:** Currently requires all fields — updates are not partial.
                    
                    **Response:** Returns the updated event details including any changes to QR code \
                    (regenerated if date/time/location changed).
                    
                    **Idempotency:** Updating the same event multiple times with the same data has no side effects."""
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Event updated successfully. Returns the full updated event details.",
                    content = @Content(schema = @Schema(implementation = EventResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error in the request body. Check field constraints.",
                    content = @Content(schema = @Schema(example = "{\"status\": 400, \"timestamp\": \"2025-06-15T10:00:00\", \"errors\": {\"durationMinutes\": \"Duration must be at least 15 minutes\"}}"))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. No valid JWT Bearer token provided.",
                    content = @Content(schema = @Schema(example = "{\"status\": 401, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Unauthorized\"}"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. The authenticated user is not the organizer of this event.",
                    content = @Content(schema = @Schema(example = "{\"status\": 403, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"You are not authorized to modify this event\"}"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found. No event exists with the provided eventId.",
                    content = @Content(schema = @Schema(example = "{\"status\": 404, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Event not found\"}"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error.",
                    content = @Content(schema = @Schema(example = "{\"status\": 500, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"An unexpected error occurred\"}"))
            )
    })
    public ResponseEntity<EventResponse> update(
            @Parameter(
                    description = "UUID of the event to update",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID eventId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Event update payload. All fields are required (no partial updates).",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EventUpdateRequest.class))
            )
            @Valid @RequestBody EventUpdateRequest request,
            @Parameter(
                    description = "Organizer ID extracted from JWT token (used for ownership validation)",
                    hidden = true
            )
            @AuthenticationPrincipal String organizerId) {
        return ResponseEntity.ok(updateEventPort.execute(eventId, request, UUID.fromString(organizerId)));
    }

    /**
     * Retrieves a single event by its ID.
     * <p>
     * Returns event details for any event status. This endpoint is accessible
     * to both students and organizers.
     * </p>
     *
     * @param eventId the UUID of the event to retrieve
     * @return the event feed response DTO
     */
    @GetMapping("/{eventId}")
    @Operation(
            operationId = "getEventById",
            summary = "Get event details by ID",
            description = """
                    Retrieves the full details of a specific university event identified by its UUID.
                    
                    **Use cases:**
                    - Viewing event details before confirming an RSVP
                    - Displaying event information in a detail screen
                    - Sharing event information with other students
                    
                    **Returns:** All available fields including capacity, status, QR code, and schedule information.
                    
                    **Note:** Only active events are visible in the feed, but this endpoint returns events \
                    in any status for reference purposes."""
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Event found and returned successfully.",
                    content = @Content(schema = @Schema(implementation = EventFeedResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. No valid JWT Bearer token provided.",
                    content = @Content(schema = @Schema(example = "{\"status\": 401, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Unauthorized\"}"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found. No event exists with the provided UUID.",
                    content = @Content(schema = @Schema(example = "{\"status\": 404, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Event not found\"}"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error.",
                    content = @Content(schema = @Schema(example = "{\"status\": 500, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"An unexpected error occurred\"}"))
            )
    })
    public ResponseEntity<EventFeedResponse> getById(
            @Parameter(
                    description = "UUID of the event to retrieve",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID eventId) {
        return ResponseEntity.ok(getEventByIdPort.execute(eventId));
    }

    /**
     * Cancels an active event.
     * <p>
     * Only the original organizer can cancel the event. Once cancelled,
     * the event is no longer visible in the public feed and no new RSVPs
     * can be made. All confirmed RSVP holders are notified.
     * </p>
     *
     * @param eventId     the UUID of the event to cancel
     * @param organizerId the organizer ID extracted from JWT token
     * @return HTTP 204 No Content on success
     */
    @PatchMapping("/{eventId}")
    @Operation(
            operationId = "cancelEvent",
            summary = "Cancel an event",
            description = """
                    Allows the original organizer of an event to cancel it permanently.
                    
                    **Effects of cancellation:**
                    - Event is marked as CANCELLED (status change, not physical deletion)
                    - Event is no longer visible in the public feed
                    - Students cannot RSVP to the event
                    - Existing RSVPs are preserved for audit purposes
                    
                    **Authorization:** Only the organizer who created the event can cancel it.
                    
                    **Irreversibility:** This operation cannot be undone. Consider creating a new event \
                    instead of trying to reactivate a cancelled one.
                    
                    **Response:** Returns HTTP 204 No Content on success — no response body."""
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Event cancelled successfully. No content is returned."
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. No valid JWT Bearer token provided.",
                    content = @Content(schema = @Schema(example = "{\"status\": 401, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Unauthorized\"}"))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. The authenticated user is not the organizer of this event.",
                    content = @Content(schema = @Schema(example = "{\"status\": 403, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"You are not authorized to cancel this event\"}"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Event not found. No event exists with the provided UUID.",
                    content = @Content(schema = @Schema(example = "{\"status\": 404, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Event not found\"}"))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict. The event is already cancelled or in a state that does not allow cancellation.",
                    content = @Content(schema = @Schema(example = "{\"status\": 409, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"Event is not in an active state\"}"))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error.",
                    content = @Content(schema = @Schema(example = "{\"status\": 500, \"timestamp\": \"2025-06-15T10:00:00\", \"message\": \"An unexpected error occurred\"}"))
            )
    })
    public ResponseEntity<Void> cancel(
            @Parameter(
                    description = "UUID of the event to cancel",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID eventId,
            @Parameter(
                    description = "Organizer ID extracted from JWT token (used for ownership validation)",
                    hidden = true
            )
            @AuthenticationPrincipal String organizerId) {
        cancelEventPort.execute(eventId, UUID.fromString(organizerId));
        return ResponseEntity.noContent().build();
    }
}