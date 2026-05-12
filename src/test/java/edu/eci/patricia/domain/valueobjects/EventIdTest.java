package edu.eci.patricia.domain.valueobjects;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventIdTest {

    @Test
    void shouldCreateEventIdWithGivenUUID() {
        UUID uuid = UUID.randomUUID();
        EventId eventId = new EventId(uuid);
        assertEquals(uuid, eventId.getValue());
    }

    @Test
    void shouldGenerateEventIdWithRandomUUID() {
        EventId eventId = EventId.generate();
        assertNotNull(eventId);
        assertNotNull(eventId.getValue());
    }

    @Test
    void shouldGenerateDifferentEventIdsEachTime() {
        EventId first = EventId.generate();
        EventId second = EventId.generate();
        assertNotEquals(first, second);
    }

    @Test
    void shouldBeEqualWhenSameUUID() {
        UUID uuid = UUID.randomUUID();
        EventId a = new EventId(uuid);
        EventId b = new EventId(uuid);
        assertEquals(a, b);
    }

    @Test
    void shouldNotBeEqualWhenDifferentUUID() {
        EventId a = new EventId(UUID.randomUUID());
        EventId b = new EventId(UUID.randomUUID());
        assertNotEquals(a, b);
    }

    @Test
    void shouldHaveSameHashCodeWhenSameUUID() {
        UUID uuid = UUID.randomUUID();
        EventId a = new EventId(uuid);
        EventId b = new EventId(uuid);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void shouldReturnUUIDStringOnToString() {
        UUID uuid = UUID.randomUUID();
        EventId eventId = new EventId(uuid);
        assertEquals(uuid.toString(), eventId.toString());
    }
}
