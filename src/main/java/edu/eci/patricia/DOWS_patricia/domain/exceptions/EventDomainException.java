package edu.eci.patricia.DOWS_patricia.domain.exceptions;

public class EventDomainException extends RuntimeException {
    public EventDomainException(String message) {
        super(message);
    }
}
