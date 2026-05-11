package edu.eci.patricia.DOWS_patricia.domain.valueobjects;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class RsvpId {

    private final UUID value;

    public RsvpId(UUID value) {
        this.value = value;
    }

    public static RsvpId generate() {
        return new RsvpId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}