package edu.eci.patricia.DOWS_patricia.infrastructure.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Schema(description = "Request para cancelar un evento universitario")
@Getter
public class CancelEventRequest {

    @Schema(description = "Motivo de la cancelación", example = "Fuerza mayor — paro universitario", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(max = 300)
    private String reason;
}