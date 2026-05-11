package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventUpdateRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventMapper;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotActiveException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.UnauthorizedOrganizerException;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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

    private UUID eventUUID;
    private UUID organizerId;
    private Event activeEvent;
    private EventUpdateRequest updateRequest;
    private EventResponse eventResponse;

    @BeforeEach
    void setUp() {
        eventUUID = UUID.randomUUID();
        organizerId = UUID.randomUUID();

        activeEvent = Event.builder()
                .id(new EventId(eventUUID))
                .name("Old Name")
                .description("Old Description")
                .dateTime(LocalDateTime.now().plusDays(5))
                .durationMinutes(60)
                .location("Old Location")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .status(EventStatus.ACTIVE)
                .organizerId(organizerId)
                .build();

        updateRequest = EventUpdateRequest.builder()
                .name("New Name")
                .description("New Description")
                .dateTime(LocalDateTime.now().plusDays(10))
                .durationMinutes(90)
                .location("New Location")
                .category(EventCategory.CULTURAL)
                .build();

        eventResponse = EventResponse.builder()
                .id(eventUUID)
                .name("New Name")
                .status(EventStatus.ACTIVE)
                .build();
    }

    @Test
    void shouldUpdateEventSuccessfully() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(activeEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(activeEvent);
        when(eventMapper.toDTO(any(Event.class))).thenReturn(eventResponse);

        EventResponse result = updateEventUseCase.execute(eventUUID, updateRequest, organizerId);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(eventRepository).findById(any(EventId.class));
        verify(eventRepository).save(activeEvent);
        verify(eventMapper).toDTO(activeEvent);
    }

    @Test
    void shouldThrowEventNotFoundExceptionWhenEventDoesNotExist() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                () -> updateEventUseCase.execute(eventUUID, updateRequest, organizerId));

        verify(eventRepository, never()).save(any());
    }

    @Test
    void shouldThrowEventNotActiveExceptionWhenEventIsCancelled() {
        activeEvent.setStatus(EventStatus.CANCELLED);
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(activeEvent));

        assertThrows(EventNotActiveException.class,
                () -> updateEventUseCase.execute(eventUUID, updateRequest, organizerId));

        verify(eventRepository, never()).save(any());
    }

    @Test
    void shouldThrowUnauthorizedOrganizerExceptionWhenOrganizerDoesNotMatch() {
        UUID differentOrganizer = UUID.randomUUID();
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(activeEvent));

        assertThrows(UnauthorizedOrganizerException.class,
                () -> updateEventUseCase.execute(eventUUID, updateRequest, differentOrganizer));

        verify(eventRepository, never()).save(any());
    }

    @Test
    void shouldApplyAllFieldsFromUpdateRequest() {
        when(eventRepository.findById(any(EventId.class))).thenReturn(Optional.of(activeEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(activeEvent);
        when(eventMapper.toDTO(any(Event.class))).thenReturn(eventResponse);

        updateEventUseCase.execute(eventUUID, updateRequest, organizerId);

        assertEquals("New Name", activeEvent.getName());
        assertEquals("New Description", activeEvent.getDescription());
        assertEquals("New Location", activeEvent.getLocation());
        assertEquals(EventCategory.CULTURAL, activeEvent.getCategory());
        assertEquals(90, activeEvent.getDurationMinutes());
        assertNotNull(activeEvent.getUpdatedAt());
    }
}
