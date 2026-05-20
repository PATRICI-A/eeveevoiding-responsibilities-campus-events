package edu.eci.patricia.domain.model;


import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.valueobjects.EventId;
import lombok.*;

import java.time.LocalDate;
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
    private LocalDate dateTime;
    private LocalDate time;
    private Integer durationMinutes;
    private String location;
    private EventCategory category;
    private EventType type;
    private EventStatus status;
    private Integer maxCapacity;
    private Integer availableCapacity;
    private UUID organizerId;
    private String qrCode;
}
