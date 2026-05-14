package edu.eci.patricia.infrastructure.adapters.persistence.repository;

import edu.eci.patricia.domain.model.enums.RsvpStatus;
import edu.eci.patricia.infrastructure.adapters.persistence.entity.EventRsvpEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRsvpMongoRepository extends MongoRepository<EventRsvpEntity, UUID> {

    Optional<EventRsvpEntity> findByEventIdAndStudentId(UUID eventId, UUID studentId);

    List<EventRsvpEntity> findByStudentIdAndStatus(UUID studentId, RsvpStatus status);

    List<EventRsvpEntity> findByEventIdAndStatus(UUID eventId, RsvpStatus status);
}