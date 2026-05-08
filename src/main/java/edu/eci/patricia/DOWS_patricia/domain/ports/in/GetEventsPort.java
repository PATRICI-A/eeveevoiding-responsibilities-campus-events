package edu.eci.patricia.DOWS_patricia.domain.ports.in;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;

import java.util.List;

public interface GetEventsPort {
    List<EventResponse> execute();
}