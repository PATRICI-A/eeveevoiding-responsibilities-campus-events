package edu.eci.patricia.application.dto.response;

import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    private UUID id;
    private String name;
    private LocalDate dateTime;
    private Integer time;
    private Integer duration;
    private String location;
    private EventCategory category;
    private EventType type;
    private EventStatus status;
    private String qrCode;
}