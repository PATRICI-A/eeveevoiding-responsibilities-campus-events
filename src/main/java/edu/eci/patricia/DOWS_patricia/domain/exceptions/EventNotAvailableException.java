package edu.eci.patricia.DOWS_patricia.domain.exceptions;

// si intentas hacer una reserva en un evento que no tenga cupo o este cancelado

public class EventNotAvailableException extends RuntimeException {
    public EventNotAvailableException(String id) {
        super("El evento con ID " + id + " no está disponible");
    }
}