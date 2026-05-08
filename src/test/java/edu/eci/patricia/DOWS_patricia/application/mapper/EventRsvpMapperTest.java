package edu.eci.patricia.DOWS_patricia.application.mapper;

import edu.eci.patricia.DOWS_patricia.application.dto.request.EventRequestRsvp;
import edu.eci.patricia.DOWS_patricia.application.dto.response.EventResponseRsvp;
import edu.eci.patricia.DOWS_patricia.domain.model.EventRsvp;
import edu.eci.patricia.DOWS_patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.EventId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.RsvpId;
import edu.eci.patricia.DOWS_patricia.domain.valueobjects.StudentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventRsvpMapperTest {

    private EventRsvpMapper eventRsvpMapper;
    private EventRequestRsvp request;
    private EventRsvp rsvp;

    @BeforeEach
    void setUp() {
        eventRsvpMapper = new EventRsvpMapper();

        request = EventRequestRsvp.builder()
                .eventId("123e4567-e89b-12d3-a456-426614174000")
                .studentId("student-001")
                .status(RsvpStatus.CONFIRMED)
                .build();

        rsvp = EventRsvp.builder()
                .id(new RsvpId("rsvp-001"))
                .eventId(new EventId("123e4567-e89b-12d3-a456-426614174000"))
                .studentId(new StudentId("student-001"))
                .confirmedAt(LocalDateTime.of(2026, 5, 1, 12, 0))
                .status(RsvpStatus.CONFIRMED)
                .build();
    }

    @Test
    void shouldMapRequestToDomain() {
        EventRsvp result = eventRsvpMapper.toDomain(request);
        assertNotNull(result);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.getEventId().getValue());
        assertEquals("student-001", result.getStudentId().getValue());
        assertEquals(RsvpStatus.CONFIRMED, result.getStatus());
    }

    @Test
    void shouldGenerateRsvpIdWhenMappingToDomain() {
        EventRsvp result = eventRsvpMapper.toDomain(request);
        assertNotNull(result.getId());
        assertNotNull(result.getId().getValue());
        assertFalse(result.getId().getValue().isBlank());
    }

    @Test
    void shouldSetConfirmedAtWhenMappingToDomain() {
        EventRsvp result = eventRsvpMapper.toDomain(request);
        assertNotNull(result.getConfirmedAt());
    }

    @Test
    void shouldMapDomainToResponse() {
        EventResponseRsvp result = eventRsvpMapper.toResponse(rsvp);
        assertNotNull(result);
        assertEquals("rsvp-001", result.getId());
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.getEventId());
        assertEquals("student-001", result.getStudentId());
        assertEquals(RsvpStatus.CONFIRMED, result.getStatus());
        assertEquals(LocalDateTime.of(2026, 5, 1, 12, 0), result.getConfirmedAt());
    }

    @Test
    void shouldMapRsvpIdAsStringInResponse() {
        EventResponseRsvp result = eventRsvpMapper.toResponse(rsvp);
        assertEquals("rsvp-001", result.getId());
    }

    @Test
    void shouldMapEventIdAsStringInResponse() {
        EventResponseRsvp result = eventRsvpMapper.toResponse(rsvp);
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.getEventId());
    }

    @Test
    void shouldMapStudentIdAsStringInResponse() {
        EventResponseRsvp result = eventRsvpMapper.toResponse(rsvp);
        assertEquals("student-001", result.getStudentId());
    }
}
