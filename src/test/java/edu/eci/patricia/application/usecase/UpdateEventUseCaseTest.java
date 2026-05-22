package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.request.EventUpdateRequest;
import edu.eci.patricia.application.dto.response.EventResponse;
import edu.eci.patricia.application.mapper.EventMapper;
import edu.eci.patricia.domain.exceptions.EventDomainException;
import edu.eci.patricia.domain.exceptions.EventNotActiveException;
import edu.eci.patricia.domain.exceptions.EventNotFoundException;
import edu.eci.patricia.domain.exceptions.UnauthorizedOrganizerException;
import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.domain.ports.out.EventRsvpRepositoryPort;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.infrastructure.messaging.EventChangePublisher;
import edu.eci.patricia.infrastructure.messaging.dto.EventChangeEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateEventUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventRsvpRepositoryPort rsvpRepository;

    @Mock
    private EventChangePublisher eventChangePublisher;

    @InjectMocks
    private UpdateEventUseCase updateEventUseCase;

    private UUID eventUUID;
    private UUID organizerId;
    private EventId eventId;
    private Event activeEvent;
    private EventUpdateRequest updateRequest;
    private EventResponse expectedResponse;

    @BeforeEach
    void setUp() {
        eventUUID = UUID.randomUUID();
        organizerId = UUID.randomUUID();
        eventId = new EventId(eventUUID);

        activeEvent = Event.builder()
                .id(eventId)
                .name("Tech Talk")
                .description("Original description")
                .dateTime(LocalDate.now().plusDays(10))
                .startTime(LocalTime.of(10, 0))
                .durationMinutes(60)
                .location("Auditorio ECI")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .status(EventStatus.ACTIVE)
                .organizerId(organizerId)
                .build();

        updateRequest = EventUpdateRequest.builder()
                .name("Tech Talk Updated")
                .description("Updated description")
                .dateTime(LocalDate.now().plusDays(15))
                .startTime("11:00")
                .duration(90)
                .location("Lab 201")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .build();

        expectedResponse = EventResponse.builder()
                .id(eventUUID)
                .name("Tech Talk Updated")
                .status(EventStatus.ACTIVE)
                .build();
    }

    @Test
    void execute_validUpdate_returnsUpdatedResponse() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(activeEvent));
        when(eventMapper.stringToLocalTime("11:00")).thenReturn(LocalTime.of(11, 0));
        when(rsvpRepository.findConfirmedByEventId(eventId)).thenReturn(List.of());
        when(eventRepository.save(activeEvent)).thenReturn(activeEvent);
        when(eventMapper.toDTO(activeEvent)).thenReturn(expectedResponse);

        EventResponse result = updateEventUseCase.execute(eventUUID, updateRequest, organizerId);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Tech Talk Updated");
        verify(eventRepository).save(activeEvent);
    }

    @Test
    void execute_eventNotFound_throwsEventNotFoundException() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateEventUseCase.execute(eventUUID, updateRequest, organizerId))
                .isInstanceOf(EventNotFoundException.class);
    }

    @Test
    void execute_eventNotActive_throwsEventNotActiveException() {
        Event cancelledEvent = Event.builder()
                .id(eventId)
                .status(EventStatus.CANCELLED)
                .organizerId(organizerId)
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(cancelledEvent));

        assertThatThrownBy(() -> updateEventUseCase.execute(eventUUID, updateRequest, organizerId))
                .isInstanceOf(EventNotActiveException.class)
                .hasMessageContaining("ACTIVE");
    }

    @Test
    void execute_wrongOrganizer_throwsUnauthorizedOrganizerException() {
        UUID anotherOrganizer = UUID.randomUUID();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(activeEvent));

        assertThatThrownBy(() -> updateEventUseCase.execute(eventUUID, updateRequest, anotherOrganizer))
                .isInstanceOf(UnauthorizedOrganizerException.class);
    }

    @Test
    void execute_withCapacityAndNullMaxCapacity_throwsEventDomainException() {
        EventUpdateRequest badRequest = EventUpdateRequest.builder()
                .name("Bad Event")
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(null)
                .startTime("10:00")
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(activeEvent));

        assertThatThrownBy(() -> updateEventUseCase.execute(eventUUID, badRequest, organizerId))
                .isInstanceOf(EventDomainException.class)
                .hasMessageContaining("Max capacity is required");
    }

    @Test
    void execute_withCapacityLessThanTwo_throwsEventDomainException() {
        EventUpdateRequest badRequest = EventUpdateRequest.builder()
                .name("Small Event")
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(1)
                .startTime("10:00")
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(activeEvent));

        assertThatThrownBy(() -> updateEventUseCase.execute(eventUUID, badRequest, organizerId))
                .isInstanceOf(EventDomainException.class)
                .hasMessageContaining("Minimum of 2 spots");
    }

    @Test
    void execute_withCapacityType_setsMaxCapacity() {
        EventUpdateRequest capacityRequest = EventUpdateRequest.builder()
                .name("Workshop")
                .description("Desc")
                .dateTime(LocalDate.now().plusDays(5))
                .startTime("09:00")
                .duration(60)
                .location("Lab 301")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(20)
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(activeEvent));
        when(eventMapper.stringToLocalTime("09:00")).thenReturn(LocalTime.of(9, 0));
        when(rsvpRepository.findConfirmedByEventId(eventId)).thenReturn(List.of());
        when(eventRepository.save(activeEvent)).thenReturn(activeEvent);
        when(eventMapper.toDTO(activeEvent)).thenReturn(expectedResponse);

        updateEventUseCase.execute(eventUUID, capacityRequest, organizerId);

        assertThat(activeEvent.getMaxCapacity()).isEqualTo(20);
    }

    @Test
    void execute_openType_clearsMaxCapacity() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(activeEvent));
        when(eventMapper.stringToLocalTime("11:00")).thenReturn(LocalTime.of(11, 0));
        when(rsvpRepository.findConfirmedByEventId(eventId)).thenReturn(List.of());
        when(eventRepository.save(activeEvent)).thenReturn(activeEvent);
        when(eventMapper.toDTO(activeEvent)).thenReturn(expectedResponse);

        updateEventUseCase.execute(eventUUID, updateRequest, organizerId);

        assertThat(activeEvent.getMaxCapacity()).isNull();
    }

    @Test
    void execute_withConfirmedRsvps_publishesChangeEventForEachRsvp() {
        UUID student1 = UUID.randomUUID();
        UUID student2 = UUID.randomUUID();

        EventRsvp rsvp1 = EventRsvp.builder().eventId(eventId).studentId(student1).status(RsvpStatus.CONFIRMED).build();
        EventRsvp rsvp2 = EventRsvp.builder().eventId(eventId).studentId(student2).status(RsvpStatus.CONFIRMED).build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(activeEvent));
        when(eventMapper.stringToLocalTime("11:00")).thenReturn(LocalTime.of(11, 0));
        when(rsvpRepository.findConfirmedByEventId(eventId)).thenReturn(List.of(rsvp1, rsvp2));
        when(eventRepository.save(activeEvent)).thenReturn(activeEvent);
        when(eventMapper.toDTO(activeEvent)).thenReturn(expectedResponse);

        updateEventUseCase.execute(eventUUID, updateRequest, organizerId);

        ArgumentCaptor<EventChangeEventDto> captor = ArgumentCaptor.forClass(EventChangeEventDto.class);
        verify(eventChangePublisher, times(2)).publish(captor.capture());

        List<EventChangeEventDto> published = captor.getAllValues();
        assertThat(published).extracting(EventChangeEventDto::getTargetUserId)
                .containsExactlyInAnyOrder(student1, student2);
        assertThat(published).extracting(EventChangeEventDto::getEventId)
                .containsOnly(eventUUID);
        assertThat(published).extracting(EventChangeEventDto::getChangeDescription)
                .containsOnly("modificado");
    }

    @Test
    void execute_noConfirmedRsvps_doesNotPublishAnyEvent() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(activeEvent));
        when(eventMapper.stringToLocalTime("11:00")).thenReturn(LocalTime.of(11, 0));
        when(rsvpRepository.findConfirmedByEventId(eventId)).thenReturn(List.of());
        when(eventRepository.save(activeEvent)).thenReturn(activeEvent);
        when(eventMapper.toDTO(activeEvent)).thenReturn(expectedResponse);

        updateEventUseCase.execute(eventUUID, updateRequest, organizerId);

        verify(eventChangePublisher, never()).publish(any());
    }

    @Test
    void execute_updatesAllFieldsOnEvent() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(activeEvent));
        when(eventMapper.stringToLocalTime("11:00")).thenReturn(LocalTime.of(11, 0));
        when(rsvpRepository.findConfirmedByEventId(eventId)).thenReturn(List.of());
        when(eventRepository.save(activeEvent)).thenReturn(activeEvent);
        when(eventMapper.toDTO(activeEvent)).thenReturn(expectedResponse);

        updateEventUseCase.execute(eventUUID, updateRequest, organizerId);

        assertThat(activeEvent.getName()).isEqualTo("Tech Talk Updated");
        assertThat(activeEvent.getDescription()).isEqualTo("Updated description");
        assertThat(activeEvent.getLocation()).isEqualTo("Lab 201");
        assertThat(activeEvent.getDurationMinutes()).isEqualTo(90);
        assertThat(activeEvent.getStartTime()).isEqualTo(LocalTime.of(11, 0));
    }
}
