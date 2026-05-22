package edu.eci.patricia.application.usecase;

import edu.eci.patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.application.mapper.EventRsvpMapper;
import edu.eci.patricia.domain.exceptions.*;
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
import edu.eci.patricia.infrastructure.notification.NotificationServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRsvpUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventRsvpRepositoryPort rsvpRepository;

    @Mock
    private EventRsvpMapper rsvpMapper;

    @Mock
    private NotificationServiceClient notificationServiceClient;

    @InjectMocks
    private CreateRsvpUseCase createRsvpUseCase;

    private UUID eventUUID;
    private UUID studentId;
    private EventId eventId;
    private Event openActiveEvent;
    private Event capacityActiveEvent;
    private EventResponseRsvp expectedResponse;

    @BeforeEach
    void setUp() {
        eventUUID = UUID.randomUUID();
        studentId = UUID.randomUUID();
        eventId = new EventId(eventUUID);

        openActiveEvent = Event.builder()
                .id(eventId)
                .name("Tech Talk")
                .dateTime(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(10, 0))
                .type(EventType.OPEN)
                .status(EventStatus.ACTIVE)
                .availableCapacity(Integer.MAX_VALUE)
                .build();

        capacityActiveEvent = Event.builder()
                .id(eventId)
                .name("Workshop")
                .dateTime(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .type(EventType.WITH_CAPACITY)
                .status(EventStatus.ACTIVE)
                .availableCapacity(10)
                .build();

        expectedResponse = EventResponseRsvp.builder()
                .id(UUID.randomUUID())
                .eventId(eventUUID)
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .build();
    }

    @Test
    void execute_newRsvpOpenEvent_createsConfirmedRsvp() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(openActiveEvent));
        when(rsvpRepository.existsByEventIdAndStudentId(eventId, studentId)).thenReturn(false);
        when(rsvpRepository.save(any(EventRsvp.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rsvpMapper.toDTO(any(EventRsvp.class))).thenReturn(expectedResponse);

        EventResponseRsvp result = createRsvpUseCase.execute(eventUUID, studentId);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(RsvpStatus.CONFIRMED);
        verify(rsvpRepository, times(2)).save(any(EventRsvp.class));
    }

    @Test
    void execute_newRsvpOpenEvent_doesNotDecrementCapacity() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(openActiveEvent));
        when(rsvpRepository.existsByEventIdAndStudentId(eventId, studentId)).thenReturn(false);
        when(rsvpRepository.save(any(EventRsvp.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rsvpMapper.toDTO(any(EventRsvp.class))).thenReturn(expectedResponse);

        createRsvpUseCase.execute(eventUUID, studentId);

        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void execute_newRsvpWithCapacityEvent_decrementsAvailableCapacity() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(capacityActiveEvent));
        when(rsvpRepository.existsByEventIdAndStudentId(eventId, studentId)).thenReturn(false);
        when(rsvpRepository.save(any(EventRsvp.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rsvpMapper.toDTO(any(EventRsvp.class))).thenReturn(expectedResponse);

        createRsvpUseCase.execute(eventUUID, studentId);

        assertThat(capacityActiveEvent.getAvailableCapacity()).isEqualTo(9);
        verify(eventRepository).save(capacityActiveEvent);
    }

    @Test
    void execute_newRsvp_registersNotificationReminder() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(openActiveEvent));
        when(rsvpRepository.existsByEventIdAndStudentId(eventId, studentId)).thenReturn(false);
        when(rsvpRepository.save(any(EventRsvp.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rsvpMapper.toDTO(any(EventRsvp.class))).thenReturn(expectedResponse);

        createRsvpUseCase.execute(eventUUID, studentId);

        ArgumentCaptor<LocalDateTime> dateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(notificationServiceClient).registerEventReminder(
                eq(studentId),
                eq(eventUUID),
                dateCaptor.capture()
        );
        assertThat(dateCaptor.getValue()).isEqualTo(
                LocalDateTime.of(openActiveEvent.getDateTime(), openActiveEvent.getStartTime())
        );
    }

    @Test
    void execute_eventNotFound_throwsEventNotFoundException() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createRsvpUseCase.execute(eventUUID, studentId))
                .isInstanceOf(EventNotFoundException.class)
                .hasMessageContaining("Event not found");
    }

    @Test
    void execute_eventNotActive_throwsEventNotActiveException() {
        Event cancelledEvent = Event.builder()
                .id(eventId)
                .status(EventStatus.CANCELLED)
                .type(EventType.OPEN)
                .availableCapacity(Integer.MAX_VALUE)
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(cancelledEvent));

        assertThatThrownBy(() -> createRsvpUseCase.execute(eventUUID, studentId))
                .isInstanceOf(EventNotActiveException.class);
    }

    @Test
    void execute_eventAtFullCapacity_throwsEventCapacityFullException() {
        Event fullEvent = Event.builder()
                .id(eventId)
                .status(EventStatus.ACTIVE)
                .type(EventType.WITH_CAPACITY)
                .availableCapacity(0)
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(fullEvent));

        assertThatThrownBy(() -> createRsvpUseCase.execute(eventUUID, studentId))
                .isInstanceOf(EventCapacityFullException.class)
                .hasMessageContaining("FULL");
    }

    @Test
    void execute_existingConfirmedRsvp_throwsRsvpAlreadyExistsException() {
        EventRsvp confirmedRsvp = EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(eventId)
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(openActiveEvent));
        when(rsvpRepository.existsByEventIdAndStudentId(eventId, studentId)).thenReturn(true);
        when(rsvpRepository.findByEventIdAndStudentId(eventUUID, studentId))
                .thenReturn(Optional.of(confirmedRsvp));

        assertThatThrownBy(() -> createRsvpUseCase.execute(eventUUID, studentId))
                .isInstanceOf(RsvpAlreadyExistsException.class)
                .hasMessageContaining(eventUUID.toString());
    }

    @Test
    void execute_existingCancelledRsvp_reconfirmsRsvp() {
        EventRsvp cancelledRsvp = EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(eventId)
                .studentId(studentId)
                .status(RsvpStatus.CANCELLED)
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(openActiveEvent));
        when(rsvpRepository.existsByEventIdAndStudentId(eventId, studentId)).thenReturn(true);
        when(rsvpRepository.findByEventIdAndStudentId(eventUUID, studentId))
                .thenReturn(Optional.of(cancelledRsvp));
        when(rsvpRepository.save(cancelledRsvp)).thenReturn(cancelledRsvp);
        when(rsvpMapper.toDTO(cancelledRsvp)).thenReturn(expectedResponse);

        createRsvpUseCase.execute(eventUUID, studentId);

        assertThat(cancelledRsvp.getStatus()).isEqualTo(RsvpStatus.CONFIRMED);
        verify(rsvpRepository).save(cancelledRsvp);
    }

    @Test
    void execute_existingCancelledRsvpWithCapacityEvent_decrementsCapacity() {
        EventRsvp cancelledRsvp = EventRsvp.builder()
                .id(RsvpId.generate())
                .eventId(eventId)
                .studentId(studentId)
                .status(RsvpStatus.CANCELLED)
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(capacityActiveEvent));
        when(rsvpRepository.existsByEventIdAndStudentId(eventId, studentId)).thenReturn(true);
        when(rsvpRepository.findByEventIdAndStudentId(eventUUID, studentId))
                .thenReturn(Optional.of(cancelledRsvp));
        when(rsvpRepository.save(cancelledRsvp)).thenReturn(cancelledRsvp);
        when(rsvpMapper.toDTO(cancelledRsvp)).thenReturn(expectedResponse);

        createRsvpUseCase.execute(eventUUID, studentId);

        assertThat(capacityActiveEvent.getAvailableCapacity()).isEqualTo(9);
        verify(eventRepository).save(capacityActiveEvent);
    }

    @Test
    void execute_existingRsvpNotFound_throwsRsvpNotFoundException() {
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(openActiveEvent));
        when(rsvpRepository.existsByEventIdAndStudentId(eventId, studentId)).thenReturn(true);
        when(rsvpRepository.findByEventIdAndStudentId(eventUUID, studentId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> createRsvpUseCase.execute(eventUUID, studentId))
                .isInstanceOf(RsvpNotFoundException.class)
                .hasMessageContaining("RSVP not found");
    }
}
