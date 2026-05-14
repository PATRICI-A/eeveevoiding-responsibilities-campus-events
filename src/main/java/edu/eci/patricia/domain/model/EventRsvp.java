package edu.eci.patricia.domain.model;


import edu.eci.patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.domain.valueobjects.RsvpId;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventRsvp {

    private RsvpId id;
    private EventId eventId;
    private UUID studentId;
    private RsvpStatus status;
    private LocalDateTime confirmedAt;
    private LocalDateTime cancelledAt;
}


