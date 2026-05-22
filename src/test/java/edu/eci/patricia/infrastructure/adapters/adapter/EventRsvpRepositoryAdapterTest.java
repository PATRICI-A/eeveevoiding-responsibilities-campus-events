package edu.eci.patricia.infrastructure.adapters.adapter;

import edu.eci.patricia.domain.model.EventRsvp;
import edu.eci.patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.domain.valueobjects.RsvpId;
import edu.eci.patricia.infrastructure.adapters.persistence.entity.EventRsvpEntity;
import edu.eci.patricia.infrastructure.adapters.persistence.mapper.EventRsvpPersistenceMapper;
import edu.eci.patricia.infrastructure.adapters.persistence.repository.EventRsvpMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRsvpRepositoryAdapterTest {

    @Mock
    private EventRsvpMongoRepository repository;

    @Mock
    private EventRsvpPersistenceMapper mapper;

    @InjectMocks
    private EventRsvpRepositoryAdapter adapter;

    private UUID rsvpUuid;
    private UUID eventUuid;
    private UUID studentUuid;
    private EventId eventId;
    private RsvpId rsvpId;
    private EventRsvp eventRsvp;
    private EventRsvpEntity eventRsvpEntity;

    @BeforeEach
    void setUp() {
        rsvpUuid = UUID.randomUUID();
        eventUuid = UUID.randomUUID();
        studentUuid = UUID.randomUUID();
        eventId = new EventId(eventUuid);
        rsvpId = new RsvpId(rsvpUuid);

        eventRsvp = EventRsvp.builder()
                .id(rsvpId)
                .eventId(eventId)
                .studentId(studentUuid)
                .status(RsvpStatus.CONFIRMED)
                .build();

        eventRsvpEntity = EventRsvpEntity.builder()
                .id(rsvpUuid)
                .eventId(eventUuid)
                .studentId(studentUuid)
                .status(RsvpStatus.CONFIRMED)
                .confirmedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void saveShouldReturnMappedRsvp() {
        when(mapper.toEntity(eventRsvp)).thenReturn(eventRsvpEntity);
        when(repository.save(eventRsvpEntity)).thenReturn(eventRsvpEntity);
        when(mapper.toModel(eventRsvpEntity)).thenReturn(eventRsvp);

        EventRsvp result = adapter.save(eventRsvp);

        assertNotNull(result);
        assertEquals(eventRsvp.getId(), result.getId());
        assertEquals(eventRsvp.getEventId(), result.getEventId());
        assertEquals(eventRsvp.getStudentId(), result.getStudentId());
        assertEquals(eventRsvp.getStatus(), result.getStatus());

        verify(mapper).toEntity(eventRsvp);
        verify(repository).save(eventRsvpEntity);
        verify(mapper).toModel(eventRsvpEntity);
    }

    @Test
    void existsByEventIdAndStudentIdShouldReturnTrueWhenExists() {
        when(repository.existsByEventIdAndStudentId(eventUuid, studentUuid)).thenReturn(true);

        boolean result = adapter.existsByEventIdAndStudentId(eventId, studentUuid);

        assertTrue(result);
        verify(repository).existsByEventIdAndStudentId(eventUuid, studentUuid);
    }

    @Test
    void existsByEventIdAndStudentIdShouldReturnFalseWhenNotExists() {
        when(repository.existsByEventIdAndStudentId(eventUuid, studentUuid)).thenReturn(false);

        boolean result = adapter.existsByEventIdAndStudentId(eventId, studentUuid);

        assertFalse(result);
        verify(repository).existsByEventIdAndStudentId(eventUuid, studentUuid);
    }

    @Test
    void findConfirmedByStudentIdShouldReturnListOfConfirmedRsvps() {
        List<EventRsvpEntity> entities = List.of(eventRsvpEntity);
        List<EventRsvp> expectedRsvps = List.of(eventRsvp);

        when(repository.findByStudentIdAndStatus(studentUuid, RsvpStatus.CONFIRMED))
                .thenReturn(entities);
        when(mapper.toModel(eventRsvpEntity)).thenReturn(eventRsvp);

        List<EventRsvp> result = adapter.findConfirmedByStudentId(studentUuid);

        assertEquals(1, result.size());
        assertEquals(expectedRsvps.size(), result.size());
        verify(repository).findByStudentIdAndStatus(studentUuid, RsvpStatus.CONFIRMED);
    }

    @Test
    void findConfirmedByStudentIdShouldReturnEmptyListWhenNoRsvps() {
        when(repository.findByStudentIdAndStatus(studentUuid, RsvpStatus.CONFIRMED))
                .thenReturn(List.of());

        List<EventRsvp> result = adapter.findConfirmedByStudentId(studentUuid);

        assertTrue(result.isEmpty());
        verify(repository).findByStudentIdAndStatus(studentUuid, RsvpStatus.CONFIRMED);
    }

    @Test
    void findConfirmedByEventIdShouldReturnListOfConfirmedRsvps() {
        List<EventRsvpEntity> entities = List.of(eventRsvpEntity);
        List<EventRsvp> expectedRsvps = List.of(eventRsvp);

        when(repository.findByEventIdAndStatus(eventUuid, RsvpStatus.CONFIRMED))
                .thenReturn(entities);
        when(mapper.toModel(eventRsvpEntity)).thenReturn(eventRsvp);

        List<EventRsvp> result = adapter.findConfirmedByEventId(eventId);

        assertEquals(1, result.size());
        verify(repository).findByEventIdAndStatus(eventUuid, RsvpStatus.CONFIRMED);
    }

    @Test
    void findConfirmedByEventIdShouldReturnEmptyListWhenNoRsvps() {
        when(repository.findByEventIdAndStatus(eventUuid, RsvpStatus.CONFIRMED))
                .thenReturn(List.of());

        List<EventRsvp> result = adapter.findConfirmedByEventId(eventId);

        assertTrue(result.isEmpty());
        verify(repository).findByEventIdAndStatus(eventUuid, RsvpStatus.CONFIRMED);
    }

    @Test
    void findByEventIdAndStudentIdShouldReturnRsvpWhenExists() {
        when(repository.findByEventIdAndStudentId(eventUuid, studentUuid))
                .thenReturn(Optional.of(eventRsvpEntity));
        when(mapper.toModel(eventRsvpEntity)).thenReturn(eventRsvp);

        Optional<EventRsvp> result = adapter.findByEventIdAndStudentId(eventUuid, studentUuid);

        assertTrue(result.isPresent());
        assertEquals(eventRsvp.getId(), result.get().getId());
        verify(repository).findByEventIdAndStudentId(eventUuid, studentUuid);
    }

    @Test
    void findByEventIdAndStudentIdShouldReturnEmptyWhenNotExists() {
        when(repository.findByEventIdAndStudentId(eventUuid, studentUuid))
                .thenReturn(Optional.empty());

        Optional<EventRsvp> result = adapter.findByEventIdAndStudentId(eventUuid, studentUuid);

        assertTrue(result.isEmpty());
        verify(repository).findByEventIdAndStudentId(eventUuid, studentUuid);
    }

    @Test
    void findByStudentIdShouldReturnListOfRsvps() {
        List<EventRsvpEntity> entities = List.of(eventRsvpEntity);
        List<EventRsvp> expectedRsvps = List.of(eventRsvp);

        when(repository.findByStudentId(studentUuid)).thenReturn(entities);
        when(mapper.toModel(eventRsvpEntity)).thenReturn(eventRsvp);

        List<EventRsvp> result = adapter.findByStudentId(studentUuid);

        assertEquals(1, result.size());
        verify(repository).findByStudentId(studentUuid);
    }

    @Test
    void findByStudentIdShouldReturnEmptyListWhenNoRsvps() {
        when(repository.findByStudentId(studentUuid)).thenReturn(List.of());

        List<EventRsvp> result = adapter.findByStudentId(studentUuid);

        assertTrue(result.isEmpty());
        verify(repository).findByStudentId(studentUuid);
    }

    @Test
    void saveShouldHandleRsvpWithNullId() {
        EventRsvp rsvpWithoutId = EventRsvp.builder()
                .id(null)
                .eventId(eventId)
                .studentId(studentUuid)
                .status(RsvpStatus.CANCELLED)
                .build();

        EventRsvpEntity entityWithoutId = EventRsvpEntity.builder()
                .id(null)
                .eventId(eventUuid)
                .studentId(studentUuid)
                .status(RsvpStatus.CANCELLED)
                .build();

        EventRsvp savedRsvp = EventRsvp.builder()
                .id(rsvpId)
                .eventId(eventId)
                .studentId(studentUuid)
                .status(RsvpStatus.CANCELLED)
                .build();

        when(mapper.toEntity(rsvpWithoutId)).thenReturn(entityWithoutId);
        when(repository.save(entityWithoutId)).thenReturn(eventRsvpEntity);
        when(mapper.toModel(eventRsvpEntity)).thenReturn(savedRsvp);

        EventRsvp result = adapter.save(rsvpWithoutId);

        assertNotNull(result);
        assertNotNull(result.getId());
        verify(mapper).toEntity(rsvpWithoutId);
        verify(repository).save(entityWithoutId);
        verify(mapper).toModel(eventRsvpEntity);
    }
}