package edu.eci.patricia.application.dto.response;

import edu.eci.patricia.domain.model.enums.RsvpStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseRsvp {

    private UUID id;
    private UUID eventId;
    private UUID studentId;
    private RsvpStatus status;
}