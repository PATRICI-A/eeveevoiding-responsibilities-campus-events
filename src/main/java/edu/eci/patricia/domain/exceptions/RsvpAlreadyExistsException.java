package edu.eci.patricia.domain.exceptions;

public class RsvpAlreadyExistsException extends RuntimeException {
    public RsvpAlreadyExistsException(String message) {
        super(message);
    }
}
