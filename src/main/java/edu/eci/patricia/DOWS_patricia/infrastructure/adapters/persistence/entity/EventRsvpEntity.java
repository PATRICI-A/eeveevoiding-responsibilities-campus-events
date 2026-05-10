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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "event_rsvp")
public class EventRsvpEntity {

    @Id
    private String id;

    @Field("eventId")
    private String eventId;

    @Field("studentId")
    private String studentId;

    @Field("status")
    private RsvpStatus status;
}
