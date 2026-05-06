package edu.eci.patricia.DOWS_patricia.domain.ports.in;

import edu.eci.patricia.DOWS_patricia.domain.model.Evento;

// obtiene el evento por el ID
public interface GetEventByIdUseCase {
    Evento execute(String id);
}

