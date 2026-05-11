package edu.eci.patricia.DOWS_patricia.domain.model;

import edu.eci.patricia.DOWS_patricia.domain.exceptions.RsvpNotCancelledException;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.RsvpNotConfirmedException;
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

    public void confirm() {
        this.status = RsvpStatus.CONFIRMED;

    }

    public void cancel() {
        if (!this.status.equals(RsvpStatus.CONFIRMED)) {
            throw new RsvpNotConfirmedException("RSVP is not in confirmed status");
        }
        this.status = RsvpStatus.CANCELLED;
    }

    public void reactivate() {
        if (!this.status.equals(RsvpStatus.CANCELLED)) {
            throw new RsvpNotCancelledException("RSVP is not in cancelled status");
        }
        this.status = RsvpStatus.CONFIRMED;
    }

    public boolean isConfirmed() {
        return this.status.equals(RsvpStatus.CONFIRMED);
    }
}


