package edu.eci.patricia.DOWS_patricia.domain.valueobjects;

import edu.eci.patricia.DOWS_patricia.domain.exceptions.InvalidEventException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentIdTest {

    @Test
    void shouldCreateStudentIdWithValidValue() {
        StudentId studentId = new StudentId("student-001");
        assertEquals("student-001", studentId.getValue());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        assertThrows(InvalidEventException.class, () -> new StudentId(null));
    }

    @Test
    void shouldThrowExceptionWhenValueIsBlank() {
        assertThrows(InvalidEventException.class, () -> new StudentId("   "));
    }

    @Test
    void shouldThrowExceptionWhenValueIsEmpty() {
        assertThrows(InvalidEventException.class, () -> new StudentId(""));
    }

    @Test
    void shouldReturnCorrectValue() {
        StudentId studentId = new StudentId("student-abc-123");
        assertEquals("student-abc-123", studentId.getValue());
    }

    @Test
    void shouldCreateTwoStudentsWithDifferentValues() {
        StudentId first = new StudentId("student-001");
        StudentId second = new StudentId("student-002");
        assertNotEquals(first.getValue(), second.getValue());
    }
}
