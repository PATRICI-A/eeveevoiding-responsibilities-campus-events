package edu.eci.patricia.DOWS_patricia.domain.exceptions;

public class EventNotCancelableException extends RuntimeException {
    public EventNotCancelableException(String message) {
        super(message);
    }
}
