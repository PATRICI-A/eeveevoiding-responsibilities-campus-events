package edu.eci.patricia.DOWS_patricia.application.dto.response;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseRsvp {
    private String id;
    private String eventId;
    private String studentId;
    private LocalDateTime confirmedAt;
    private RsvpStatus status;
}
