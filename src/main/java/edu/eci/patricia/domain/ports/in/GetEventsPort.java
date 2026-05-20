package edu.eci.patricia.domain.ports.in;

import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.dto.response.EventResponse;
import edu.eci.patricia.domain.model.enums.EventCategory;

import java.time.LocalDate;
import java.util.List;

public interface GetEventsPort {
    List<EventFeedResponse> execute(EventCategory category, LocalDate date);
}