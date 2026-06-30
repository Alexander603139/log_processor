package io.github.bugrov.log_processor.domain.port.outgoing;

import io.github.bugrov.log_processor.api.dto.CommandMessage;

public interface KafkaCommandProducerPort {
    void sendCommand(CommandMessage command);
}