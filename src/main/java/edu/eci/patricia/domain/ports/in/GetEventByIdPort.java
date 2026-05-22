package edu.eci.patricia.domain.ports.in;

import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.dto.response.EventResponse;
import java.util.UUID;

public interface GetEventByIdPort {
    EventFeedResponse execute(UUID eventId);
}