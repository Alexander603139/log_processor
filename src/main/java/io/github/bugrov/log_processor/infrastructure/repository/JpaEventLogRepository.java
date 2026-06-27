package io.github.bugrov.log_processor.infrastructure.repository;

import io.github.bugrov.log_processor.infrastructure.entity.PostgresLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEventLogRepository extends JpaRepository<PostgresLogEntity, String> {
}
