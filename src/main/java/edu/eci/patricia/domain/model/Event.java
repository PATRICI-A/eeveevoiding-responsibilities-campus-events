package edu.eci.patricia.domain.model;


import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.valueobjects.EventId;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Event {

    private EventId id;
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
    private LocalDateTime updatedAt;
}
