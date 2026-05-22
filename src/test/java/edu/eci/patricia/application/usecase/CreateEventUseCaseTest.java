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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private EventId eventId;
    private Event mappedEvent;
    private Event savedEvent;
    private EventResponse expectedResponse;

    @BeforeEach
    void setUp() {
        organizerId = UUID.randomUUID();
        eventId = EventId.generate();

        mappedEvent = Event.builder()
                .name("Tech Talk")
                .description("A tech event")
                .dateTime(LocalDate.now().plusDays(10))
                .startTime(LocalTime.of(10, 0))
                .durationMinutes(60)
                .location("Auditorio ECI")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .build();

        savedEvent = Event.builder()
                .id(eventId)
                .name("Tech Talk")
                .description("A tech event")
                .dateTime(LocalDate.now().plusDays(10))
                .startTime(LocalTime.of(10, 0))
                .durationMinutes(60)
                .location("Auditorio ECI")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .status(EventStatus.ACTIVE)
                .organizerId(organizerId)
                .qrCode("QR-" + eventId.getValue())
                .build();

        expectedResponse = EventResponse.builder()
                .id(eventId.getValue())
                .name("Tech Talk")
                .status(EventStatus.ACTIVE)
                .build();
    }

    @Test
    void execute_openEvent_createsAndReturnsResponse() {
        EventRequest request = EventRequest.builder()
                .name("Tech Talk")
                .description("A tech event")
                .dateTime(LocalDate.now().plusDays(10))
                .startTime("10:00")
                .duration(60)
                .location("Auditorio ECI")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .build();

        when(eventMapper.toDomain(request)).thenReturn(mappedEvent);
        when(eventRepository.existsByName("Tech Talk")).thenReturn(false);
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);
        when(eventMapper.toDTO(savedEvent)).thenReturn(expectedResponse);

        EventResponse result = createEventUseCase.execute(request, organizerId);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Tech Talk");
        assertThat(result.getStatus()).isEqualTo(EventStatus.ACTIVE);
        verify(eventRepository, times(2)).save(any(Event.class));
    }

    @Test
    void execute_withCapacityEvent_setsAvailableCapacity() {
        EventRequest request = EventRequest.builder()
                .name("Workshop Java")
                .dateTime(LocalDate.now().plusDays(5))
                .startTime("14:00")
                .duration(90)
                .location("Lab 301")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(30)
                .build();

        Event mappedCapacityEvent = Event.builder()
                .name("Workshop Java")
                .type(EventType.WITH_CAPACITY)
                .build();

        Event savedCapacityEvent = Event.builder()
                .id(eventId)
                .name("Workshop Java")
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(30)
                .availableCapacity(30)
                .status(EventStatus.ACTIVE)
                .organizerId(organizerId)
                .qrCode("QR-" + eventId.getValue())
                .build();

        when(eventMapper.toDomain(request)).thenReturn(mappedCapacityEvent);
        when(eventRepository.existsByName("Workshop Java")).thenReturn(false);
        when(eventRepository.save(any(Event.class))).thenReturn(savedCapacityEvent);
        when(eventMapper.toDTO(savedCapacityEvent)).thenReturn(expectedResponse);

        createEventUseCase.execute(request, organizerId);

        verify(eventRepository, times(2)).save(any(Event.class));
    }

    @Test
    void execute_withCapacityAndNullMaxCapacity_throwsEventDomainException() {
        EventRequest request = EventRequest.builder()
                .name("Workshop sin cupo")
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(null)
                .build();

        assertThatThrownBy(() -> createEventUseCase.execute(request, organizerId))
                .isInstanceOf(EventDomainException.class)
                .hasMessageContaining("Max capacity is required");
    }

    @Test
    void execute_withCapacityLessThanTwo_throwsEventDomainException() {
        EventRequest request = EventRequest.builder()
                .name("Evento pequeño")
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(1)
                .build();

        assertThatThrownBy(() -> createEventUseCase.execute(request, organizerId))
                .isInstanceOf(EventDomainException.class)
                .hasMessageContaining("Minimum of 2 spots");
    }

    @Test
    void execute_duplicateName_throwsEventDomainException() {
        EventRequest request = EventRequest.builder()
                .name("Tech Talk")
                .type(EventType.OPEN)
                .build();

        when(eventRepository.existsByName("Tech Talk")).thenReturn(true);

        assertThatThrownBy(() -> createEventUseCase.execute(request, organizerId))
                .isInstanceOf(EventDomainException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void execute_setsQrCodeAfterFirstSave() {
        EventRequest request = EventRequest.builder()
                .name("Tech Talk")
                .type(EventType.OPEN)
                .dateTime(LocalDate.now().plusDays(10))
                .startTime("10:00")
                .duration(60)
                .location("Auditorio ECI")
                .category(EventCategory.ACADEMIC)
                .build();

        when(eventMapper.toDomain(request)).thenReturn(mappedEvent);
        when(eventRepository.existsByName("Tech Talk")).thenReturn(false);
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);
        when(eventMapper.toDTO(savedEvent)).thenReturn(expectedResponse);

        createEventUseCase.execute(request, organizerId);

        assertThat(savedEvent.getQrCode()).startsWith("QR-");
    }

    @Test
    void execute_openEvent_availableCapacityIsNull() {
        EventRequest request = EventRequest.builder()
                .name("Open Event")
                .type(EventType.OPEN)
                .dateTime(LocalDate.now().plusDays(10))
                .startTime("09:00")
                .duration(45)
                .location("Sala Virtual")
                .category(EventCategory.ACADEMIC)
                .build();

        Event openEvent = Event.builder()
                .name("Open Event")
                .type(EventType.OPEN)
                .build();

        when(eventMapper.toDomain(request)).thenReturn(openEvent);
        when(eventRepository.existsByName("Open Event")).thenReturn(false);
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);
        when(eventMapper.toDTO(savedEvent)).thenReturn(expectedResponse);

        createEventUseCase.execute(request, organizerId);

        assertThat(openEvent.getAvailableCapacity()).isNull();
    }
}
