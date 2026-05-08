package edu.eci.patricia.DOWS_patricia.domain.ports.in;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;

public interface CancelRsvpPort {
    EventResponseRsvp execute(String id);
}
