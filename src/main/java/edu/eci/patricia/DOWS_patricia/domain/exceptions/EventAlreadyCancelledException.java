package edu.eci.patricia.DOWS_patricia.domain.exceptions;

public class EventAlreadyCancelledException extends RuntimeException {
    public EventAlreadyCancelledException(String message) {
        super(message);
    }
}
