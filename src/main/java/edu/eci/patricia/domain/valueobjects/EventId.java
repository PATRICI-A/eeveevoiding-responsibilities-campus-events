package edu.eci.patricia.domain.valueobjects;


import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class EventId {

    private final UUID value;

    public EventId(UUID value) {
        this.value = value;
    }

    public static EventId generate() {
        return new EventId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
