package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.request.EventRequest;
import edu.eci.patricia.application.dto.response.EventResponse;
import edu.eci.patricia.application.mapper.EventMapper;
import edu.eci.patricia.domain.exceptions.EventDomainException;
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
import java.time.LocalTime;
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
    private EventRequest request;
    private Event event;
    private EventResponse eventResponse;

    @BeforeEach
    void setUp() {
        organizerId = UUID.randomUUID();

        request = EventRequest.builder()
                .name("Caminata Monserrate")
                .description("Evento de senderismo")
                .dateTime(LocalDate.now().plusDays(1))
                .startTime("08:30")
                .duration(120)
                .location("Bogotá")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .build();

        event = Event.builder()
                .id(EventId.generate())
                .name("Caminata Monserrate")
                .description("Evento de senderismo")
                .status(EventStatus.ACTIVE)
                .organizerId(organizerId)
                .type(EventType.OPEN)
                .build();

        eventResponse = EventResponse.builder()
                .id(UUID.randomUUID())
                .name("Caminata Monserrate")
                .build();
    }

    @Test
    void execute_shouldCreateEventSuccessfully_whenValidOpenEvent() {
        when(eventRepository.existsByName(request.getName())).thenReturn(false);
        when(eventMapper.toDomain(request)).thenReturn(event);
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventMapper.toDTO(any(Event.class))).thenReturn(eventResponse);

        EventResponse result = createEventUseCase.execute(request, organizerId);

        assertNotNull(result);
        assertEquals("Caminata Monserrate", result.getName());
        verify(eventRepository, times(2)).save(any(Event.class));
    }

    @Test
    void execute_shouldCreateEventWithCapacity_whenValidWithCapacityEvent() {
        request = EventRequest.builder()
                .name("Taller Java")
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(30)
                .dateTime(LocalDate.now().plusDays(1))
                .startTime("10:00")
                .duration(60)
                .location("Bogotá")
                .category(EventCategory.ACADEMIC)
                .build();

        Event eventWithCapacity = Event.builder()
                .id(EventId.generate())
                .name("Taller Java")
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(30)
                .availableCapacity(30)
                .status(EventStatus.ACTIVE)
                .organizerId(organizerId)
                .build();

        when(eventRepository.existsByName(request.getName())).thenReturn(false);
        when(eventMapper.toDomain(request)).thenReturn(eventWithCapacity);
        when(eventRepository.save(any(Event.class))).thenReturn(eventWithCapacity);
        when(eventMapper.toDTO(any(Event.class))).thenReturn(eventResponse);

        EventResponse result = createEventUseCase.execute(request, organizerId);

        assertNotNull(result);
        verify(eventRepository, times(2)).save(any(Event.class));
    }

    @Test
    void execute_shouldThrowException_whenNameAlreadyExists() {
        when(eventRepository.existsByName(request.getName())).thenReturn(true);

        EventDomainException ex = assertThrows(EventDomainException.class,
                () -> createEventUseCase.execute(request, organizerId));

        assertTrue(ex.getMessage().contains("already exists"));
        verify(eventRepository, never()).save(any());
    }

    @Test
    void execute_shouldThrowException_whenWithCapacityAndNullMaxCapacity() {
        request = EventRequest.builder()
                .name("Taller")
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(null)
                .build();

        EventDomainException ex = assertThrows(EventDomainException.class,
                () -> createEventUseCase.execute(request, organizerId));

        assertTrue(ex.getMessage().contains("Max capacity is required"));
        verify(eventRepository, never()).save(any());
    }

    @Test
    void execute_shouldThrowException_whenWithCapacityLessThan2() {
        request = EventRequest.builder()
                .name("Taller")
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(1)
                .build();

        EventDomainException ex = assertThrows(EventDomainException.class,
                () -> createEventUseCase.execute(request, organizerId));

        assertTrue(ex.getMessage().contains("Minimum of 2 spots"));
        verify(eventRepository, never()).save(any());
    }
}
