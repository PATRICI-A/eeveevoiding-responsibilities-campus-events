package edu.eci.patricia.DOWS_patricia.domain.model;



import edu.eci.patricia.DOWS_patricia.application.dto.request.EventUpdateRequest;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.*;
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
    private String organizerId;

    public void validateEditableBy(String organizerId) {
        if (!this.organizerId.equals(organizerId)) {
            throw new NotEventCreatorException("Only the event creator can edit this event");
        }
        if (!this.status.equals(EventStatus.ACTIVE)) {
            throw new EventNotEditableException("Only active events can be edited");
        }
    }

    public void validateCancelableBy(String organizerId) {
        if (!this.organizerId.equals(organizerId)) {
            throw new NotEventCreatorException("Only the event creator can cancel this event");
        }
        if (!this.status.equals(EventStatus.ACTIVE)) {
            throw new EventNotCancelableException("Only active events can be cancelled");
        }
    }

    public void validateCanReceiveRsvp() {
        if (!this.status.equals(EventStatus.ACTIVE)) {
            throw new EventNotActiveException("Event is not active");
        }
        if (this.type.equals(EventType.WITH_CAPACITY) && this.availableCapacity <= 0) {
            throw new CapacityFullException("Event has reached its maximum capacity");
        }
    }


    public void decreaseCapacity() {
        if (this.type.equals(EventType.WITH_CAPACITY)) {
            this.availableCapacity--;
        }
    }

    public void increaseCapacity() {
        if (this.type.equals(EventType.WITH_CAPACITY)) {
            this.availableCapacity++;
        }
    }

    public void cancel() {
        this.status = EventStatus.CANCELLED;
    }

    public void updateFrom(EventUpdateRequest request) {
        if (request.getName() != null) {
            this.name = request.getName();
        }
        if (request.getDescription() != null) {
            this.description = request.getDescription();
        }
        if (request.getDateTime() != null) {
            this.dateTime = request.getDateTime();
        }
        if (request.getLocation() != null) {
            this.location = request.getLocation();
        }
        if (request.getCategory() != null) {
            this.category = request.getCategory();
        }
        if (request.getType() != null) {
            this.type = request.getType();
        }
        if (request.getAvailableCapacity() != null) {
            this.availableCapacity = request.getAvailableCapacity();
        }
    }
}
