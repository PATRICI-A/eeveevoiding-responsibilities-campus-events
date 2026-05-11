package edu.eci.patricia.DOWS_patricia.domain.ports.in;

import java.util.UUID;

public interface CancelEventPort {
    void execute(UUID eventId, UUID organizerId);
}