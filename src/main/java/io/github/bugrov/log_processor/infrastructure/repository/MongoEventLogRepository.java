package io.github.bugrov.log_processor.infrastructure.repository;

import io.github.bugrov.log_processor.infrastructure.entity.MongoLogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoEventLogRepository extends MongoRepository<MongoLogDocument, String> {
}
