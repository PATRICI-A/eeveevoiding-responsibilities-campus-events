package edu.eci.patricia.domain.exceptions;

public class RsvpNotFoundException extends RuntimeException {
    public RsvpNotFoundException(String message) {
        super(message);
    }
}
