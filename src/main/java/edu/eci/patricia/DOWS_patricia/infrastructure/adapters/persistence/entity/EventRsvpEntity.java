package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpStatus;
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
@Document(collection = "event_rsvp")
public class EventRsvpEntity {
    @Id
    private String id;
    private String eventId;
    private String studentId;
    private LocalDateTime confirmedAt;
    private RsvpStatus status;
}
