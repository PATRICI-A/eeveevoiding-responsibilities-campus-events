package edu.eci.patricia.entrypoints.advice;

import edu.eci.patricia.domain.exceptions.EventCapacityFullException;
import edu.eci.patricia.domain.exceptions.EventDomainException;
import edu.eci.patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.domain.exceptions.EventNotActiveException;
import edu.eci.patricia.domain.exceptions.RsvpAlreadyExistsException;
import edu.eci.patricia.domain.exceptions.RsvpNotFoundException;
import edu.eci.patricia.domain.exceptions.UnauthorizedOrganizerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEventNotFound(EventNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EventNotActiveException.class)
    public ResponseEntity<Map<String, Object>> handleEventNotActive(EventNotActiveException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(EventCapacityFullException.class)
    public ResponseEntity<Map<String, Object>> handleCapacityFull(EventCapacityFullException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedOrganizerException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedOrganizer(UnauthorizedOrganizerException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(RsvpNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRsvpNotFound(RsvpNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RsvpAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleRsvpAlreadyExists(RsvpAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(EventDomainException.class)
    public ResponseEntity<Map<String, Object>> handleEventDomain(EventDomainException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}