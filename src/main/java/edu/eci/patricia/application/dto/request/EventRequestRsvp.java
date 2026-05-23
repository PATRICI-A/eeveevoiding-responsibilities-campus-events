package edu.eci.patricia.application.dto.request;

import edu.eci.patricia.domain.model.enums.RsvpAction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "EventRequestRsvp",
        description = """
                Payload for confirming or cancelling attendance to an event.
                The action determines whether the student registers for or withdraws from the event.
                """
)
public class EventRequestRsvp {

    @NotNull(message = "Action is required")
    @Schema(
            description = """
                    Action to perform:
                    - `CONFIRM` — Register attendance to the event (subject to capacity and event status)
                    - `CANCEL` — Withdraw previously confirmed attendance
                    """,
            example = "CONFIRM",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private RsvpAction action;
}