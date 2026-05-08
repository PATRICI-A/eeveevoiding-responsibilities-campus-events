package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.mapper;

import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.OrganizerId;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity.EventEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventPersistenceMapperTest {

    private EventPersistenceMapper mapper;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        mapper = new EventPersistenceMapper();
        now = LocalDateTime.now();
    }

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        Event event = Event.builder()
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

        EventEntity entity = mapper.toEntity(event);

        assertNotNull(entity);
        assertEquals("event-id-123", entity.getId());
        assertEquals("Tech Talk", entity.getName());
        assertEquals("A tech event", entity.getDescription());
        assertEquals(now, entity.getDateTime());
        assertEquals("Room 101", entity.getLocation());
        assertEquals(EventCategory.CULTURAL, entity.getCategory());
        assertEquals(EventType.OPEN, entity.getType());
        assertEquals(100, entity.getMaxCapacity());
        assertEquals(50, entity.getAvailableSpots());
        assertEquals("organizer-id-456", entity.getOrganizerId());
        assertEquals(EventStatus.ACTIVE, entity.getStatus());
        assertEquals(now, entity.getCreatedAt());
    }

    @Test
    void toDomain_ShouldMapAllFieldsCorrectly() {
        EventEntity entity = EventEntity.builder()
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

        Event event = mapper.toDomain(entity);

        assertNotNull(event);
        assertEquals("event-id-123", event.getId().getValue());
        assertEquals("Tech Talk", event.getName());
        assertEquals("A tech event", event.getDescription());
        assertEquals(now, event.getDateTime());
        assertEquals("Room 101", event.getLocation());
        assertEquals(EventCategory.CULTURAL, event.getCategory());
        assertEquals(EventType.OPEN, event.getType());
        assertEquals(100, event.getMaxCapacity());
        assertEquals(50, event.getAvailableSpots());
        assertEquals("organizer-id-456", event.getOrganizerId().getValue());
        assertEquals(EventStatus.ACTIVE, event.getStatus());
        assertEquals(now, event.getCreatedAt());
    }

    @Test
    void toEntity_ThenToDomain_ShouldReturnEquivalentEvent() {
        Event original = Event.builder()
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

        Event result = mapper.toDomain(mapper.toEntity(original));

        assertEquals(original.getId().getValue(), result.getId().getValue());
        assertEquals(original.getName(), result.getName());
        assertEquals(original.getDescription(), result.getDescription());
        assertEquals(original.getDateTime(), result.getDateTime());
        assertEquals(original.getLocation(), result.getLocation());
        assertEquals(original.getCategory(), result.getCategory());
        assertEquals(original.getType(), result.getType());
        assertEquals(original.getMaxCapacity(), result.getMaxCapacity());
        assertEquals(original.getAvailableSpots(), result.getAvailableSpots());
        assertEquals(original.getOrganizerId().getValue(), result.getOrganizerId().getValue());
        assertEquals(original.getStatus(), result.getStatus());
        assertEquals(original.getCreatedAt(), result.getCreatedAt());
    }
}
