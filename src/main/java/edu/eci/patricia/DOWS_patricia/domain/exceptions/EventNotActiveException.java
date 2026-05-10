package edu.eci.patricia.DOWS_patricia.domain.exceptions;

public class EventNotActiveException extends RuntimeException {
    public EventNotActiveException(String message) {
        super(message);
    }
}
