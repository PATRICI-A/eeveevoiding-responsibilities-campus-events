package edu.eci.patricia.domain.exceptions;

public class EventNotActiveException extends RuntimeException {
    public EventNotActiveException(String message) {
        super(message);
    }
}
