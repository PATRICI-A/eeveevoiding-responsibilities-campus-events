package edu.eci.patricia.DOWS_patricia.application.dto.request;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.OrganizerId;
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
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private String location;
    private EventCategory category;
    private EventType type;
    private Integer maxCapacity;
    private String organizerId;
}