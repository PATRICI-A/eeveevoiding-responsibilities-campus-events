package edu.eci.patricia.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "EventResponse",
        description = """
                Complete event details returned after creation or update operations.
                Contains all event metadata including QR code for attendance tracking.
                """
)
public class EventResponse {

    @Schema(
            description = "Unique identifier of the event",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID id;

    @Schema(
            description = "Title of the event",
            example = "International Conference on Artificial Intelligence"
    )
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @Schema(
            description = "Date of the event in yyyy-MM-dd format",
            example = "2025-07-15"
    )
    private LocalDate dateTime;

    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    @Schema(
            description = "Start time of the event in HH:mm format",
            example = "14:00"
    )
    private LocalTime startTime;

    @Schema(
            description = "Duration of the event in minutes",
            example = "90"
    )
    private Integer duration;

    @Schema(
            description = "Physical location or virtual meeting link",
            example = "Auditorio Principal, Edificio de Ingeniería"
    )
    private String location;

    @Schema(
            description = "Category of the event",
            example = "ACADEMIC"
    )
    private EventCategory category;

    @Schema(
            description = "Type of the event (OPEN or WITH_CAPACITY)",
            example = "WITH_CAPACITY"
    )
    private EventType type;

    @Schema(
            description = "Current status of the event (ACTIVE or CANCELLED)",
            example = "ACTIVE"
    )
    private EventStatus status;

    @Schema(
            description = "QR code image URL or base64 encoded data for attendance tracking",
            example = "data:image/png;base64,iVBORw0KGgoAAAANS..."
    )
    private String qrCode;
}