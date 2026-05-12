package edu.eci.patricia.domain.ports.in;

import edu.eci.patricia.application.dto.request.EventRequest;
import edu.eci.patricia.application.dto.response.EventResponse;
import java.util.UUID;

public interface CreateEventPort {
    EventResponse execute(EventRequest request, UUID organizerId);
}