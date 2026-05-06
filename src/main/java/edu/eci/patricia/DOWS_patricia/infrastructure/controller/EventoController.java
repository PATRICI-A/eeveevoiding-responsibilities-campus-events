package edu.eci.patricia.DOWS_patricia.infrastructure.controller;

import edu.eci.patricia.DOWS_patricia.application.service.EventoService;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.CategoriaEvento;
import edu.eci.patricia.DOWS_patricia.domain.model.Evento;
import edu.eci.patricia.DOWS_patricia.infrastructure.dto.EventoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/eventos")
@RequiredArgsConstructor
@Tag(name = "Eventos Universitarios", description = "Feed oficial de eventos del campus")
public class EventoController {

    private final EventoService eventoService;

    @GetMapping
    @Operation(
        summary = "Consultar feed de eventos",
        description = "Como estudiante, quiero ver los eventos universitarios disponibles y filtrarlos para encontrar los que me interesan."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Feed de eventos retornado exitosamente",
            content = @Content(schema = @Schema(implementation = EventoResponse.class))),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content),
        @ApiResponse(responseCode = "401", description = "Token JWT inválido o ausente", content = @Content)
    })
    public ResponseEntity<List<EventoResponse>> consultarFeed(
            @RequestParam(required = false) CategoriaEvento categoria) {

        List<Evento> eventos = eventoService.obtenerFeed(categoria);
        List<EventoResponse> response = eventos.stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    private EventoResponse toResponse(Evento evento) {
        EventoResponse dto = new EventoResponse();
        dto.setId(evento.getId());
        dto.setNombre(evento.getNombre());
        dto.setDescripcion(evento.getDescripcion());
        dto.setFechaHora(evento.getFechaHora());
        dto.setLugar(evento.getLugar());
        dto.setCategoria(evento.getCategoria());
        dto.setTipo(evento.getTipo());
        dto.setCupoDisponible(evento.getCupoDisponible());
        dto.setEstado(evento.getEstado());
        return dto;
    }
}
