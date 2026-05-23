package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.response.EventFeedResponse;
import edu.eci.patricia.application.mapper.EventMapper;
import edu.eci.patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.domain.ports.out.EventRsvpRepositoryPort;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.domain.valueobjects.RsvpId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetRsvpUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventRsvpRepositoryPort rsvpRepository;

    @Mock
    private EventRsvpMapper rsvpMapper;

    @InjectMocks
    private GetRsvpUseCase getRsvpUseCase;

    private UUID studentId;
    private EventId evId1;
    private EventId evId2;
    private Event activeEvent;
    private Event cancelledEvent;
    private EventRsvp rsvp1;
    private EventRsvp rsvp2;
    private EventFeedResponse feedResponse;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        evId1 = EventId.generate();
        evId2 = EventId.generate();

        activeEvent = Event.builder()
                .id(evId1)
                .name("Evento Activo")
                .status(EventStatus.ACTIVE)
                .type(EventType.OPEN)
                .category(EventCategory.ACADEMIC)
                .build();

        cancelledEvent = Event.builder()
                .id(evId2)
                .name("Evento Cancelado")
                .status(EventStatus.CANCELLED)
                .type(EventType.OPEN)
                .category(EventCategory.ACADEMIC)
                .build();

        rsvp1 = EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(evId1)
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .build();

        rsvp2 = EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(evId2)
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .build();

        feedResponse = EventFeedResponse.builder()
                .id(evId1.getValue())
                .name("Evento Activo")
                .status(EventStatus.ACTIVE)
                .build();
    }

    @Test
    void execute_shouldReturnActiveEvents_whenStudentHasRsvps() {
        when(rsvpRepository.findByStudentId(studentId)).thenReturn(List.of(rsvp1));
        when(eventRepository.findById(evId1)).thenReturn(Optional.of(activeEvent));
        when(eventMapper.toFeedDTO(activeEvent)).thenReturn(feedResponse);

        List<EventFeedResponse> result = getRsvpUseCase.execute(studentId);

        assertEquals(1, result.size());
        assertEquals("Evento Activo", result.get(0).getName());
    }

    @Test
    void execute_shouldReturnEmptyList_whenStudentHasNoRsvps() {
        when(rsvpRepository.findByStudentId(studentId)).thenReturn(List.of());

        List<EventFeedResponse> result = getRsvpUseCase.execute(studentId);

        assertTrue(result.isEmpty());
    }

    @Test
    void execute_shouldExcludeCancelledEvents_whenRsvpEventIsCancelled() {
        when(rsvpRepository.findByStudentId(studentId)).thenReturn(List.of(rsvp2));
        when(eventRepository.findById(evId2)).thenReturn(Optional.of(cancelledEvent));

        List<EventFeedResponse> result = getRsvpUseCase.execute(studentId);

        assertTrue(result.isEmpty());
        verify(eventMapper, never()).toFeedDTO(any());
    }

    @Test
    void execute_shouldExcludeEvents_whenEventNotFound() {
        when(rsvpRepository.findByStudentId(studentId)).thenReturn(List.of(rsvp1));
        when(eventRepository.findById(evId1)).thenReturn(Optional.empty());

        List<EventFeedResponse> result = getRsvpUseCase.execute(studentId);

        assertTrue(result.isEmpty());
        verify(eventMapper, never()).toFeedDTO(any());
    }

    @Test
    void execute_shouldReturnOnlyActiveEvents_whenMixedRsvps() {
        when(rsvpRepository.findByStudentId(studentId)).thenReturn(List.of(rsvp1, rsvp2));
        when(eventRepository.findById(evId1)).thenReturn(Optional.of(activeEvent));
        when(eventRepository.findById(evId2)).thenReturn(Optional.of(cancelledEvent));
        when(eventMapper.toFeedDTO(activeEvent)).thenReturn(feedResponse);

        List<EventFeedResponse> result = getRsvpUseCase.execute(studentId);

        assertEquals(1, result.size());
        verify(eventMapper, times(1)).toFeedDTO(any());
    }
}
