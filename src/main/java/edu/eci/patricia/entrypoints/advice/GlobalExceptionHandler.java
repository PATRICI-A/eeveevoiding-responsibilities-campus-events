package edu.eci.patricia.entrypoints.advice;

import edu.eci.patricia.domain.exceptions.*;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Campus Events Service.
 * Translates domain exceptions into standardised HTTP error responses.
 *
 * <p><strong>Note:</strong> This handler is excluded from Swagger/OpenAPI documentation
 * via {@code @Hidden} because SpringDoc automatically documents the error responses
 * defined in the controller-level {@code @ApiResponses} annotations.</p>
 */
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link EventNotFoundException} when an event is not found by ID.
     *
     * @param ex the exception containing the error message
     * @return HTTP 404 Not Found with error details
     */
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEventNotFound(EventNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles {@link EventNotActiveException} when an operation is attempted on a non-active event.
     *
     * @param ex the exception containing the error message
     * @return HTTP 409 Conflict with error details
     */
    @ExceptionHandler(EventNotActiveException.class)
    public ResponseEntity<Map<String, Object>> handleEventNotActive(EventNotActiveException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Handles {@link EventCapacityFullException} when a student tries to RSVP to a full event.
     *
     * @param ex the exception containing the error message
     * @return HTTP 409 Conflict with error details
     */
    @ExceptionHandler(EventCapacityFullException.class)
    public ResponseEntity<Map<String, Object>> handleCapacityFull(EventCapacityFullException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Handles {@link UnauthorizedOrganizerException} when a non-organizer tries to modify an event.
     *
     * @param ex the exception containing the error message
     * @return HTTP 403 Forbidden with error details
     */
    @ExceptionHandler(UnauthorizedOrganizerException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedOrganizer(UnauthorizedOrganizerException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    /**
     * Handles {@link RsvpNotFoundException} when an RSVP is not found for a student/event pair.
     *
     * @param ex the exception containing the error message
     * @return HTTP 404 Not Found with error details
     */
    @ExceptionHandler(RsvpNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRsvpNotFound(RsvpNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles {@link RsvpAlreadyExistsException} when a student tries to RSVP twice to the same event.
     *
     * @param ex the exception containing the error message
     * @return HTTP 409 Conflict with error details
     */
    @ExceptionHandler(RsvpAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleRsvpAlreadyExists(RsvpAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Handles {@link EventDomainException} for general event business rule violations.
     *
     * @param ex the exception containing the error message
     * @return HTTP 400 Bad Request with error details
     */
    @ExceptionHandler(EventDomainException.class)
    public ResponseEntity<Map<String, Object>> handleEventDomain(EventDomainException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles bean validation errors ({@link MethodArgumentNotValidException}).
     * Returns a map of field names to validation error messages.
     *
     * @param ex the validation exception containing field errors
     * @return HTTP 400 Bad Request with field-specific error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("timestamp", LocalDateTime.now());
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("errors", ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(HashMap::new,
                        (map, e) -> map.put(e.getField(), e.getDefaultMessage()),
                        HashMap::putAll));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Catch-all handler for any unhandled exception. Provides a generic error message
     * to avoid exposing internal implementation details to API clients.
     *
     * @param ex the unexpected exception
     * @return HTTP 500 Internal Server Error with generic message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    /**
     * Builds a consistent JSON error response body.
     *
     * @param status  the HTTP status code to return
     * @param message the human-readable error message
     * @return a ResponseEntity containing the error body with the specified status
     */
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}