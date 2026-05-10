package edu.eci.patricia.DOWS_patricia.application.dto.request;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

    @NotBlank(message = "Event name cannot be empty")
    @Size(max = 100, message = "Event name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Event date and time is required")
    @Future(message = "Event date must be in the future")
    private LocalDateTime dateTime;

    @NotBlank(message = "Location cannot be empty")
    private String location;

    @NotNull(message = "Event category is required")
    private EventCategory category;

    @NotNull(message = "Event type is required")
    private EventType type;

    @Min(value = 10, message = "Minimum capacity is 2 people")
    private Integer availableCapacity;

    @NotNull(message = "Organizer id is required")
    private String organizerId;
}

