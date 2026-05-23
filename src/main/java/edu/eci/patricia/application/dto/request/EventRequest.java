package edu.eci.patricia.application.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "EventRequest",
        description = """
                Payload for creating a new university event. All fields except `maxCapacity` are required.
                
                **Validation rules:**
                - Event name: required, max 100 characters
                - Event date: must be in the future (cannot be today or earlier)
                - Duration: minimum 15 minutes
                - Location: required
                - Category and Type: must be valid enum values
                - maxCapacity: optional, if provided must be ≥ 1
                
                **Note:** This endpoint is only accessible to users with ORGANIZADOR role.
                """
)
public class EventRequest {

    @NotBlank(message = "Event name is required")
    @Size(max = 100, message = "Event name cannot exceed 100 characters")
    @Schema(
            description = "Title of the event. Must be descriptive and unique within the same date.",
            example = "International Conference on Artificial Intelligence",
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(
            description = "Detailed description of the event content, agenda, and purpose.",
            example = "Annual AI conference featuring keynote speakers from industry and academia, panel discussions, and networking sessions.",
            maxLength = 500
    )
    private String description;

    @NotNull(message = "Event date and time is required")
    @Future(message = "Event date must be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @Schema(
            description = "Date of the event in ISO-8601 format (yyyy-MM-dd). Must be in the future.",
            example = "2025-07-15",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate dateTime;

    @NotNull(message = "Event schedule is required")
    @Schema(
            description = "Start time of the event in HH:mm format (24-hour).",
            example = "14:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String startTime;

    @NotNull(message = "Duration is required")
    @Min(value = 15, message = "Duration must be at least 15 minutes")
    @Schema(
            description = "Duration of the event in minutes. Minimum 15 minutes.",
            example = "90",
            minimum = "15",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer duration;

    @NotBlank(message = "Location is required")
    @Schema(
            description = "Physical location or virtual meeting link for the event.",
            example = "Auditorio Principal, Edificio de Ingeniería, Piso 3",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String location;

    @NotNull(message = "Event category is required")
    @Schema(
            description = """
                    Category of the event. Determines visibility in filtered feeds and recommendation algorithms.
                    Valid values: `ACADEMIC`, `CULTURAL`, `SPORTS`, `WELLNESS`
                    """,
            example = "ACADEMIC",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private EventCategory category;

    @NotNull(message = "Event type is required")
    @Schema(
            description = """
                    Type of the event. Determines capacity handling:
                    - `OPEN` — Unlimited capacity, no registration limit
                    - `WITH_CAPACITY` — Limited capacity, requires `maxCapacity` field
                    """,
            example = "WITH_CAPACITY",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private EventType type;

    @Schema(
            description = """
                    Maximum number of students who can attend. Required when `type = WITH_CAPACITY`.
                    Must be ≥ 1. When not provided or `OPEN` type, capacity is unlimited.
                    """,
            example = "100",
            minimum = "1"
    )
    private Integer maxCapacity;
}