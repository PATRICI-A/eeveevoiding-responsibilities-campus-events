package edu.eci.patricia.DOWS_patricia.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "event_rsvp")
public class EventoRsvp {

    public enum EstadoRsvp { CONFIRMADO, CANCELADO }

    @Id
    private String id;
    private String eventoId;
    private String estudianteId;
    private LocalDateTime confirmadoEn;
    private EstadoRsvp estado;
}
