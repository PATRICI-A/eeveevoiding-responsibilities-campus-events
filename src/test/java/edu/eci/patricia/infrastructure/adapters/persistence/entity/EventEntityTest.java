package edu.eci.patricia.infrastructure.adapters.persistence.entity;

import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventEntityTest {

    private EventEntity buildFullEntity() {
        return EventEntity.builder()
                .id(UUID.randomUUID())
                .name("Tech Talk")
                .description("A talk about Java")
                .dateTime(LocalDate.of(2025, 6, 15))
                .startTime("10:00")
                .durationMinutes(90)
                .location("Auditorio A")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .status(EventStatus.ACTIVE)
                .maxCapacity(100)
                .availableCapacity(50)
                .organizerId(UUID.randomUUID())
                .qrCode("QR-ABC123")
                .build();
    }

    @Test
    void shouldBuildEventEntityWithAllFields() {
        EventEntity entity = buildFullEntity();
        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertEquals("Tech Talk", entity.getName());
        assertEquals("A talk about Java", entity.getDescription());
        assertEquals(LocalDate.of(2025, 6, 15), entity.getDateTime());
        assertEquals("10:00", entity.getStartTime());
        assertEquals(90, entity.getDurationMinutes());
        assertEquals("Auditorio A", entity.getLocation());
        assertEquals(EventCategory.ACADEMIC, entity.getCategory());
        assertEquals(EventType.WITH_CAPACITY, entity.getType());
        assertEquals(EventStatus.ACTIVE, entity.getStatus());
        assertEquals(100, entity.getMaxCapacity());
        assertEquals(50, entity.getAvailableCapacity());
        assertNotNull(entity.getOrganizerId());
        assertEquals("QR-ABC123", entity.getQrCode());
    }

    @Test
    void shouldCreateEventEntityWithNoArgsConstructor() {
        EventEntity entity = new EventEntity();
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getDescription());
        assertNull(entity.getDateTime());
        assertNull(entity.getStartTime());
        assertNull(entity.getDurationMinutes());
        assertNull(entity.getLocation());
        assertNull(entity.getCategory());
        assertNull(entity.getType());
        assertNull(entity.getStatus());
        assertNull(entity.getMaxCapacity());
        assertNull(entity.getAvailableCapacity());
        assertNull(entity.getOrganizerId());
        assertNull(entity.getQrCode());
    }

    @Test
    void shouldCreateEventEntityWithAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        UUID organizerId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2025, 8, 20);

        EventEntity entity = new EventEntity(id, "Workshop", "Hands-on", date, "09:00",
                60, "Lab 3", EventCategory.WELLNESS, EventType.WITH_CAPACITY,
                EventStatus.ACTIVE, 30, 30, organizerId, "QR-001");

        assertEquals(id, entity.getId());
        assertEquals("Workshop", entity.getName());
        assertEquals("Hands-on", entity.getDescription());
        assertEquals(date, entity.getDateTime());
        assertEquals("09:00", entity.getStartTime());
        assertEquals(60, entity.getDurationMinutes());
        assertEquals("Lab 3", entity.getLocation());
        assertEquals(EventCategory.WELLNESS, entity.getCategory());
        assertEquals(EventType.WITH_CAPACITY, entity.getType());
        assertEquals(EventStatus.ACTIVE, entity.getStatus());
        assertEquals(30, entity.getMaxCapacity());
        assertEquals(30, entity.getAvailableCapacity());
        assertEquals(organizerId, entity.getOrganizerId());
        assertEquals("QR-001", entity.getQrCode());
    }

    @Test
    void shouldSetAndGetId() {
        EventEntity entity = buildFullEntity();
        UUID newId = UUID.randomUUID();
        entity.setId(newId);
        assertEquals(newId, entity.getId());
    }

    @Test
    void shouldSetAndGetName() {
        EventEntity entity = buildFullEntity();
        entity.setName("Updated Name");
        assertEquals("Updated Name", entity.getName());
    }

    @Test
    void shouldSetAndGetDescription() {
        EventEntity entity = buildFullEntity();
        entity.setDescription("Updated description");
        assertEquals("Updated description", entity.getDescription());
    }

    @Test
    void shouldSetAndGetDateTime() {
        EventEntity entity = buildFullEntity();
        LocalDate newDate = LocalDate.of(2026, 3, 10);
        entity.setDateTime(newDate);
        assertEquals(newDate, entity.getDateTime());
    }

    @Test
    void shouldSetAndGetStartTime() {
        EventEntity entity = buildFullEntity();
        entity.setStartTime("14:30");
        assertEquals("14:30", entity.getStartTime());
    }

    @Test
    void shouldSetAndGetDurationMinutes() {
        EventEntity entity = buildFullEntity();
        entity.setDurationMinutes(120);
        assertEquals(120, entity.getDurationMinutes());
    }

    @Test
    void shouldSetAndGetLocation() {
        EventEntity entity = buildFullEntity();
        entity.setLocation("Sala B");
        assertEquals("Sala B", entity.getLocation());
    }

    @Test
    void shouldSetAndGetCategory() {
        EventEntity entity = buildFullEntity();
        entity.setCategory(EventCategory.CULTURAL);
        assertEquals(EventCategory.CULTURAL, entity.getCategory());
    }

    @Test
    void shouldSetAndGetType() {
        EventEntity entity = buildFullEntity();
        entity.setType(EventType.OPEN);
        assertEquals(EventType.OPEN, entity.getType());
    }

    @Test
    void shouldSetAndGetStatus() {
        EventEntity entity = buildFullEntity();
        entity.setStatus(EventStatus.CANCELLED);
        assertEquals(EventStatus.CANCELLED, entity.getStatus());
    }

    @Test
    void shouldSetAndGetMaxCapacity() {
        EventEntity entity = buildFullEntity();
        entity.setMaxCapacity(200);
        assertEquals(200, entity.getMaxCapacity());
    }

    @Test
    void shouldSetAndGetAvailableCapacity() {
        EventEntity entity = buildFullEntity();
        entity.setAvailableCapacity(75);
        assertEquals(75, entity.getAvailableCapacity());
    }

    @Test
    void shouldSetAndGetOrganizerId() {
        EventEntity entity = buildFullEntity();
        UUID newId = UUID.randomUUID();
        entity.setOrganizerId(newId);
        assertEquals(newId, entity.getOrganizerId());
    }

    @Test
    void shouldSetAndGetQrCode() {
        EventEntity entity = buildFullEntity();
        entity.setQrCode("QR-NEW");
        assertEquals("QR-NEW", entity.getQrCode());
    }

    @Test
    void shouldBeEqualWhenSameFields() {
        UUID id = UUID.randomUUID();
        UUID organizerId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2025, 6, 15);

        EventEntity a = EventEntity.builder().id(id).name("Event").dateTime(date)
                .organizerId(organizerId).build();
        EventEntity b = EventEntity.builder().id(id).name("Event").dateTime(date)
                .organizerId(organizerId).build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        EventEntity a = buildFullEntity();
        EventEntity b = buildFullEntity();
        assertNotEquals(a, b);
    }

    @Test
    void shouldReturnToStringWithClassName() {
        EventEntity entity = buildFullEntity();
        String toString = entity.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("EventEntity"));
    }

    @Test
    void shouldSupportAllEventCategories() {
        for (EventCategory category : EventCategory.values()) {
            EventEntity entity = buildFullEntity();
            entity.setCategory(category);
            assertEquals(category, entity.getCategory());
        }
    }

    @Test
    void shouldSupportAllEventStatuses() {
        for (EventStatus status : EventStatus.values()) {
            EventEntity entity = buildFullEntity();
            entity.setStatus(status);
            assertEquals(status, entity.getStatus());
        }
    }

    @Test
    void shouldSupportAllEventTypes() {
        for (EventType type : EventType.values()) {
            EventEntity entity = buildFullEntity();
            entity.setType(type);
            assertEquals(type, entity.getType());
        }
    }

    @Test
    void shouldBuildWithNullOptionalFields() {
        EventEntity entity = EventEntity.builder()
                .id(UUID.randomUUID())
                .name("Minimal")
                .build();
        assertNotNull(entity);
        assertNull(entity.getDescription());
        assertNull(entity.getDateTime());
        assertNull(entity.getStartTime());
        assertNull(entity.getDurationMinutes());
        assertNull(entity.getLocation());
        assertNull(entity.getCategory());
        assertNull(entity.getType());
        assertNull(entity.getStatus());
        assertNull(entity.getMaxCapacity());
        assertNull(entity.getAvailableCapacity());
        assertNull(entity.getOrganizerId());
        assertNull(entity.getQrCode());
    }
}
