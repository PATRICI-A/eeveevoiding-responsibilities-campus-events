package edu.eci.patricia.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RsvpIdTest {

    @Test
    void shouldCreateRsvpIdWithGivenUUID() {
        UUID uuid = UUID.randomUUID();
        RsvpId rsvpId = new RsvpId(uuid);
        assertEquals(uuid, rsvpId.getValue());
    }

    @Test
    void shouldGenerateRsvpIdWithRandomUUID() {
        RsvpId rsvpId = RsvpId.generate();
        assertNotNull(rsvpId);
        assertNotNull(rsvpId.getValue());
    }

    @Test
    void shouldGenerateUniqueRsvpIds() {
        RsvpId first = RsvpId.generate();
        RsvpId second = RsvpId.generate();
        assertNotEquals(first, second);
    }

    @Test
    void shouldBeEqualWhenSameUUID() {
        UUID uuid = UUID.randomUUID();
        RsvpId a = new RsvpId(uuid);
        RsvpId b = new RsvpId(uuid);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentUUID() {
        RsvpId a = new RsvpId(UUID.randomUUID());
        RsvpId b = new RsvpId(UUID.randomUUID());
        assertNotEquals(a, b);
    }

    @Test
    void shouldReturnUUIDStringOnToString() {
        UUID uuid = UUID.randomUUID();
        RsvpId rsvpId = new RsvpId(uuid);
        assertEquals(uuid.toString(), rsvpId.toString());
    }

    @Test
    void shouldNotBeEqualToNull() {
        RsvpId rsvpId = RsvpId.generate();
        assertNotEquals(null, rsvpId);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        RsvpId rsvpId = RsvpId.generate();
        assertNotEquals("someString", rsvpId);
    }
}
