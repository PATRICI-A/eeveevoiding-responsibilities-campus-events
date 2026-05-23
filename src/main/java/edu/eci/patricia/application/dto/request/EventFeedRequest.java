package edu.eci.patricia.application.dto.request;

import edu.eci.patricia.domain.model.enums.EventCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "EventFeedRequest",
        description = """
                Optional filters for retrieving the public event feed. Both filters are optional \
                and can be combined. If no filters are provided, all upcoming active events are returned.
                """
)
public class EventFeedRequest {

    @Schema(
            description = """
                    Filter events by category. Valid values:
                    - `ACADEMIC` — Lectures, conferences, academic workshops
                    - `CULTURAL` — Concerts, art exhibitions, cultural festivals
                    - `SPORTS` — Tournaments, training sessions, sports events
                    - `WELLNESS` — Yoga, meditation, mental health activities
                    """,
            example = "ACADEMIC"
    )
    private EventCategory category;

    @FutureOrPresent(message = "Date filter cannot be in the past")
    @Schema(
            description = "Filter events by specific date in ISO-8601 format (yyyy-MM-dd). Cannot be in the past.",
            example = "2025-07-15"
    )
    private LocalDate date;
}