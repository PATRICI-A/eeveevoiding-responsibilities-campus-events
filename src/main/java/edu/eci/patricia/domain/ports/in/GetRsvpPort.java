package edu.eci.patricia.domain.ports.in;

import edu.eci.patricia.application.dto.response.EventFeedResponse;

import java.util.List;
import java.util.UUID;

public interface GetRsvpPort {
    List<EventFeedResponse> execute(UUID studentId);
}
