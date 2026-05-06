package edu.eci.patricia.DOWS_patricia.infrastructure.dto.request;

import edu.eci.patricia.DOWS_patricia.domain.model.enums.CategoriaEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.TipoEvento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "Request para publicar un nuevo evento universitario")
@Getter
public class CreateEventRequest {

    @Schema(description = "Nombre del evento", example = "Feria de Ciencias ECI 2026", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String name;

    @Schema(description = "Descripción del evento", example = "Exposición de proyectos estudiantiles")
    private String description;

    @Schema(description = "Fecha y hora de inicio", example = "2026-06-15T09:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Future
    private LocalDateTime startDateTime;

    @Schema(description = "Lugar dentro del campus", example = "Bloque B — Auditorio principal", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String location;

    @Schema(description = "Categoría del evento", example = "ACADEMICO", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private CategoriaEvento category;

    @Schema(description = "Tipo: ABIERTO sin límite o CON_CUPO con límite", example = "CON_CUPO", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private TipoEvento type;

    @Schema(description = "Cupo máximo. Obligatorio si tipo = CON_CUPO, mínimo 2", example = "50")
    @Min(2)
    private Integer maxCapacity;

    @Schema(description = "ID del organizador que publica el evento", example = "org-001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String organizerId;
}