package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.mapper.EventMapper;
import edu.eci.patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.ports.in.GetRsvpPort;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.domain.ports.out.EventRsvpRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Caso de uso para obtener todos los eventos a los que un estudiante
 * tiene una reserva confirmada.
 * <p>
 * Solo retorna eventos que aún están ACTIVOS.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class GetRsvpUseCase implements GetRsvpPort {

    private final EventRepositoryPort eventRepository;
    private final EventMapper eventMapper;
    private final EventRsvpRepositoryPort rsvpRepository;
    private final EventRsvpMapper rsvpMapper;

    /**
     * Ejecuta la consulta de eventos reservados por un estudiante.
     *
     * @param studentId identificador del estudiante
     * @return lista de eventos activos en los que el estudiante tiene reserva
     */
    @Override
    public List<EventFeedResponse> execute(UUID studentId) {

        List<EventRsvp> rsvps = rsvpRepository.findByStudentId(studentId);

        return rsvps.stream()
                .map(rsvp -> eventRepository.findById(rsvp.getEventId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(event -> event.getStatus().equals(EventStatus.ACTIVE))
                .map(eventMapper::toFeedDTO)
                .toList();
    }
}