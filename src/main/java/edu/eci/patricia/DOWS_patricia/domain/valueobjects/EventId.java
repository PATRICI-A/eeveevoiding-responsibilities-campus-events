package edu.eci.patricia.DOWS_patricia.domain.valueobjects;


import edu.eci.patricia.DOWS_patricia.domain.exceptions.InvalidEventException;

import java.util.UUID;

public final class EventId {
    private final String value;

    public EventId(String value) {
        if (value == null || value.isBlank())
            throw new InvalidEventException("EventId cannot be empty");
        this.value = value;
    }

    public static EventId generate() {
        return new EventId(UUID.randomUUID().toString());
    }

    public String getValue() { return value; }
}