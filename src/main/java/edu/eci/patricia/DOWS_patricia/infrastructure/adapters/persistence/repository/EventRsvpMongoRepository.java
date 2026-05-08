package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.repository;

import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity.EventRsvpEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRsvpMongoRepository extends MongoRepository<EventRsvpEntity, String> {
}
