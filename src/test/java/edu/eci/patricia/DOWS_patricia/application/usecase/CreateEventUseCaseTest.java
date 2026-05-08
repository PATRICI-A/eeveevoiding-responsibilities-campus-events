package edu.eci.patricia.DOWS_patricia.application.usecase;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequest;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponse;
import edu.eci.patricia.DOWS_patricia.application.mapper.EventMapper;
import edu.eci.patricia.DOWS_patricia.domain.exceptions.EventAlreadyExistsException;
import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventType;
import edu.eci.patricia.DOWS_patricia.domain.ports.out.EventRepositoryPort;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.OrganizerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateEventUseCaseTest {

    @Mock
    private EventRepositoryPort eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private CreateEventUseCase createEventUseCase;

    private EventRequest request;
    private Event event;
    private EventResponse response;

    @BeforeEach
    void setUp() {
        request = EventRequest.builder()
                .name("Tech Talk")
                .description("A tech talk event")
                .dateTime(LocalDateTime.of(2026, 6, 15, 10, 0))
                .location("ECI Auditorium")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .organizerId("org-001")
                .build();

        event = Event.builder()
                .id(new EventId("123e4567-e89b-12d3-a456-426614174000"))
                .name("Tech Talk")
                .description("A tech talk event")
                .dateTime(LocalDateTime.of(2026, 6, 15, 10, 0))
                .location("ECI Auditorium")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .maxCapacity(100)
                .availableSpots(100)
                .organizerId(new OrganizerId("org-001"))
                .status(EventStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        response = EventResponse.builder()
                .id("123e4567-e89b-12d3-a456-426614174000")
                .name("Tech Talk")
                .status(EventStatus.ACTIVE)
                .build();
    }

    @Test
    void shouldCreateEventSuccessfully() {
        when(eventRepository.existsByName(request.getName())).thenReturn(false);
        when(eventMapper.toDomain(request)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toResponse(event)).thenReturn(response);

        EventResponse result = createEventUseCase.execute(request);

        assertNotNull(result);
        assertEquals("Tech Talk", result.getName());
        verify(eventRepository).existsByName(request.getName());
        verify(eventRepository).save(event);
    }

    @Test
    void shouldThrowExceptionWhenEventNameAlreadyExists() {
        when(eventRepository.existsByName(request.getName())).thenReturn(true);

        assertThrows(EventAlreadyExistsException.class, () -> createEventUseCase.execute(request));
        verify(eventRepository, never()).save(any());
    }

    @Test
    void shouldCallMapperToDomainWhenCreatingEvent() {
        when(eventRepository.existsByName(request.getName())).thenReturn(false);
        when(eventMapper.toDomain(request)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toResponse(event)).thenReturn(response);

        createEventUseCase.execute(request);

        verify(eventMapper).toDomain(request);
        verify(eventMapper).toResponse(event);
    }
}
