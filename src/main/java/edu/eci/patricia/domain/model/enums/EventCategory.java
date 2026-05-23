package edu.eci.patricia.domain.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "EventCategory",
        description = """
                Categories for classifying university events. Used for filtering the event feed \
                and for recommendation algorithms.
                
                **Category descriptions:**
                - `ACADEMIC` — Lectures, conferences, academic workshops, seminars
                - `CULTURAL` — Concerts, art exhibitions, cultural festivals, theatre
                - `SPORTS` — Tournaments, training sessions, sports events, fitness classes
                - `WELLNESS` — Yoga, meditation, mental health activities, wellness workshops
                """
)
public enum EventCategory {

    @Schema(description = "Academic events: lectures, conferences, workshops")
    ACADEMIC,

    @Schema(description = "Cultural events: concerts, exhibitions, festivals")
    CULTURAL,

    @Schema(description = "Sports events: tournaments, training, fitness")
    SPORTS,

    @Schema(description = "Wellness events: yoga, meditation, mental health")
    WELLNESS
}