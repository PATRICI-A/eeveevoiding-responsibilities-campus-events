package edu.eci.patricia.DOWS_patricia.application.mapper;

import edu.eci.patricia.DOWS_patricia.infrastructure.dto.request.CreateEventRequest;
import edu.eci.patricia.DOWS_patricia.infrastructure.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.domain.model.Evento;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventMapper {

    public EventResponse toResponse(Evento event) {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getNombre())
                .description(event.getDescripcion())
                .startDateTime(event.getFechaHora())
                .location(event.getLugar())
                .category(event.getCategoria())
                .type(event.getTipo())
                .availableCapacity(event.getCupoDisponible())
                .status(event.getEstado())
                .createdAt(event.getCreadoEn())
                .build();
    }

    public Evento toDomain(CreateEventRequest request) {
        return Evento.builder()
                .nombre(request.getName())
                .descripcion(request.getDescription())
                .fechaHora(request.getStartDateTime())
                .lugar(request.getLocation())
                .categoria(request.getCategory())
                .tipo(request.getType())
                .cupoMaximo(request.getMaxCapacity())
                .cupoDisponible(request.getMaxCapacity())
                .organizadorId(request.getOrganizerId())
                //Falta evento en estado activo pero me estaba dando error
                .creadoEn(LocalDateTime.now())
                .build();
    }
}