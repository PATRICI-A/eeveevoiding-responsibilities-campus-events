package edu.eci.patricia.domain.ports.in;

import edu.eci.patricia.application.dto.response.EventResponse;
import java.util.UUID;

public interface GetEventByIdPort {
    EventResponse execute(UUID eventId);
}