package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.adapter;

import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity.EventEntity;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.mapper.EventPersistenceMapper;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.repository.EventMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRepositoryAdapterTest {

    @Mock
    private EventMongoRepository mongoRepository;

    @Mock
    private EventPersistenceMapper mapper;

    @InjectMocks
    private EventRepositoryAdapter adapter;

    private Event event;
    private EventEntity eventEntity;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        event = Event.builder()
                .id(new EventId("event-id-123"))
                .name("Tech Talk")
                .description("A tech event")
                .dateTime(now)
                .location("Room 101")
                .category(EventCategory.CULTURAL)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .availableSpots(50)
                .organizerId(new OrganizerId("organizer-id-456"))
                .status(EventStatus.ACTIVE)
                .createdAt(now)
                .build();

        eventEntity = EventEntity.builder()
                .id("event-id-123")
                .name("Tech Talk")
                .description("A tech event")
                .dateTime(now)
                .location("Room 101")
                .category(EventCategory.CULTURAL)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .availableSpots(50)
                .organizerId("organizer-id-456")
                .status(EventStatus.ACTIVE)
                .createdAt(now)
                .build();
    }

    @Test
    void save_ShouldReturnSavedEvent() {
        when(mapper.toEntity(event)).thenReturn(eventEntity);
        when(mongoRepository.save(eventEntity)).thenReturn(eventEntity);
        when(mapper.toDomain(eventEntity)).thenReturn(event);

        Event result = adapter.save(event);

        assertNotNull(result);
        assertEquals(event.getId().getValue(), result.getId().getValue());
        verify(mapper).toEntity(event);
        verify(mongoRepository).save(eventEntity);
        verify(mapper).toDomain(eventEntity);
    }

    @Test
    void findById_WhenExists_ShouldReturnEvent() {
        EventId eventId = new EventId("event-id-123");
        when(mongoRepository.findById("event-id-123")).thenReturn(Optional.of(eventEntity));
        when(mapper.toDomain(eventEntity)).thenReturn(event);

        Optional<Event> result = adapter.findById(eventId);

        assertTrue(result.isPresent());
        assertEquals("event-id-123", result.get().getId().getValue());
        verify(mongoRepository).findById("event-id-123");
        verify(mapper).toDomain(eventEntity);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        EventId eventId = new EventId("non-existent-id");
        when(mongoRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        Optional<Event> result = adapter.findById(eventId);

        assertFalse(result.isPresent());
        verify(mongoRepository).findById("non-existent-id");
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void findAll_ShouldReturnMappedEvents() {
        when(mongoRepository.findAll()).thenReturn(List.of(eventEntity));
        when(mapper.toDomain(eventEntity)).thenReturn(event);

        List<Event> result = adapter.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("event-id-123", result.get(0).getId().getValue());
        verify(mongoRepository).findAll();
        verify(mapper).toDomain(eventEntity);
    }

    @Test
    void findAll_WhenNoEvents_ShouldReturnEmptyList() {
        when(mongoRepository.findAll()).thenReturn(List.of());

        List<Event> result = adapter.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mongoRepository).findAll();
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void existsByName_WhenExists_ShouldReturnTrue() {
        when(mongoRepository.existsByName("Tech Talk")).thenReturn(true);

        boolean result = adapter.existsByName("Tech Talk");

        assertTrue(result);
        verify(mongoRepository).existsByName("Tech Talk");
    }

    @Test
    void existsByName_WhenNotExists_ShouldReturnFalse() {
        when(mongoRepository.existsByName("Unknown Event")).thenReturn(false);

        boolean result = adapter.existsByName("Unknown Event");

        assertFalse(result);
        verify(mongoRepository).existsByName("Unknown Event");
    }
}
