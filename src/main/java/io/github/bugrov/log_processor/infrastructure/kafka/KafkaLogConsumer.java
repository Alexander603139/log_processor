package io.github.bugrov.log_processor.infrastructure.kafka;


import io.github.bugrov.log_processor.api.dto.LogEventMessage;
import io.github.bugrov.log_processor.domain.model.EventLog;
import io.github.bugrov.log_processor.domain.port.outgoing.EventLogRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaLogConsumer {

    private final EventLogRepositoryPort repository;

    @KafkaListener(topics = "${app.kafka.topic.raw-events}", groupId = "${app.kafka.consumer.group-id}")
    public void consume(LogEventMessage message) {
        log.info("Received event from Kafka: {}", message);

        // Преобразуем в доменную модель
        EventLog eventLog = EventLog.builder()
                .id(message.getId() != null ? message.getId() : UUID.randomUUID().toString())
                .source(message.getSource())
                .ipAddress(message.getIpAddress())
                .userAgent(message.getUserAgent())
                .rawMessage(message.getRawMessage())
                .timestamp(message.getTimestamp() != null ? message.getTimestamp() : Instant.now())
                .build();

        // Выполняем бизнес-правила (можно вынести в отдельный RuleEngine позже)
        if (eventLog.isSuspicious()) {
            // Здесь можно отправить алерт в другой топик (например, outgoing-commands)
            log.warn("Suspicious log detected: {}", eventLog);
        }

        // Сохраняем в обе БД через порт
        log.info("Calling repository.save for event: {}", eventLog);
        repository.save(eventLog);
    }
}
