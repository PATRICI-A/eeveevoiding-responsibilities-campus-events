package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.request.EventUpdateRequest;
import edu.eci.patricia.application.dto.response.EventResponse;
import edu.eci.patricia.application.mapper.EventMapper;
import edu.eci.patricia.domain.exceptions.EventDomainException;
import edu.eci.patricia.domain.exceptions.EventNotActiveException;
import edu.eci.patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.domain.exceptions.UnauthorizedOrganizerException;
import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.domain.valueobjects.EventId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateEventUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private UpdateEventUseCase updateEventUseCase;

    private UUID eventId;
    private UUID organizerId;
    private Event activeEvent;
    private EventUpdateRequest updateRequest;
    private EventResponse eventResponse;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        organizerId = UUID.randomUUID();

        activeEvent = Event.builder()
                .id(new EventId(eventId))
                .name("Evento Original")
                .status(EventStatus.ACTIVE)
                .organizerId(organizerId)
                .type(EventType.OPEN)
                .category(EventCategory.ACADEMIC)
                .build();

        updateRequest = EventUpdateRequest.builder()
                .name("Evento Actualizado")
                .description("Nueva descripción")
                .dateTime(LocalDate.now().plusDays(2))
                .startTime("10:00")
                .duration(90)
                .location("Medellín")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .build();

        eventResponse = EventResponse.builder()
                .id(eventId)
                .name("Evento Actualizado")
                .build();
    }

    @Test
    void execute_shouldUpdateEvent_whenValidRequest() {
        when(eventRepository.findById(new EventId(eventId))).thenReturn(Optional.of(activeEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(activeEvent);
        when(eventMapper.toDTO(any(Event.class))).thenReturn(eventResponse);

        EventResponse result = updateEventUseCase.execute(eventId, updateRequest, organizerId);

        assertNotNull(result);
        assertEquals("Evento Actualizado", result.getName());
        verify(eventRepository).save(activeEvent);
    }

    @Test
    void execute_shouldThrowException_whenEventNotFound() {
        when(eventRepository.findById(new EventId(eventId))).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> updateEventUseCase.execute(eventId, updateRequest, organizerId));

        verify(eventRepository, never()).save(any());
    }

    @Test
    void execute_shouldThrowException_whenEventIsNotActive() {
        activeEvent.setStatus(EventStatus.CANCELLED);
        when(eventRepository.findById(new EventId(eventId))).thenReturn(Optional.of(activeEvent));

        assertThrows(EventNotActiveException.class,
                () -> updateEventUseCase.execute(eventId, updateRequest, organizerId));

        verify(eventRepository, never()).save(any());
    }

    @Test
    void execute_shouldThrowException_whenOrganizerIsNotOwner() {
        UUID otherOrganizer = UUID.randomUUID();
        when(eventRepository.findById(new EventId(eventId))).thenReturn(Optional.of(activeEvent));

        assertThrows(UnauthorizedOrganizerException.class,
                () -> updateEventUseCase.execute(eventId, updateRequest, otherOrganizer));

        verify(eventRepository, never()).save(any());
    }

    @Test
    void execute_shouldThrowException_whenWithCapacityAndNullMaxCapacity() {
        updateRequest = EventUpdateRequest.builder()
                .name("Evento")
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(null)
                .build();

        when(eventRepository.findById(new EventId(eventId))).thenReturn(Optional.of(activeEvent));

        assertThrows(EventDomainException.class,
                () -> updateEventUseCase.execute(eventId, updateRequest, organizerId));

        verify(eventRepository, never()).save(any());
    }

    @Test
    void execute_shouldThrowException_whenWithCapacityLessThan2() {
        updateRequest = EventUpdateRequest.builder()
                .name("Evento")
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(1)
                .build();

        when(eventRepository.findById(new EventId(eventId))).thenReturn(Optional.of(activeEvent));

        assertThrows(EventDomainException.class,
                () -> updateEventUseCase.execute(eventId, updateRequest, organizerId));

        verify(eventRepository, never()).save(any());
    }

    @Test
    void execute_shouldSetMaxCapacityNull_whenTypeChangedToOpen() {
        updateRequest = EventUpdateRequest.builder()
                .name("Evento")
                .type(EventType.OPEN)
                .dateTime(LocalDate.now().plusDays(1))
                .startTime("09:00")
                .duration(60)
                .location("Bogotá")
                .category(EventCategory.ACADEMIC)
                .build();

        when(eventRepository.findById(new EventId(eventId))).thenReturn(Optional.of(activeEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(activeEvent);
        when(eventMapper.toDTO(any(Event.class))).thenReturn(eventResponse);

        updateEventUseCase.execute(eventId, updateRequest, organizerId);

        assertNull(activeEvent.getMaxCapacity());
    }
}
