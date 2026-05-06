package edu.eci.patricia.DOWS_patricia.domain.ports.in;

import edu.eci.patricia.DOWS_patricia.domain.model.Evento;

// Para cancelar un evento
public interface CancelEventUseCase {
    Evento execute(String eventId, String organizerId, String reason);
}