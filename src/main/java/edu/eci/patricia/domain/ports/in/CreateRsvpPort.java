package edu.eci.patricia.domain.ports.in;

import edu.eci.patricia.application.dto.response.EventResponseRsvp;

import java.util.UUID;

public interface CreateRsvpPort {
    EventResponseRsvp execute(UUID eventId, UUID studentId);
}
