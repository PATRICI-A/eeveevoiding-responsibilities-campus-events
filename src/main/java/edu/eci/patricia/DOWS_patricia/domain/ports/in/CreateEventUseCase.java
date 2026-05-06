package edu.eci.patricia.DOWS_patricia.domain.ports.in;

import edu.eci.patricia.DOWS_patricia.domain.model.Evento;

// para publicar un nuevo evento
public interface CreateEventUseCase {
    Evento execute(Evento event);
}