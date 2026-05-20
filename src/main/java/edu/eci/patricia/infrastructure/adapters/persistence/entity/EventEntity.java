package edu.eci.patricia.infrastructure.adapters.persistence.entity;

import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
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
    private LocalDate dateTime;

    @Field("start_time")
    private String startTime;

    @Field("duration_minutes")
    private Integer durationMinutes;

    @Field("location")
    private String location;

    @Field("category")
    private EventCategory category;

    @Field("type")
    private EventType type;

    @Field("status")
    private EventStatus status;

    @Field("max_capacity")
    private Integer maxCapacity;

    @Field("available_capacity")
    private Integer availableCapacity;

    @Field("organizer_id")
    private UUID organizerId;

    @Field("qr_code")
    private String qrCode;
}