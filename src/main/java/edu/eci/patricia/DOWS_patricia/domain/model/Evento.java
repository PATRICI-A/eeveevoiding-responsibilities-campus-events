package edu.eci.patricia.DOWS_patricia.domain.model;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.CategoriaEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EstadoEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.TipoEvento;
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
@Document(collection = "events")
public class Evento {

    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaHora;
    private String lugar;
    private CategoriaEvento categoria;
    private TipoEvento tipo;
    private Integer cupoMaximo;
    private Integer cupoDisponible;
    private String organizadorId;
    @Builder.Default
    private EstadoEvento estado = EstadoEvento.ACTIVO;
    private LocalDateTime creadoEn;
}
