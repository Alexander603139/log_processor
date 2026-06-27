package io.github.bugrov.log_processor.infrastructure.kafka;


import io.github.bugrov.log_processor.api.dto.LogEventMessage;
import io.github.bugrov.log_processor.domain.port.outgoing.KafkaProducerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerAdapter implements KafkaProducerPort {

    private final KafkaTemplate<String, LogEventMessage> kafkaTemplate;

    @Value("${app.kafka.topic.raw-events}")
    private String topicName;

    @Override
    public void sendEvent(LogEventMessage event) {
        // Можно установить ключ = ipAddress для партиционирования
        kafkaTemplate.send(topicName, event.getIpAddress(), event);
        log.debug("Sent event to Kafka: {}", event);
    }
}