package edu.eci.patricia.DOWS_patricia.domain.ports.in;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;

public interface GetEventByIdPort {
    EventResponse execute(String id);
}