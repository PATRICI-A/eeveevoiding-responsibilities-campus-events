package edu.eci.patricia.infrastructure.messaging.dto;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventChangeEventDto {
    private UUID targetUserId;
    private UUID eventId;
    private String eventName;
    private String changeDescription;
}