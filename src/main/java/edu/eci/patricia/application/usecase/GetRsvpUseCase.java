package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.mapper.EventMapper;
import edu.eci.patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.ports.in.GetRsvpPort;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.domain.ports.out.EventRsvpRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class GetRsvpUseCase implements GetRsvpPort {

    private final EventRepositoryPort eventRepository;
    private final EventMapper eventMapper;
    private final EventRsvpRepositoryPort rsvpRepository;
    private final EventRsvpMapper rsvpMapper;

    @Override
    public List<EventFeedResponse> execute(UUID studentId) {

        List<EventRsvp> rsvps = rsvpRepository.findByStudentId(studentId);

        return rsvps.stream()
                .map(rsvp -> eventRepository.findById(rsvp.getEventId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(eventMapper::toFeedDTO)
                .toList();
    }
}
