package io.github.bugrov.log_processor.infrastructure.repository;

import io.github.bugrov.log_processor.infrastructure.entity.PostgresLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface JpaEventLogRepository extends JpaRepository<PostgresLogEntity, String> {

    @Modifying
    @Query("DELETE FROM PostgresLogEntity e WHERE e.timestamp < :date")
    int deleteOlderThan(@Param("date") Instant date);
}
