package edu.eci.patricia.infrastructure.adapters.persistence.mapper;

import edu.eci.patricia.domain.model.Event;
import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.domain.model.enums.EventType;
import edu.eci.patricia.domain.valueobjects.EventId;
import edu.eci.patricia.infrastructure.adapters.persistence.entity.EventEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventPersistenceMapperTest {

    private EventPersistenceMapper eventPersistenceMapper;

    @BeforeEach
    void setUp() {
        eventPersistenceMapper = Mappers.getMapper(EventPersistenceMapper.class);
    }

    // ── toEntity ──────────────────────────────────────────────

    @Test
    void toEntity_shouldMapDomainToEntity_whenValidEvent() {
        UUID id = UUID.randomUUID();
        UUID organizerId = UUID.randomUUID();

        Event event = Event.builder()
                .id(new EventId(id))
                .name("Evento Test")
                .description("Descripción")
                .dateTime(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(9, 30))
                .durationMinutes(60)
                .location("Bogotá")
                .category(EventCategory.ACADEMIC)
                .type(EventType.OPEN)
                .status(EventStatus.ACTIVE)
                .maxCapacity(null)
                .availableCapacity(null)
                .organizerId(organizerId)
                .qrCode("QR-" + id)
                .build();

        EventEntity result = eventPersistenceMapper.toEntity(event);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Evento Test", result.getName());
        assertEquals("Descripción", result.getDescription());
        assertEquals("09:30", result.getStartTime());
        assertEquals(60, result.getDurationMinutes());
        assertEquals("Bogotá", result.getLocation());
        assertEquals(EventCategory.ACADEMIC, result.getCategory());
        assertEquals(EventType.OPEN, result.getType());
        assertEquals(EventStatus.ACTIVE, result.getStatus());
        assertEquals(organizerId, result.getOrganizerId());
    }

    @Test
    void toEntity_shouldMapStartTime_asString() {
        Event event = Event.builder()
                .id(EventId.generate())
                .name("Evento")
                .startTime(LocalTime.of(8, 0))
                .build();

        EventEntity result = eventPersistenceMapper.toEntity(event);

        assertEquals("08:00", result.getStartTime());
    }

    @Test
    void toEntity_shouldMapNullStartTime_whenStartTimeIsNull() {
        Event event = Event.builder()
                .id(EventId.generate())
                .name("Evento")
                .startTime(null)
                .build();

        EventEntity result = eventPersistenceMapper.toEntity(event);

        assertNull(result.getStartTime());
    }

    // ── toModel ───────────────────────────────────────────────

    @Test
    void toModel_shouldMapEntityToDomain_whenValidEntity() {
        UUID id = UUID.randomUUID();
        UUID organizerId = UUID.randomUUID();

        EventEntity entity = EventEntity.builder()
                .id(id)
                .name("Evento Entity")
                .description("Desc")
                .dateTime(LocalDate.now().plusDays(1))
                .startTime("14:00")
                .durationMinutes(45)
                .location("Cali")
                .category(EventCategory.ACADEMIC)
                .type(EventType.WITH_CAPACITY)
                .status(EventStatus.ACTIVE)
                .maxCapacity(30)
                .availableCapacity(25)
                .organizerId(organizerId)
                .qrCode("QR-" + id)
                .build();

        Event result = eventPersistenceMapper.toModel(entity);

        assertNotNull(result);
        assertEquals(id, result.getId().getValue());
        assertEquals("Evento Entity", result.getName());
        assertEquals(LocalTime.of(14, 0), result.getStartTime());
        assertEquals(45, result.getDurationMinutes());
        assertEquals(EventStatus.ACTIVE, result.getStatus());
        assertEquals(organizerId, result.getOrganizerId());
    }

    @Test
    void toModel_shouldParseStartTime_fromString() {
        EventEntity entity = EventEntity.builder()
                .id(UUID.randomUUID())
                .name("Evento")
                .startTime("08:30")
                .build();

        Event result = eventPersistenceMapper.toModel(entity);

        assertEquals(LocalTime.of(8, 30), result.getStartTime());
    }

    @Test
    void toModel_shouldReturnNullStartTime_whenStartTimeIsNull() {
        EventEntity entity = EventEntity.builder()
                .id(UUID.randomUUID())
                .name("Evento")
                .startTime(null)
                .build();

        Event result = eventPersistenceMapper.toModel(entity);

        assertNull(result.getStartTime());
    }

    // ── localTimeToString / stringToLocalTime ─────────────────

    @Test
    void localTimeToString_shouldReturnFormattedString_whenLocalTimeNotNull() {
        String result = eventPersistenceMapper.localTimeToString(LocalTime.of(10, 45));
        assertEquals("10:45", result);
    }

    @Test
    void localTimeToString_shouldReturnNull_whenLocalTimeIsNull() {
        assertNull(eventPersistenceMapper.localTimeToString(null));
    }

    @Test
    void stringToLocalTime_shouldReturnLocalTime_whenValidString() {
        LocalTime result = eventPersistenceMapper.stringToLocalTime("08:30");
        assertEquals(LocalTime.of(8, 30), result);
    }

    @Test
    void stringToLocalTime_shouldReturnNull_whenStringIsNull() {
        assertNull(eventPersistenceMapper.stringToLocalTime(null));
    }

    // ── eventIdToUUID / uuidToEventId ─────────────────────────

    @Test
    void eventIdToUUID_shouldReturnUUID_whenEventIdNotNull() {
        UUID uuid = UUID.randomUUID();
        assertEquals(uuid, eventPersistenceMapper.eventIdToUUID(new EventId(uuid)));
    }

    @Test
    void eventIdToUUID_shouldReturnNull_whenEventIdIsNull() {
        assertNull(eventPersistenceMapper.eventIdToUUID(null));
    }

    @Test
    void uuidToEventId_shouldReturnEventId_whenUUIDNotNull() {
        UUID uuid = UUID.randomUUID();
        EventId result = eventPersistenceMapper.uuidToEventId(uuid);

        assertNotNull(result);
        assertEquals(uuid, result.getValue());
    }

    @Test
    void uuidToEventId_shouldReturnNull_whenUUIDIsNull() {
        assertNull(eventPersistenceMapper.uuidToEventId(null));
    }
}
