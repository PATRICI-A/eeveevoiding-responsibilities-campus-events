package edu.eci.patricia.DOWS_patricia.domain.exceptions;

// Si los datos del evento no son validos

public class InvalidEventException extends RuntimeException {
    public InvalidEventException(String message) {
        super(message);
    }
}
