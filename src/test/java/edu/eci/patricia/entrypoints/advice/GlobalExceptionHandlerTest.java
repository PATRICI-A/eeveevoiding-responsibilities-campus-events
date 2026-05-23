package edu.eci.patricia.entrypoints.advice;

import edu.eci.patricia.domain.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    private ResponseEntity<Map<String, Object>> response;
    private Map<String, Object> body;

    @Test
    void handleEventNotFoundShouldReturnNotFoundStatus() {
        String message = "Event not found with id: 123";
        EventNotFoundException exception = new EventNotFoundException(message);

        response = handler.handleEventNotFound(exception);
        body = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(body);
        assertEquals(404, body.get("status"));
        assertEquals(message, body.get("message"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void handleEventNotActiveShouldReturnConflictStatus() {
        String message = "Event is not active";
        EventNotActiveException exception = new EventNotActiveException(message);

        response = handler.handleEventNotActive(exception);
        body = response.getBody();

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(body);
        assertEquals(409, body.get("status"));
        assertEquals(message, body.get("message"));
    }

    @Test
    void handleEventCapacityFullShouldReturnConflictStatus() {
        String message = "Event capacity is full";
        EventCapacityFullException exception = new EventCapacityFullException(message);

        response = handler.handleCapacityFull(exception);
        body = response.getBody();

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(body);
        assertEquals(409, body.get("status"));
        assertEquals(message, body.get("message"));
    }


    @Test
    void handleRsvpNotFoundShouldReturnNotFoundStatus() {
        String message = "RSVP not found for the user";
        RsvpNotFoundException exception = new RsvpNotFoundException(message);

        response = handler.handleRsvpNotFound(exception);
        body = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(body);
        assertEquals(404, body.get("status"));
        assertEquals(message, body.get("message"));
    }

    @Test
    void handleRsvpAlreadyExistsShouldReturnConflictStatus() {
        String message = "RSVP already exists for this event";
        RsvpAlreadyExistsException exception = new RsvpAlreadyExistsException(message);

        response = handler.handleRsvpAlreadyExists(exception);
        body = response.getBody();

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(body);
        assertEquals(409, body.get("status"));
        assertEquals(message, body.get("message"));
    }

    @Test
    void handleEventDomainShouldReturnBadRequestStatus() {
        String message = "Domain validation error";
        EventDomainException exception = new EventDomainException(message);

        response = handler.handleEventDomain(exception);
        body = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
        assertEquals(400, body.get("status"));
        assertEquals(message, body.get("message"));
    }

    @Test
    void handleValidationShouldReturnBadRequestWithErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError1 = new FieldError("object", "name", "Name is required");
        FieldError fieldError2 = new FieldError("object", "email", "Email is invalid");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        response = handler.handleValidation(exception);
        body = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
        assertEquals(400, body.get("status"));
        assertNotNull(body.get("errors"));

        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) body.get("errors");
        assertEquals(2, errors.size());
        assertEquals("Name is required", errors.get("name"));
        assertEquals("Email is invalid", errors.get("email"));
    }

    @Test
    void handleValidationShouldHandleEmptyErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        response = handler.handleValidation(exception);
        body = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) body.get("errors");
        assertTrue(errors.isEmpty());
    }

    @Test
    void handleGenericShouldReturnInternalServerError() {
        Exception exception = new RuntimeException("Unexpected error");

        response = handler.handleGeneric(exception);
        body = response.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(body);
        assertEquals(500, body.get("status"));
        assertEquals("An unexpected error occurred", body.get("message"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void buildResponseShouldCreateProperErrorStructure() {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "Test error message";

        response = handler.handleEventDomain(new EventDomainException(message));
        body = response.getBody();

        assertEquals(status, response.getStatusCode());
        assertEquals(message, body.get("message"));
        assertEquals(400, body.get("status"));
        assertTrue(body.containsKey("timestamp"));
    }
}