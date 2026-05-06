package edu.eci.patricia.DOWS_patricia.domain.ports.in;

import edu.eci.patricia.DOWS_patricia.domain.model.Evento;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.CategoriaEvento;

import java.time.LocalDate;
import java.util.List;

// Entrada para consultar los eventos con filtros
public interface GetEventsUseCase {
    List<Evento> execute(CategoriaEvento category, LocalDate date, int page, int size);
}

