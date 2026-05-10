package edu.eci.patricia.DOWS_patricia.domain.model;



import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private EventId id;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private String location;
    private EventCategory category;
    private EventType type;
    private Integer availableCapacity;
    private EventStatus status;
}
