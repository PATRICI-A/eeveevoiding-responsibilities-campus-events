package edu.eci.patricia.infrastructure.adapters.adapter;

import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.infrastructure.adapters.persistence.entity.EventEntity;
import edu.eci.patricia.infrastructure.adapters.persistence.mapper.EventPersistenceMapper;
import edu.eci.patricia.infrastructure.adapters.persistence.repository.EventMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRepositoryAdapterTest {

    @Mock
    private EventMongoRepository repository;

    @Mock
    private EventPersistenceMapper mapper;

    @InjectMocks
    private EventRepositoryAdapter adapter;

    private UUID eventUuid;
    private EventId eventId;
    private Event event;
    private EventEntity eventEntity;

    @BeforeEach
    void setUp() {
        eventUuid = UUID.randomUUID();
        eventId = new EventId(eventUuid);

        event = Event.builder()
                .id(eventId)
                .name("Test Event")
                .description("Description")
                .dateTime(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .durationMinutes(60)
                .location("Test Location")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .status(EventStatus.ACTIVE)
                .maxCapacity(100)
                .availableCapacity(100)
                .organizerId(UUID.randomUUID())
                .qrCode("qr123")
                .build();

        eventEntity = EventEntity.builder()
                .id(eventUuid)
                .name("Test Event")
                .description("Description")
                .dateTime(LocalDate.now())
                .startTime("10:00")
                .durationMinutes(60)
                .location("Test Location")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .status(EventStatus.ACTIVE)
                .maxCapacity(100)
                .availableCapacity(100)
                .organizerId(event.getOrganizerId())
                .qrCode("qr123")
                .build();
    }

    @Test
    void saveShouldReturnMappedEvent() {
        when(mapper.toEntity(event)).thenReturn(eventEntity);
        when(repository.save(eventEntity)).thenReturn(eventEntity);
        when(mapper.toModel(eventEntity)).thenReturn(event);

        Event result = adapter.save(event);

        assertNotNull(result);
        assertEquals(event.getId(), result.getId());
        assertEquals(event.getName(), result.getName());
        verify(mapper).toEntity(event);
        verify(repository).save(eventEntity);
        verify(mapper).toModel(eventEntity);
    }

    @Test
    void findByIdShouldReturnEventWhenExists() {
        when(repository.findById(eventUuid)).thenReturn(Optional.of(eventEntity));
        when(mapper.toModel(eventEntity)).thenReturn(event);

        Optional<Event> result = adapter.findById(eventId);

        assertTrue(result.isPresent());
        assertEquals(event.getId(), result.get().getId());
        verify(repository).findById(eventUuid);
        verify(mapper).toModel(eventEntity);
    }

    @Test
    void findByIdShouldReturnEmptyWhenNotExists() {
        when(repository.findById(eventUuid)).thenReturn(Optional.empty());

        Optional<Event> result = adapter.findById(eventId);

        assertTrue(result.isEmpty());
        verify(repository).findById(eventUuid);
        verify(mapper, never()).toModel(any());
    }

    @Test
    void findActiveEventsWithCategoryAndDateShouldReturnFilteredEvents() {
        EventCategory category = EventCategory.ACADEMIC;
        LocalDate date = LocalDate.now();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<EventEntity> entities = List.of(eventEntity);
        List<Event> expectedEvents = List.of(event);

        when(repository.findByStatusAndCategoryAndDateTimeBetween(EventStatus.ACTIVE, category, start, end))
                .thenReturn(entities);
        when(mapper.toModel(eventEntity)).thenReturn(event);

        List<Event> result = adapter.findActiveEvents(category, date);

        assertEquals(1, result.size());
        assertEquals(expectedEvents.size(), result.size());
        verify(repository).findByStatusAndCategoryAndDateTimeBetween(EventStatus.ACTIVE, category, start, end);
    }

    @Test
    void findActiveEventsWithCategoryOnlyShouldReturnFilteredEvents() {
        EventCategory category = EventCategory.CULTURAL;

        List<EventEntity> entities = List.of(eventEntity);

        when(repository.findByStatusAndCategory(EventStatus.ACTIVE, category))
                .thenReturn(entities);
        when(mapper.toModel(eventEntity)).thenReturn(event);

        List<Event> result = adapter.findActiveEvents(category, null);

        assertEquals(1, result.size());
        verify(repository).findByStatusAndCategory(EventStatus.ACTIVE, category);
    }

    @Test
    void findActiveEventsWithDateOnlyShouldReturnFilteredEvents() {
        LocalDate date = LocalDate.now();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<EventEntity> entities = List.of(eventEntity);

        when(repository.findByStatusAndDateTimeBetween(EventStatus.ACTIVE, start, end))
                .thenReturn(entities);
        when(mapper.toModel(eventEntity)).thenReturn(event);

        List<Event> result = adapter.findActiveEvents(null, date);

        assertEquals(1, result.size());
        verify(repository).findByStatusAndDateTimeBetween(EventStatus.ACTIVE, start, end);
    }

    @Test
    void findActiveEventsWithNoFiltersShouldReturnAllActiveEvents() {
        List<EventEntity> entities = List.of(eventEntity);

        when(repository.findByStatus(EventStatus.ACTIVE))
                .thenReturn(entities);
        when(mapper.toModel(eventEntity)).thenReturn(event);

        List<Event> result = adapter.findActiveEvents(null, null);

        assertEquals(1, result.size());
        verify(repository).findByStatus(EventStatus.ACTIVE);
    }

    @Test
    void findActiveEventsShouldReturnEmptyListWhenNoEvents() {
        when(repository.findByStatus(EventStatus.ACTIVE)).thenReturn(List.of());

        List<Event> result = adapter.findActiveEvents(null, null);

        assertTrue(result.isEmpty());
        verify(repository).findByStatus(EventStatus.ACTIVE);
    }

    @Test
    void findByStatusShouldReturnEventsWithGivenStatus() {
        EventStatus status = EventStatus.CANCELLED;
        List<EventEntity> entities = List.of(eventEntity);
        List<Event> expectedEvents = List.of(event);

        when(repository.findByStatus(status)).thenReturn(entities);
        when(mapper.toModel(eventEntity)).thenReturn(event);

        List<Event> result = adapter.findByStatus(status);

        assertEquals(1, result.size());
        verify(repository).findByStatus(status);
    }

    @Test
    void findByStatusShouldReturnEmptyListWhenNoEvents() {
        EventStatus status = EventStatus.CANCELLED;

        when(repository.findByStatus(status)).thenReturn(List.of());

        List<Event> result = adapter.findByStatus(status);

        assertTrue(result.isEmpty());
        verify(repository).findByStatus(status);
    }

    @Test
    void existsByNameShouldReturnTrueWhenNameExists() {
        String name = "Test Event";

        when(repository.existsByName(name)).thenReturn(true);

        boolean result = adapter.existsByName(name);

        assertTrue(result);
        verify(repository).existsByName(name);
    }

    @Test
    void existsByNameShouldReturnFalseWhenNameNotExists() {
        String name = "Non Existent Event";

        when(repository.existsByName(name)).thenReturn(false);

        boolean result = adapter.existsByName(name);

        assertFalse(result);
        verify(repository).existsByName(name);
    }
}