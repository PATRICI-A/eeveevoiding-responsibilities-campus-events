package edu.eci.patricia.DOWS_patricia.domain.model;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRsvp {
    private RsvpId id;
    private EventId eventId;
    private String studentId;
    private RsvpStatus status;
}


