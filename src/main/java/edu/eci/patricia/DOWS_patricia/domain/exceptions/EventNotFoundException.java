package edu.eci.patricia.DOWS_patricia.domain.exceptions;

// Cuando no encontramos un evento por el ID

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String id) {
        super("Evento no encontrado por el ID: " + id);
    }
}