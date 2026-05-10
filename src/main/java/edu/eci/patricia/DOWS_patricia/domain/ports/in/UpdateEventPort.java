package edu.eci.patricia.DOWS_patricia.domain.ports.in;


import edu.eci.patricia.DOWS_patricia.application.dto.request.EventUpdateRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;


public interface UpdateEventPort {
    EventResponse execute(String eventID, EventUpdateRequest request, String organizerId);
}
