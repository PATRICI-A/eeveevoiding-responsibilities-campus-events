package edu.eci.patricia.infrastructure.adapters.persistence.repository;

import edu.eci.patricia.domain.model.enums.EventCategory;
import edu.eci.patricia.domain.model.enums.EventStatus;
import edu.eci.patricia.infrastructure.adapters.persistence.entity.EventEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface EventMongoRepository extends MongoRepository<EventEntity, UUID> {

    List<EventEntity> findByStatus(EventStatus status);

    List<EventEntity> findByStatusAndCategory(EventStatus status, EventCategory category);

    List<EventEntity> findByStatusAndDateTimeBetween(EventStatus status,
                                                     java.time.LocalDateTime start,
                                                     java.time.LocalDateTime end);

    List<EventEntity> findByStatusAndCategoryAndDateTimeBetween(EventStatus status,
                                                                EventCategory category,
                                                                java.time.LocalDateTime start,
                                                                java.time.LocalDateTime end);
}