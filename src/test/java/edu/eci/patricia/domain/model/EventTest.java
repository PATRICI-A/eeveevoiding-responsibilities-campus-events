package edu.eci.patricia.domain.model;

import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.valueobjects.EventId;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event buildDefaultEvent() {
        return Event.builder()
                .id(EventId.generate())
                .name("Tech Talk")
                .description("An academic event")
                .dateTime(LocalDateTime.of(2025, 6, 15, 10, 0))
                .durationMinutes(90)
                .location("Auditorium A")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .maxCapacity(100)
                .availableCapacity(100)
                .status(EventStatus.ACTIVE)
                .organizerId(UUID.randomUUID())
                .qrCode("QR-ABC123")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldBuildEventWithAllFields() {
        Event event = buildDefaultEvent();
        assertNotNull(event);
        assertNotNull(event.getId());
        assertEquals("Tech Talk", event.getName());
        assertEquals("An academic event", event.getDescription());
        assertEquals(90, event.getDurationMinutes());
        assertEquals("Auditorium A", event.getLocation());
        assertEquals(EventCategory.ACADEMIC, event.getCategory());
        assertEquals(EventType.WITH_CAPACITY, event.getType());
        assertEquals(100, event.getMaxCapacity());
        assertEquals(100, event.getAvailableCapacity());
        assertEquals(EventStatus.ACTIVE, event.getStatus());
        assertNotNull(event.getOrganizerId());
        assertEquals("QR-ABC123", event.getQrCode());
        assertNotNull(event.getCreatedAt());
        assertNotNull(event.getUpdatedAt());
    }

    @Test
    void shouldAllowSettingName() {
        Event event = buildDefaultEvent();
        event.setName("New Name");
        assertEquals("New Name", event.getName());
    }

    @Test
    void shouldAllowSettingDescription() {
        Event event = buildDefaultEvent();
        event.setDescription("Updated description");
        assertEquals("Updated description", event.getDescription());
    }

    @Test
    void shouldAllowSettingStatus() {
        Event event = buildDefaultEvent();
        event.setStatus(EventStatus.CANCELLED);
        assertEquals(EventStatus.CANCELLED, event.getStatus());
    }

    @Test
    void shouldAllowSettingAvailableCapacity() {
        Event event = buildDefaultEvent();
        event.setAvailableCapacity(50);
        assertEquals(50, event.getAvailableCapacity());
    }

    @Test
    void shouldAllowSettingCategory() {
        Event event = buildDefaultEvent();
        event.setCategory(EventCategory.SPORTS);
        assertEquals(EventCategory.SPORTS, event.getCategory());
    }

    @Test
    void shouldAllowSettingType() {
        Event event = buildDefaultEvent();
        event.setType(EventType.OPEN);
        assertEquals(EventType.OPEN, event.getType());
    }

    @Test
    void shouldAllowSettingLocation() {
        Event event = buildDefaultEvent();
        event.setLocation("Room 202");
        assertEquals("Room 202", event.getLocation());
    }

    @Test
    void shouldAllowSettingDateTime() {
        Event event = buildDefaultEvent();
        LocalDateTime newDate = LocalDateTime.of(2025, 12, 1, 9, 0);
        event.setDateTime(newDate);
        assertEquals(newDate, event.getDateTime());
    }

    @Test
    void shouldAllowSettingQrCode() {
        Event event = buildDefaultEvent();
        event.setQrCode("QR-XYZ999");
        assertEquals("QR-XYZ999", event.getQrCode());
    }

    @Test
    void shouldAllowSettingOrganizerId() {
        Event event = buildDefaultEvent();
        UUID newOrganizer = UUID.randomUUID();
        event.setOrganizerId(newOrganizer);
        assertEquals(newOrganizer, event.getOrganizerId());
    }

    @Test
    void shouldAllowSettingUpdatedAt() {
        Event event = buildDefaultEvent();
        LocalDateTime updatedAt = LocalDateTime.of(2025, 7, 1, 12, 0);
        event.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, event.getUpdatedAt());
    }

    @Test
    void shouldSupportOpenEventTypeWithoutCapacity() {
        Event event = Event.builder()
                .id(EventId.generate())
                .name("Open Seminar")
                .type(EventType.OPEN)
                .status(EventStatus.ACTIVE)
                .category(EventCategory.CULTURAL)
                .organizerId(UUID.randomUUID())
                .dateTime(LocalDateTime.now())
                .build();

        assertEquals(EventType.OPEN, event.getType());
        assertNull(event.getMaxCapacity());
    }

    @Test
    void shouldSupportAllEventCategories() {
        for (EventCategory category : EventCategory.values()) {
            Event event = Event.builder()
                    .id(EventId.generate())
                    .name("Event")
                    .category(category)
                    .status(EventStatus.ACTIVE)
                    .type(EventType.OPEN)
                    .organizerId(UUID.randomUUID())
                    .dateTime(LocalDateTime.now())
                    .build();
            assertEquals(category, event.getCategory());
        }
    }
}
