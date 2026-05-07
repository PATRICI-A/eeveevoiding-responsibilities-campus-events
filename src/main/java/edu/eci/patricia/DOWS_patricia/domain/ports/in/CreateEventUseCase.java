package edu.eci.patricia.DOWS_patricia.domain.ports.in;

import edu.eci.patricia.DOWS_patricia.domain.model.Event;

// para publicar un nuevo evento
public interface CreateEventUseCase {
    Event execute(Event event);
}