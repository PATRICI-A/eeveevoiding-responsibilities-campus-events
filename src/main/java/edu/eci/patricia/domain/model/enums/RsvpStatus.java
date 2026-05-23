package edu.eci.patricia.domain.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RsvpStatus",
        description = """
                Current state of a student's RSVP for a specific event.
                
                **Status descriptions:**
                - `CONFIRMED` — Student is registered to attend the event
                - `CANCELLED` — Student has withdrawn attendance
                
                **Note:** Students cannot have both CONFIRMED and CANCELLED RSVPs for the same event.
                """
)
public enum RsvpStatus {

    @Schema(description = "Student is registered to attend")
    CONFIRMED,

    @Schema(description = "Student has withdrawn attendance")
    CANCELLED
}