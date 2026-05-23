package edu.eci.patricia.domain.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "EventType",
        description = """
                Determines how event capacity is handled and whether attendance limits apply.
                
                **Type descriptions:**
                - `OPEN` — Unlimited capacity; any number of students can RSVP
                - `WITH_CAPACITY` — Limited capacity; requires `maxCapacity` field; \
                  RSVPs are rejected once capacity is reached
                """
)
public enum EventType {

    @Schema(description = "Unlimited capacity — no attendance limit")
    OPEN,

    @Schema(description = "Limited capacity — requires maxCapacity field")
    WITH_CAPACITY
}