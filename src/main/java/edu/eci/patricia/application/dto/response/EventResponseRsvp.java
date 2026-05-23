package edu.eci.patricia.application.dto.response;

import edu.eci.patricia.domain.model.enums.RsvpStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "EventResponseRsvp",
        description = """
                RSVP record returned after confirming or cancelling attendance to an event.
                Contains the RSVP ID, event reference, student reference, and current status.
                """
)
public class EventResponseRsvp {

    @Schema(
            description = "Unique identifier of the RSVP record",
            example = "660e8400-e29b-41d4-a716-446655440001"
    )
    private UUID id;

    @Schema(
            description = "UUID of the event the student RSVP'd to",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID eventId;

    @Schema(
            description = "UUID of the student who made the RSVP",
            example = "770e8400-e29b-41d4-a716-446655440002"
    )
    private UUID studentId;

    @Schema(
            description = """
                    Current status of the RSVP:
                    - `CONFIRMED` — Student is registered to attend
                    - `CANCELLED` — Student has withdrawn attendance
                    """,
            example = "CONFIRMED"
    )
    private RsvpStatus status;
}