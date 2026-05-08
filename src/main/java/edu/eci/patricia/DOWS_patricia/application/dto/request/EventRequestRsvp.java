package edu.eci.patricia.DOWS_patricia.application.dto.request;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.StudentId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestRsvp {
    private String eventId;
    private String studentId;
    private LocalDateTime confirmedAt;
    private RsvpStatus status;
}
