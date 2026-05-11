package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.adapter;

import edu.eci.patricia.DOWS_patricia.domain.model.Event;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity.EventEntity;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.mapper.EventPersistenceMapper;
import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.repository.EventMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRepositoryAdapterTest {

    @Mock
    private EventMongoRepository repository;

    @Mock
    private EventPersistenceMapper mapper;

    @InjectMocks
    private EventRepositoryAdapter adapter;

    private UUID eventUUID;
    private EventId eventId;
    private Event event;
    private EventEntity eventEntity;

    @BeforeEach
    void setUp() {
        eventUUID = UUID.randomUUID();
        eventId = new EventId(eventUUID);
        event = Event.builder().id(eventId).name("Test Event").build();
        eventEntity = EventEntity.builder().id(eventUUID).name("Test Event").build();
    }

    @Test
    void save_shouldReturnMappedModel() {
        when(mapper.toEntity(event)).thenReturn(eventEntity);
        when(repository.save(eventEntity)).thenReturn(eventEntity);
        when(mapper.toModel(eventEntity)).thenReturn(event);

        Event result = adapter.save(event);

        assertNotNull(result);
        assertEquals(event, result);
        verify(mapper).toEntity(event);
        verify(repository).save(eventEntity);
        verify(mapper).toModel(eventEntity);
    }

    @Test
    void findById_shouldReturnMappedModel_whenEntityExists() {
        when(repository.findById(eventUUID)).thenReturn(Optional.of(eventEntity));
        when(mapper.toModel(eventEntity)).thenReturn(event);

        Optional<Event> result = adapter.findById(eventId);

        assertTrue(result.isPresent());
        assertEquals(event, result.get());
        verify(repository).findById(eventUUID);
        verify(mapper).toModel(eventEntity);
    }

    @Test
    void findById_shouldReturnEmpty_whenEntityDoesNotExist() {
        when(repository.findById(eventUUID)).thenReturn(Optional.empty());

        Optional<Event> result = adapter.findById(eventId);

        assertFalse(result.isPresent());
        verify(repository).findById(eventUUID);
        verify(mapper, never()).toModel(any());
    }

    @Test
    void findActiveEvents_shouldFindAllActive_whenNoCategoryNorDate() {
        when(repository.findByStatus(EventStatus.ACTIVE)).thenReturn(List.of(eventEntity));
        when(mapper.toModel(eventEntity)).thenReturn(event);

        List<Event> result = adapter.findActiveEvents(null, null);

        assertEquals(1, result.size());
        verify(repository).findByStatus(EventStatus.ACTIVE);
        verify(repository, never()).findByStatusAndCategory(any(), any());
        verify(repository, never()).findByStatusAndDateTimeBetween(any(), any(), any());
        verify(repository, never()).findByStatusAndCategoryAndDateTimeBetween(any(), any(), any(), any());
    }

    @Test
    void findActiveEvents_shouldFilterByCategory_whenOnlyCategoryProvided() {
        EventCategory category = EventCategory.CULTURAL;
        when(repository.findByStatusAndCategory(EventStatus.ACTIVE, category)).thenReturn(List.of(eventEntity));
        when(mapper.toModel(eventEntity)).thenReturn(event);

        List<Event> result = adapter.findActiveEvents(category, null);

        assertEquals(1, result.size());
        verify(repository).findByStatusAndCategory(EventStatus.ACTIVE, category);
        verify(repository, never()).findByStatus(any());
        verify(repository, never()).findByStatusAndDateTimeBetween(any(), any(), any());
        verify(repository, never()).findByStatusAndCategoryAndDateTimeBetween(any(), any(), any(), any());
    }

    @Test
    void findActiveEvents_shouldFilterByDate_whenOnlyDateProvided() {
        LocalDate date = LocalDate.now().plusDays(1);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        when(repository.findByStatusAndDateTimeBetween(EventStatus.ACTIVE, start, end)).thenReturn(List.of(eventEntity));
        when(mapper.toModel(eventEntity)).thenReturn(event);

        List<Event> result = adapter.findActiveEvents(null, date);

        assertEquals(1, result.size());
        verify(repository).findByStatusAndDateTimeBetween(EventStatus.ACTIVE, start, end);
        verify(repository, never()).findByStatus(any());
        verify(repository, never()).findByStatusAndCategory(any(), any());
        verify(repository, never()).findByStatusAndCategoryAndDateTimeBetween(any(), any(), any(), any());
    }

    @Test
    void findActiveEvents_shouldFilterByCategoryAndDate_whenBothProvided() {
        EventCategory category = EventCategory.CULTURAL;
        LocalDate date = LocalDate.now().plusDays(1);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        when(repository.findByStatusAndCategoryAndDateTimeBetween(EventStatus.ACTIVE, category, start, end))
                .thenReturn(List.of(eventEntity));
        when(mapper.toModel(eventEntity)).thenReturn(event);

        List<Event> result = adapter.findActiveEvents(category, date);

        assertEquals(1, result.size());
        verify(repository).findByStatusAndCategoryAndDateTimeBetween(EventStatus.ACTIVE, category, start, end);
        verify(repository, never()).findByStatus(any());
        verify(repository, never()).findByStatusAndCategory(any(), any());
        verify(repository, never()).findByStatusAndDateTimeBetween(any(), any(), any());
    }

    @Test
    void findActiveEvents_shouldReturnEmptyList_whenNoResults() {
        when(repository.findByStatus(EventStatus.ACTIVE)).thenReturn(List.of());

        List<Event> result = adapter.findActiveEvents(null, null);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByStatus_shouldReturnMappedList() {
        when(repository.findByStatus(EventStatus.CANCELLED)).thenReturn(List.of(eventEntity));
        when(mapper.toModel(eventEntity)).thenReturn(event);

        List<Event> result = adapter.findByStatus(EventStatus.CANCELLED);

        assertEquals(1, result.size());
        assertEquals(event, result.get(0));
        verify(repository).findByStatus(EventStatus.CANCELLED);
    }

    @Test
    void findByStatus_shouldReturnEmptyList_whenNoResults() {
        when(repository.findByStatus(EventStatus.CANCELLED)).thenReturn(List.of());

        List<Event> result = adapter.findByStatus(EventStatus.CANCELLED);

        assertTrue(result.isEmpty());
    }
}
