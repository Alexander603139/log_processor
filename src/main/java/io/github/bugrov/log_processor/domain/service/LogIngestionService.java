package io.github.bugrov.log_processor.domain.service;

import io.github.bugrov.log_processor.api.dto.LogEventMessage;
import io.github.bugrov.log_processor.domain.model.EventLog;
import io.github.bugrov.log_processor.domain.port.outgoing.KafkaProducerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogIngestionService {

    private final KafkaProducerPort kafkaProducer;

    public void process(EventLog eventLog) {
        // Преобразуем в DTO для Kafka
        LogEventMessage message = LogEventMessage.builder()
                .id(eventLog.getId() != null ? eventLog.getId() : UUID.randomUUID().toString())
                .source(eventLog.getSource())
                .ipAddress(eventLog.getIpAddress())
                .userAgent(eventLog.getUserAgent())
                .rawMessage(eventLog.getRawMessage())
                .timestamp(eventLog.getTimestamp() != null ? eventLog.getTimestamp() : Instant.now())
                .build();

        // Отправляем в Kafka
        kafkaProducer.sendEvent(message);
        log.info("Event sent to Kafka for async processing: {}", message.getId());
    }
}