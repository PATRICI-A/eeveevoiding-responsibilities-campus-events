package edu.eci.patricia.DOWS_patricia.domain.valueobjects;

public final class OrganizerId {
    private final String value;

    public OrganizerId(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("OrganizerId cannot be empty");
        this.value = value;
    }

    public String getValue() { return value; }
}