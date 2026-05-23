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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "EventUpdateRequest",
        description = """
                Payload for updating an existing university event. All fields are required \
                (no partial updates). The same validation rules as event creation apply.
                
                **Authorization:** Only the original event organizer can update the event.
                """
)
public class EventUpdateRequest {

    @NotBlank(message = "Event name is required")
    @Size(max = 100, message = "Event name cannot exceed 100 characters")
    @Schema(
            description = "Updated title of the event.",
            example = "International Conference on AI - Extended Edition",
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(
            description = "Updated description of the event.",
            example = "Extended conference with additional workshops and networking sessions.",
            maxLength = 500
    )
    private String description;

    @NotNull(message = "Event date and time is required")
    @Future(message = "Event date must be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @Schema(
            description = "Updated event date. Must be in the future.",
            example = "2025-07-16",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate dateTime;

    @NotNull(message = "Event schedule is required")
    @Schema(
            description = "Updated start time in HH:mm format.",
            example = "15:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String startTime;

    @NotNull(message = "Duration is required")
    @Min(value = 15, message = "Duration must be at least 15 minutes")
    @Schema(
            description = "Updated duration in minutes.",
            example = "120",
            minimum = "15",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer duration;

    @NotBlank(message = "Location is required")
    @Schema(
            description = "Updated event location.",
            example = "Auditorio Principal, Edificio de Ingeniería, Piso 3",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String location;

    @NotNull(message = "Event category is required")
    @Schema(
            description = "Updated event category.",
            example = "ACADEMIC",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private EventCategory category;

    @NotNull(message = "Event type is required")
    @Schema(
            description = "Updated event type (OPEN or WITH_CAPACITY).",
            example = "WITH_CAPACITY",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private EventType type;

    @Schema(
            description = "Updated maximum capacity. Required if type is WITH_CAPACITY.",
            example = "150",
            minimum = "1"
    )
    private Integer maxCapacity;
}