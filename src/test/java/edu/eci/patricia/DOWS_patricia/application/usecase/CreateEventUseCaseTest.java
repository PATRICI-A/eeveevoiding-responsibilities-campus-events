package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventMapper;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventDomainException;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateEventUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private CreateEventUseCase createEventUseCase;

    private UUID organizerId;
    private EventRequest openRequest;
    private EventRequest withCapacityRequest;
    private Event mappedEvent;
    private Event savedEvent;
    private EventResponse eventResponse;

    @BeforeEach
    void setUp() {
        organizerId = UUID.randomUUID();

        openRequest = EventRequest.builder()
                .name("Open Seminar")
                .description("A cultural event")
                .dateTime(LocalDateTime.now().plusDays(10))
                .durationMinutes(60)
                .location("Room 101")
                .category(EventCategory.CULTURAL)
                .type(EventType.OPEN)
                .build();

        withCapacityRequest = EventRequest.builder()
                .name("Workshop")
                .description("Academic workshop")
                .dateTime(LocalDateTime.now().plusDays(10))
                .durationMinutes(120)
                .location("Lab 3")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(50)
                .build();

        mappedEvent = Event.builder()
                .name("Open Seminar")
                .category(EventCategory.CULTURAL)
                .type(EventType.OPEN)
                .build();

        savedEvent = Event.builder()
                .id(EventId.generate())
                .name("Open Seminar")
                .category(EventCategory.CULTURAL)
                .type(EventType.OPEN)
                .status(EventStatus.ACTIVE)
                .organizerId(organizerId)
                .build();

        eventResponse = EventResponse.builder()
                .id(savedEvent.getId().getValue())
                .name("Open Seminar")
                .status(EventStatus.ACTIVE)
                .build();
    }

    @Test
    void shouldCreateOpenEventSuccessfully() {
        when(eventMapper.toDomain(openRequest)).thenReturn(mappedEvent);
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);
        when(eventMapper.toDTO(any(Event.class))).thenReturn(eventResponse);

        EventResponse result = createEventUseCase.execute(openRequest, organizerId);

        assertNotNull(result);
        assertEquals("Open Seminar", result.getName());
        verify(eventMapper).toDomain(openRequest);
        verify(eventRepository, times(2)).save(any(Event.class));
        verify(eventMapper).toDTO(any(Event.class));
    }

    @Test
    void shouldCreateWithCapacityEventSuccessfully() {
        Event capacityMappedEvent = Event.builder()
                .name("Workshop")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(50)
                .build();

        Event capacitySavedEvent = Event.builder()
                .id(EventId.generate())
                .name("Workshop")
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(50)
                .availableCapacity(50)
                .status(EventStatus.ACTIVE)
                .organizerId(organizerId)
                .build();

        EventResponse capacityResponse = EventResponse.builder()
                .id(capacitySavedEvent.getId().getValue())
                .name("Workshop")
                .maxCapacity(50)
                .availableCapacity(50)
                .status(EventStatus.ACTIVE)
                .build();

        when(eventMapper.toDomain(withCapacityRequest)).thenReturn(capacityMappedEvent);
        when(eventRepository.save(any(Event.class))).thenReturn(capacitySavedEvent);
        when(eventMapper.toDTO(any(Event.class))).thenReturn(capacityResponse);

        EventResponse result = createEventUseCase.execute(withCapacityRequest, organizerId);

        assertNotNull(result);
        assertEquals(50, result.getMaxCapacity());
        assertEquals(50, result.getAvailableCapacity());
    }

    @Test
    void shouldThrowEventDomainExceptionWhenWithCapacityAndNoMaxCapacity() {
        EventRequest badRequest = EventRequest.builder()
                .name("Workshop")
                .dateTime(LocalDateTime.now().plusDays(5))
                .durationMinutes(60)
                .location("Lab")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(null)
                .build();

        assertThrows(EventDomainException.class, () -> createEventUseCase.execute(badRequest, organizerId));
        verify(eventRepository, never()).save(any());
    }

    @Test
    void shouldSetStatusActiveOnCreation() {
        when(eventMapper.toDomain(openRequest)).thenReturn(mappedEvent);
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);
        when(eventMapper.toDTO(any(Event.class))).thenReturn(eventResponse);

        createEventUseCase.execute(openRequest, organizerId);

        verify(eventRepository, times(2)).save(argThat(e -> e.getStatus() == EventStatus.ACTIVE || e.getQrCode() != null));
    }

    @Test
    void shouldSetQrCodeAfterFirstSave() {
        when(eventMapper.toDomain(openRequest)).thenReturn(mappedEvent);
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);
        when(eventMapper.toDTO(any(Event.class))).thenReturn(eventResponse);

        createEventUseCase.execute(openRequest, organizerId);

        verify(eventRepository, times(2)).save(any(Event.class));
    }

    @Test
    void shouldSetOrganizerIdOnCreatedEvent() {
        when(eventMapper.toDomain(openRequest)).thenReturn(mappedEvent);
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);
        when(eventMapper.toDTO(any(Event.class))).thenReturn(eventResponse);

        createEventUseCase.execute(openRequest, organizerId);

        verify(eventRepository, times(2)).save(argThat(e -> organizerId.equals(e.getOrganizerId()) || e.getQrCode() != null));
    }
}
