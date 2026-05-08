package edu.eci.patricia.DOWS_patricia.domain.valueobjects;

import edu.eci.patricia.DOWS_patricia.domain.exceptions.InvalidEventException;

public final class StudentId {
    private final String value;

    public StudentId(String value) {
        if (value == null || value.isBlank())
            throw new InvalidEventException("StudentId cannot be empty");
        this.value = value;
    }

    public String getValue() { return value; }
}