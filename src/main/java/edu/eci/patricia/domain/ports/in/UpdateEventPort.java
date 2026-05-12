package edu.eci.patricia.domain.ports.in;


import edu.eci.patricia.application.dto.request.EventUpdateRequest;
import edu.eci.patricia.application.dto.response.EventResponse;

import java.util.UUID;


public interface UpdateEventPort {
    EventResponse execute(UUID eventId, EventUpdateRequest request, UUID organizerId);
}
