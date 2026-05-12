package edu.eci.patricia.domain.ports.in;

import java.util.UUID;

public interface CancelEventPort {
    void execute(UUID eventId, UUID organizerId);
}