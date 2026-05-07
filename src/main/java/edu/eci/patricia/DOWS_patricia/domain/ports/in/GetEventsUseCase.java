package edu.eci.patricia.DOWS_patricia.domain.ports.in;

import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;

import java.time.LocalDate;
import java.util.List;

// Entrada para consultar los eventos con filtros
public interface GetEventsUseCase {
    List<Event> execute(EventCategory category, LocalDate date, int page, int size);
}

