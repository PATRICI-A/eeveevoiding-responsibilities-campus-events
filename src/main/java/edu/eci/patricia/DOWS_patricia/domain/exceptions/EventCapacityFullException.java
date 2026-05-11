package edu.eci.patricia.DOWS_patricia.domain.exceptions;

public class EventCapacityFullException extends RuntimeException {
    public EventCapacityFullException(String message) {
        super(message);
    }
}
