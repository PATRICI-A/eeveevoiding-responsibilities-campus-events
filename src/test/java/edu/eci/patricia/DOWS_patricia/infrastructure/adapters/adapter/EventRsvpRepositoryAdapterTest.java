package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.adapter;

import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.StudentId;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity.EventRsvpEntity;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.mapper.EventRsvpPersistenceMapper;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.repository.EventRsvpMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRsvpRepositoryAdapterTest {

    @Mock
    private EventRsvpMongoRepository mongoRepository;

    @Mock
    private EventRsvpPersistenceMapper mapper;

    @InjectMocks
    private EventRsvpRepositoryAdapter adapter;

    private EventRsvp rsvp;
    private EventRsvpEntity rsvpEntity;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        rsvp = EventRsvp.builder()
                .id(new RsvpId("rsvp-id-123"))
                .eventId(new EventId("event-id-456"))
                .studentId(new StudentId("student-id-789"))
                .confirmedAt(now)
                .status(RsvpStatus.CONFIRMED)
                .build();

        rsvpEntity = EventRsvpEntity.builder()
                .id("rsvp-id-123")
                .eventId("event-id-456")
                .studentId("student-id-789")
                .confirmedAt(now)
                .status(RsvpStatus.CONFIRMED)
                .build();
    }

    @Test
    void save_ShouldReturnSavedRsvp() {
        when(mapper.toEntity(rsvp)).thenReturn(rsvpEntity);
        when(mongoRepository.save(rsvpEntity)).thenReturn(rsvpEntity);
        when(mapper.toDomain(rsvpEntity)).thenReturn(rsvp);

        EventRsvp result = adapter.save(rsvp);

        assertNotNull(result);
        assertEquals(rsvp.getId().getValue(), result.getId().getValue());
        verify(mapper).toEntity(rsvp);
        verify(mongoRepository).save(rsvpEntity);
        verify(mapper).toDomain(rsvpEntity);
    }

    @Test
    void findById_WhenExists_ShouldReturnRsvp() {
        RsvpId rsvpId = new RsvpId("rsvp-id-123");
        when(mongoRepository.findById("rsvp-id-123")).thenReturn(Optional.of(rsvpEntity));
        when(mapper.toDomain(rsvpEntity)).thenReturn(rsvp);

        Optional<EventRsvp> result = adapter.findById(rsvpId);

        assertTrue(result.isPresent());
        assertEquals("rsvp-id-123", result.get().getId().getValue());
        verify(mongoRepository).findById("rsvp-id-123");
        verify(mapper).toDomain(rsvpEntity);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        RsvpId rsvpId = new RsvpId("non-existent-id");
        when(mongoRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        Optional<EventRsvp> result = adapter.findById(rsvpId);

        assertFalse(result.isPresent());
        verify(mongoRepository).findById("non-existent-id");
        verify(mapper, never()).toDomain(any());
    }
}
