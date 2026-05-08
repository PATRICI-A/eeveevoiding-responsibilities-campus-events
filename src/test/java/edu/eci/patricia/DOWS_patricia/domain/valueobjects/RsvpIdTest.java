package edu.eci.patricia.DOWS_patricia.domain.valueobjects;

import edu.eci.patricia.DOWS_patricia.domain.exceptions.InvalidEventException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RsvpIdTest {

    @Test
    void shouldCreateRsvpIdWithValidValue() {
        RsvpId rsvpId = new RsvpId("rsvp-001");
        assertEquals("rsvp-001", rsvpId.getValue());
    }

    @Test
    void shouldGenerateUniqueRsvpId() {
        RsvpId first = RsvpId.generate();
        RsvpId second = RsvpId.generate();
        assertNotNull(first.getValue());
        assertNotNull(second.getValue());
        assertNotEquals(first.getValue(), second.getValue());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        assertThrows(InvalidEventException.class, () -> new RsvpId(null));
    }

    @Test
    void shouldThrowExceptionWhenValueIsBlank() {
        assertThrows(InvalidEventException.class, () -> new RsvpId("   "));
    }

    @Test
    void shouldThrowExceptionWhenValueIsEmpty() {
        assertThrows(InvalidEventException.class, () -> new RsvpId(""));
    }

    @Test
    void shouldReturnCorrectValue() {
        RsvpId rsvpId = new RsvpId("rsvp-abc-123");
        assertEquals("rsvp-abc-123", rsvpId.getValue());
    }
}
