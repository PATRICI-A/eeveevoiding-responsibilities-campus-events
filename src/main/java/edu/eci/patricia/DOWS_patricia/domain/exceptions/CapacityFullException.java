package edu.eci.patricia.DOWS_patricia.domain.exceptions;

public class CapacityFullException extends RuntimeException {
    public CapacityFullException(String message) {
        super(message);
    }
}
