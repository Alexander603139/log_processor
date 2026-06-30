package io.github.bugrov.log_processor.infrastructure.kafka;

import io.github.bugrov.log_processor.api.dto.CommandMessage;
import io.github.bugrov.log_processor.domain.port.outgoing.KafkaCommandProducerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaCommandProducerAdapter implements KafkaCommandProducerPort {

    private final KafkaTemplate<String, CommandMessage> kafkaTemplate;

    @Value("${app.kafka.topic.outgoing-commands}")
    private String topicName;

    @Override
    public void sendCommand(CommandMessage command) {
        kafkaTemplate.send(topicName, command.getIpAddress(), command);
        log.debug("Command sent to Kafka: {}", command);
    }
}
