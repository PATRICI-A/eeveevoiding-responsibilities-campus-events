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
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "events")
public class EventEntity {

    @Id
    private UUID id;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("date_time")
    private LocalDateTime dateTime;

    @Field("location")
    private String location;

    @Field("category")
    private EventCategory category;

    @Field("type")
    private EventType type;

    @Field("available_capacity")
    private Integer availableCapacity;

    @Field("status")
    private EventStatus status;

    @Field("organizer_id")
    private String organizerId;
}