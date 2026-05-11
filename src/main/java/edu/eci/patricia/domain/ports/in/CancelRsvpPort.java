package edu.eci.patricia.domain.ports.in;

import java.util.UUID;

public interface CancelRsvpPort {
    void execute(UUID eventId, UUID studentId);
}

