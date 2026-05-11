package edu.eci.patricia.DOWS_patricia.domain.ports.in;

import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;

import java.time.LocalDate;
import java.util.List;

public interface GetEventsPort {
    List<EventResponse> execute(EventCategory category, LocalDate date);
}