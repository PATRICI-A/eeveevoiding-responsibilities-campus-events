package edu.eci.patricia.DOWS_patricia.application.dto.response;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    private UUID id;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private Integer durationMinutes;
    private String location;
    private EventCategory category;
    private EventType type;
    private Integer maxCapacity;
    private Integer availableCapacity;
    private EventStatus status;
    private UUID organizerId;
    private String qrCode;
    private LocalDateTime createdAt;
}