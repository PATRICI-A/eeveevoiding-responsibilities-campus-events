package edu.eci.patricia.DOWS_patricia.infrastructure.dto.response;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.CategoriaEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EstadoEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.TipoEvento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "Evento universitario")
@Getter
@Builder
public class EventResponse {

    @Schema(description = "ID único del evento", example = "evt-001")
    private String id;

    @Schema(description = "Nombre del evento", example = "Feria de Ciencias ECI 2026")
    private String name;

    @Schema(description = "Descripción del evento")
    private String description;

    @Schema(description = "Fecha y hora de inicio", example = "2026-06-15T09:00:00")
    private LocalDateTime startDateTime;

    @Schema(description = "Lugar dentro del campus", example = "Auditorio principal")
    private String location;

    @Schema(description = "Categoría del evento", example = "ACADEMICO")
    private CategoriaEvento category;

    @Schema(description = "Tipo del evento", example = "CON_CUPO")
    private TipoEvento type;

    @Schema(description = "Cupos disponibles. Null si tipo = ABIERTO", example = "30")
    private Integer availableCapacity;

    @Schema(description = "Estado del evento", example = "ACTIVO")
    private EstadoEvento status;

    @Schema(description = "Fecha de creación del registro")
    private LocalDateTime createdAt;
}
