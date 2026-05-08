package edu.eci.patricia.DOWS_patricia.domain.valueobjects;

import edu.eci.patricia.DOWS_patricia.domain.exceptions.InvalidEventException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventIdTest {

    @Test
    void shouldCreateEventIdWithValidValue() {
        EventId eventId = new EventId("123e4567-e89b-12d3-a456-426614174000");
        assertEquals("123e4567-e89b-12d3-a456-426614174000", eventId.getValue());
    }

    @Test
    void shouldGenerateUniqueEventId() {
        EventId first = EventId.generate();
        EventId second = EventId.generate();
        assertNotNull(first.getValue());
        assertNotNull(second.getValue());
        assertNotEquals(first.getValue(), second.getValue());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        assertThrows(InvalidEventException.class, () -> new EventId(null));
    }

    @Test
    void shouldThrowExceptionWhenValueIsBlank() {
        assertThrows(InvalidEventException.class, () -> new EventId("   "));
    }

    @Test
    void shouldThrowExceptionWhenValueIsEmpty() {
        assertThrows(InvalidEventException.class, () -> new EventId(""));
    }

    @Test
    void shouldReturnCorrectValue() {
        EventId eventId = new EventId("test-id-123");
        assertEquals("test-id-123", eventId.getValue());
    }
}
