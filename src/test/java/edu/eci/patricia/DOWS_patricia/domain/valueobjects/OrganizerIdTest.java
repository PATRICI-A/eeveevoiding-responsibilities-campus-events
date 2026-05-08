package edu.eci.patricia.DOWS_patricia.domain.valueobjects;

import edu.eci.patricia.DOWS_patricia.domain.exceptions.InvalidEventException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrganizerIdTest {

    @Test
    void shouldCreateOrganizerIdWithValidValue() {
        OrganizerId organizerId = new OrganizerId("org-001");
        assertEquals("org-001", organizerId.getValue());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        assertThrows(InvalidEventException.class, () -> new OrganizerId(null));
    }

    @Test
    void shouldThrowExceptionWhenValueIsBlank() {
        assertThrows(InvalidEventException.class, () -> new OrganizerId("   "));
    }

    @Test
    void shouldThrowExceptionWhenValueIsEmpty() {
        assertThrows(InvalidEventException.class, () -> new OrganizerId(""));
    }

    @Test
    void shouldReturnCorrectValue() {
        OrganizerId organizerId = new OrganizerId("org-abc-123");
        assertEquals("org-abc-123", organizerId.getValue());
    }

    @Test
    void shouldCreateTwoOrganizersWithDifferentValues() {
        OrganizerId first = new OrganizerId("org-001");
        OrganizerId second = new OrganizerId("org-002");
        assertNotEquals(first.getValue(), second.getValue());
    }
}
