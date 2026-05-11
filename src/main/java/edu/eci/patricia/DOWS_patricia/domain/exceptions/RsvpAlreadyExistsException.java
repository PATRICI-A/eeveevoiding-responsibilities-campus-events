package edu.eci.patricia.DOWS_patricia.domain.exceptions;

public class RsvpAlreadyExistsException extends RuntimeException {
    public RsvpAlreadyExistsException(String message) {
        super(message);
    }
}
