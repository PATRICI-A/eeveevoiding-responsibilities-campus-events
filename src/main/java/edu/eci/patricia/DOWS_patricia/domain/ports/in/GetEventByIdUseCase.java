package edu.eci.patricia.DOWS_patricia.domain.ports.in;

import edu.eci.patricia.DOWS_patricia.domain.model.Event;

// obtiene el evento por el ID
public interface GetEventByIdUseCase {
    Event execute(String id);
}

