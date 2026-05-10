package edu.eci.patricia.DOWS_patricia.application.dto.request;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventUpdateRequest {
    @Size(max = 100, message = "Event name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Future(message = "Event date must be in the future")
    private LocalDateTime dateTime;

    private String location;

    private EventCategory category;

    private EventType type;

    @Min(value = 10, message = "Minimum capacity is 2 people")
    private Integer availableCapacity;
}