package io.github.bugrov.log_processor.infrastructure.repository;

import io.github.bugrov.log_processor.domain.model.EventLog;
import io.github.bugrov.log_processor.domain.port.outgoing.EventLogRepositoryPort;
import io.github.bugrov.log_processor.infrastructure.entity.MongoLogDocument;
import io.github.bugrov.log_processor.infrastructure.entity.PostgresLogEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CompositeEventLogRepository implements EventLogRepositoryPort {

    private final JpaEventLogRepository jpaRepo;
    private final MongoEventLogRepository mongoRepo;

    @Override
    public void save(EventLog eventLog) {
        // 1. Сохраняем в PostgreSQL
        PostgresLogEntity sqlEntity = PostgresLogEntity.builder()
                .id(eventLog.getId())
                .source(eventLog.getSource())
                .ipAddress(eventLog.getIpAddress())
                .userAgent(eventLog.getUserAgent())
                .rawMessage(eventLog.getRawMessage())
                .timestamp(eventLog.getTimestamp())
                .build();
        try {
            jpaRepo.save(sqlEntity);
        } catch (Exception e) {
            log.error("Failed to save to PostgreSQL: {}", e.getMessage());
            // В реальном проекте можно реализовать компенсирующие действия
        }

        // 2. Сохраняем в MongoDB
        MongoLogDocument mongoDoc = MongoLogDocument.builder()
                .id(eventLog.getId())
                .source(eventLog.getSource())
                .ipAddress(eventLog.getIpAddress())
                .userAgent(eventLog.getUserAgent())
                .rawMessage(eventLog.getRawMessage())
                .timestamp(eventLog.getTimestamp())
                .build();
        try {
            mongoRepo.save(mongoDoc);
        } catch (Exception e) {
            log.error("Failed to save to MongoDB: {}", e.getMessage());
        }
    }
}