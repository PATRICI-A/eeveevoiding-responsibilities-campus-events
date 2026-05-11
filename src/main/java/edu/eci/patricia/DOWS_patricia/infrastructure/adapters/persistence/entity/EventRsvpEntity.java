package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpStatus;
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
@Document(collection = "event_rsvp")
public class EventRsvpEntity {

    @Id
    private UUID id;

    @Field("event_id")
    private UUID eventId;

    @Field("student_id")
    private UUID studentId;

    @Field("status")
    private RsvpStatus status;

    @Field("confirmed_at")
    private LocalDateTime confirmedAt;

    @Field("cancelled_at")
    private LocalDateTime cancelledAt;
}