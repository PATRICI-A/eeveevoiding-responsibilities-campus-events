package edu.eci.patricia.DOWS_patricia.domain.valueobjects;

import java.util.UUID;

public final class RsvpId {
    private final String value;

    public RsvpId(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("RsvpId cannot be empty");
        this.value = value;
    }

    public static RsvpId generate() {
        return new RsvpId(UUID.randomUUID().toString());
    }

    public String getValue() { return value; }
}
