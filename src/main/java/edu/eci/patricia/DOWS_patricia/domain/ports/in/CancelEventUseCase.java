package edu.eci.patricia.DOWS_patricia.domain.ports.in;

import edu.eci.patricia.DOWS_patricia.domain.model.Event;

// Para cancelar un evento
public interface CancelEventUseCase {
    Event execute(String eventId, String organizerId, String reason);
}