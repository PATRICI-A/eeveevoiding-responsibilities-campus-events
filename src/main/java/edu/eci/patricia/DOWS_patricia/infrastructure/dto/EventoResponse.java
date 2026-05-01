package edu.eci.patricia.DOWS_patricia.infrastructure.dto;

import edu.eci.patricia.DOWS_patricia.domain.model.CategoriaEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.EstadoEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.TipoEvento;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventoResponse {

    private String id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaHora;
    private String lugar;
    private CategoriaEvento categoria;
    private TipoEvento tipo;
    private Integer cupoDisponible;
    private EstadoEvento estado;
}
