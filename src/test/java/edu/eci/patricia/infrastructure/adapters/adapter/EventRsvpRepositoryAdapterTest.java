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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRsvpRepositoryAdapterTest {

    @Mock
    private EventRsvpMongoRepository repository;

    @Mock
    private EventRsvpPersistenceMapper mapper;

    @InjectMocks
    private EventRsvpRepositoryAdapter adapter;

    private UUID rsvpUUID;
    private UUID eventUUID;
    private UUID studentId;
    private EventId eventId;
    private RsvpId rsvpId;
    private EventRsvp eventRsvp;
    private EventRsvpEntity eventRsvpEntity;

    @BeforeEach
    void setUp() {
        rsvpUUID = UUID.randomUUID();
        eventUUID = UUID.randomUUID();
        studentId = UUID.randomUUID();
        eventId = new EventId(eventUUID);
        rsvpId = new RsvpId(rsvpUUID);
        eventRsvp = EventRsvp.builder()
                .id(rsvpId)
                .eventId(eventId)
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .build();
        eventRsvpEntity = EventRsvpEntity.builder()
                .id(rsvpUUID)
                .eventId(eventUUID)
                .studentId(studentId)
                .status(RsvpStatus.CONFIRMED)
                .build();
    }

    @Test
    void save_shouldReturnMappedModel() {
        when(mapper.toEntity(eventRsvp)).thenReturn(eventRsvpEntity);
        when(repository.save(eventRsvpEntity)).thenReturn(eventRsvpEntity);
        when(mapper.toModel(eventRsvpEntity)).thenReturn(eventRsvp);

        EventRsvp result = adapter.save(eventRsvp);

        assertNotNull(result);
        assertEquals(eventRsvp, result);
        verify(mapper).toEntity(eventRsvp);
        verify(repository).save(eventRsvpEntity);
        verify(mapper).toModel(eventRsvpEntity);
    }

    @Test
    void findByEventIdAndStudentId_shouldReturnMappedModel_whenExists() {
        when(repository.findByEventIdAndStudentId(eventUUID, studentId)).thenReturn(Optional.of(eventRsvpEntity));
        when(mapper.toModel(eventRsvpEntity)).thenReturn(eventRsvp);

        Optional<EventRsvp> result = adapter.findByEventIdAndStudentId(eventId, studentId);

        assertTrue(result.isPresent());
        assertEquals(eventRsvp, result.get());
        verify(repository).findByEventIdAndStudentId(eventUUID, studentId);
        verify(mapper).toModel(eventRsvpEntity);
    }

    @Test
    void findByEventIdAndStudentId_shouldReturnEmpty_whenNotFound() {
        when(repository.findByEventIdAndStudentId(eventUUID, studentId)).thenReturn(Optional.empty());

        Optional<EventRsvp> result = adapter.findByEventIdAndStudentId(eventId, studentId);

        assertFalse(result.isPresent());
        verify(repository).findByEventIdAndStudentId(eventUUID, studentId);
        verify(mapper, never()).toModel(any());
    }

    @Test
    void findConfirmedByStudentId_shouldReturnMappedList() {
        when(repository.findByStudentIdAndStatus(studentId, RsvpStatus.CONFIRMED)).thenReturn(List.of(eventRsvpEntity));
        when(mapper.toModel(eventRsvpEntity)).thenReturn(eventRsvp);

        List<EventRsvp> result = adapter.findConfirmedByStudentId(studentId);

        assertEquals(1, result.size());
        assertEquals(eventRsvp, result.get(0));
        verify(repository).findByStudentIdAndStatus(studentId, RsvpStatus.CONFIRMED);
    }

    @Test
    void findConfirmedByStudentId_shouldReturnEmptyList_whenNoneFound() {
        when(repository.findByStudentIdAndStatus(studentId, RsvpStatus.CONFIRMED)).thenReturn(List.of());

        List<EventRsvp> result = adapter.findConfirmedByStudentId(studentId);

        assertTrue(result.isEmpty());
        verify(repository).findByStudentIdAndStatus(studentId, RsvpStatus.CONFIRMED);
    }

    @Test
    void findConfirmedByEventId_shouldReturnMappedList() {
        when(repository.findByEventIdAndStatus(eventUUID, RsvpStatus.CONFIRMED)).thenReturn(List.of(eventRsvpEntity));
        when(mapper.toModel(eventRsvpEntity)).thenReturn(eventRsvp);

        List<EventRsvp> result = adapter.findConfirmedByEventId(eventId);

        assertEquals(1, result.size());
        assertEquals(eventRsvp, result.get(0));
        verify(repository).findByEventIdAndStatus(eventUUID, RsvpStatus.CONFIRMED);
    }

    @Test
    void findConfirmedByEventId_shouldReturnEmptyList_whenNoneFound() {
        when(repository.findByEventIdAndStatus(eventUUID, RsvpStatus.CONFIRMED)).thenReturn(List.of());

        List<EventRsvp> result = adapter.findConfirmedByEventId(eventId);

        assertTrue(result.isEmpty());
        verify(repository).findByEventIdAndStatus(eventUUID, RsvpStatus.CONFIRMED);
    }

    @Test
    void findConfirmedByStudentId_shouldPassConfirmedStatusToRepository() {
        when(repository.findByStudentIdAndStatus(any(), any())).thenReturn(List.of());

        adapter.findConfirmedByStudentId(studentId);

        verify(repository).findByStudentIdAndStatus(studentId, RsvpStatus.CONFIRMED);
        verify(repository, never()).findByStudentIdAndStatus(studentId, RsvpStatus.CANCELLED);
    }

    @Test
    void findConfirmedByEventId_shouldPassConfirmedStatusToRepository() {
        when(repository.findByEventIdAndStatus(any(), any())).thenReturn(List.of());

        adapter.findConfirmedByEventId(eventId);

        verify(repository).findByEventIdAndStatus(eventUUID, RsvpStatus.CONFIRMED);
        verify(repository, never()).findByEventIdAndStatus(eventUUID, RsvpStatus.CANCELLED);
    }
}
