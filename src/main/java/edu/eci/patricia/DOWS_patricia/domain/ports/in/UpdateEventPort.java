package edu.eci.patricia.DOWS_patricia.domain.ports.in;


import edu.eci.patricia.DOWS_patricia.application.dto.request.EventUpdateRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;

import java.util.UUID;


public interface UpdateEventPort {
    EventResponse execute(UUID eventId, EventUpdateRequest request, UUID organizerId);
}
