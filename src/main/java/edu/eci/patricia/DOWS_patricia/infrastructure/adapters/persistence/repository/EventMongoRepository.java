package edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.repository;

import edu.eci.patricia.DOWS_patricia.infrastructure.adapters.persistence.entity.EventEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventMongoRepository extends MongoRepository<EventEntity, String> {
    boolean existsByName(String name);
}
