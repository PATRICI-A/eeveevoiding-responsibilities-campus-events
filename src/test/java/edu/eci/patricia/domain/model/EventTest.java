package edu.eci.patricia.domain.model;

import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.valueobjects.EventId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event buildFullEvent() {
        return Event.builder()
                .id(EventId.generate())
                .name("Tech Talk")
                .description("A talk about Java")
                .dateTime(LocalDate.of(2025, 6, 15))
                .startTime(LocalTime.of(10, 0))
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
    void shouldBuildEventWithAllFields() {
        Event event = buildFullEvent();
        assertNotNull(event);
        assertNotNull(event.getId());
        assertEquals("Tech Talk", event.getName());
        assertEquals("A talk about Java", event.getDescription());
        assertEquals(LocalDate.of(2025, 6, 15), event.getDateTime());
        assertEquals(LocalTime.of(10, 0), event.getStartTime());
        assertEquals(90, event.getDurationMinutes());
        assertEquals("Auditorio A", event.getLocation());
        assertEquals(EventCategory.ACADEMIC, event.getCategory());
        assertEquals(EventType.WITH_CAPACITY, event.getType());
        assertEquals(EventStatus.ACTIVE, event.getStatus());
        assertEquals(100, event.getMaxCapacity());
        assertEquals(50, event.getAvailableCapacity());
        assertNotNull(event.getOrganizerId());
        assertEquals("QR-ABC123", event.getQrCode());
    }

    @Test
    void shouldSetAndGetName() {
        Event event = buildFullEvent();
        event.setName("New Name");
        assertEquals("New Name", event.getName());
    }

    @Test
    void shouldSetAndGetDescription() {
        Event event = buildFullEvent();
        event.setDescription("Updated description");
        assertEquals("Updated description", event.getDescription());
    }

    @Test
    void shouldSetAndGetDateTime() {
        Event event = buildFullEvent();
        LocalDate newDate = LocalDate.of(2026, 1, 1);
        event.setDateTime(newDate);
        assertEquals(newDate, event.getDateTime());
    }

    @Test
    void shouldSetAndGetStartTime() {
        Event event = buildFullEvent();
        LocalTime newTime = LocalTime.of(14, 30);
        event.setStartTime(newTime);
        assertEquals(newTime, event.getStartTime());
    }

    @Test
    void shouldSetAndGetDurationMinutes() {
        Event event = buildFullEvent();
        event.setDurationMinutes(120);
        assertEquals(120, event.getDurationMinutes());
    }

    @Test
    void shouldSetAndGetLocation() {
        Event event = buildFullEvent();
        event.setLocation("Sala B");
        assertEquals("Sala B", event.getLocation());
    }

    @Test
    void shouldSetAndGetCategory() {
        Event event = buildFullEvent();
        event.setCategory(EventCategory.SPORTS);
        assertEquals(EventCategory.SPORTS, event.getCategory());
    }

    @Test
    void shouldSetAndGetType() {
        Event event = buildFullEvent();
        event.setType(EventType.OPEN);
        assertEquals(EventType.OPEN, event.getType());
    }

    @Test
    void shouldSetAndGetStatus() {
        Event event = buildFullEvent();
        event.setStatus(EventStatus.CANCELLED);
        assertEquals(EventStatus.CANCELLED, event.getStatus());
    }

    @Test
    void shouldSetAndGetMaxCapacity() {
        Event event = buildFullEvent();
        event.setMaxCapacity(200);
        assertEquals(200, event.getMaxCapacity());
    }

    @Test
    void shouldSetAndGetAvailableCapacity() {
        Event event = buildFullEvent();
        event.setAvailableCapacity(75);
        assertEquals(75, event.getAvailableCapacity());
    }

    @Test
    void shouldSetAndGetOrganizerId() {
        Event event = buildFullEvent();
        UUID newId = UUID.randomUUID();
        event.setOrganizerId(newId);
        assertEquals(newId, event.getOrganizerId());
    }

    @Test
    void shouldSetAndGetQrCode() {
        Event event = buildFullEvent();
        event.setQrCode("QR-XYZ");
        assertEquals("QR-XYZ", event.getQrCode());
    }

    @Test
    void shouldSetAndGetId() {
        Event event = buildFullEvent();
        EventId newId = EventId.generate();
        event.setId(newId);
        assertEquals(newId, event.getId());
    }

    @Test
    void shouldBuildEventWithNullOptionalFields() {
        Event event = Event.builder()
                .id(EventId.generate())
                .name("Minimal Event")
                .status(EventStatus.ACTIVE)
                .type(EventType.OPEN)
                .build();
        assertNotNull(event);
        assertNull(event.getDescription());
        assertNull(event.getDateTime());
        assertNull(event.getStartTime());
        assertNull(event.getDurationMinutes());
        assertNull(event.getLocation());
        assertNull(event.getCategory());
        assertNull(event.getMaxCapacity());
        assertNull(event.getAvailableCapacity());
        assertNull(event.getOrganizerId());
        assertNull(event.getQrCode());
    }

    @Test
    void shouldSupportAllEventCategories() {
        for (EventCategory category : EventCategory.values()) {
            Event event = buildFullEvent();
            event.setCategory(category);
            assertEquals(category, event.getCategory());
        }
    }

    @Test
    void shouldSupportAllEventStatuses() {
        for (EventStatus status : EventStatus.values()) {
            Event event = buildFullEvent();
            event.setStatus(status);
            assertEquals(status, event.getStatus());
        }
    }

    @Test
    void shouldSupportAllEventTypes() {
        for (EventType type : EventType.values()) {
            Event event = buildFullEvent();
            event.setType(type);
            assertEquals(type, event.getType());
        }
    }

    @Test
    void shouldCreateEventWithAllArgsConstructor() {
        EventId id = EventId.generate();
        UUID organizerId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2025, 8, 20);
        LocalTime time = LocalTime.of(9, 0);

        Event event = new Event(id, "Workshop", "Hands-on", date, time, 60,
                "Lab 3", EventCategory.WELLNESS, EventType.WITH_CAPACITY,
                EventStatus.ACTIVE, 30, 30, organizerId, "QR-001");

        assertEquals(id, event.getId());
        assertEquals("Workshop", event.getName());
        assertEquals("Hands-on", event.getDescription());
        assertEquals(date, event.getDateTime());
        assertEquals(time, event.getStartTime());
        assertEquals(60, event.getDurationMinutes());
        assertEquals("Lab 3", event.getLocation());
        assertEquals(EventCategory.WELLNESS, event.getCategory());
        assertEquals(EventType.WITH_CAPACITY, event.getType());
        assertEquals(EventStatus.ACTIVE, event.getStatus());
        assertEquals(30, event.getMaxCapacity());
        assertEquals(30, event.getAvailableCapacity());
        assertEquals(organizerId, event.getOrganizerId());
        assertEquals("QR-001", event.getQrCode());
    }
}
