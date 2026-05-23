package edu.eci.patricia.domain.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RsvpAction",
        description = """
                Action to perform when managing event attendance via the RSVP endpoint.
                
                **Action descriptions:**
                - `CONFIRM` — Register attendance to the event (subject to capacity and event status)
                - `CANCEL` — Withdraw previously confirmed attendance
                
                **Note:** CANCEL only works if the student has an existing CONFIRMED RSVP.
                """
)
public enum RsvpAction {

    @Schema(description = "Register attendance to the event")
    CONFIRM,

    @Schema(description = "Withdraw previously confirmed attendance")
    CANCEL
}