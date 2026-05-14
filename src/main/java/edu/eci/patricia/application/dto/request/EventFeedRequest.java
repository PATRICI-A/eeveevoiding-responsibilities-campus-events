package edu.eci.patricia.application.dto.request;



import edu.eci.patricia.domain.model.enums.EventCategory;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFeedRequest {

    private EventCategory category;

    @FutureOrPresent(message = "Date filter cannot be in the past")
    private LocalDate date;
}