package edu.eci.patricia.domain.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "EventStatus",
        description = """
                Current lifecycle state of an event. Determines visibility in the public feed \
                and whether students can RSVP.
                
                **State descriptions:**
                - `ACTIVE` — Event is published and visible; students can RSVP
                - `CANCELLED` — Event has been cancelled; not visible in feed; no new RSVPs
                
                **Note:** Once cancelled, an event cannot be reactivated.
                """
)
public enum EventStatus {

    @Schema(description = "Event is active and accepting RSVPs")
    ACTIVE,

    @Schema(description = "Event has been cancelled; not accepting RSVPs")
    CANCELLED
}