package edu.eci.patricia.DOWS_patricia.domain.ports.in;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;

import java.util.UUID;

public interface CreateRsvpPort {
    EventResponseRsvp execute(UUID eventId, UUID studentId);
}
