package edu.eci.patricia.DOWS_patricia.domain.ports.in;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import java.util.UUID;

public interface GetEventByIdPort {
    EventResponse execute(UUID eventId);
}