package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "events")
public class EventEntity {
    @Id
    private String id;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private String location;
    private EventCategory category;
    private EventType type;
    private Integer maxCapacity;
    private Integer availableSpots;
    private String organizerId;
    private EventStatus status;
    private LocalDateTime createdAt;
}