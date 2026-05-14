package edu.eci.patricia.domain.exceptions;

public class EventDomainException extends RuntimeException {
    public EventDomainException(String message) {
        super(message);
    }
}
