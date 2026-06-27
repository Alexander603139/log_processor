package io.github.bugrov.log_processor.domain.port.outgoing;

import io.github.bugrov.log_processor.api.dto.LogEventMessage;

public interface KafkaProducerPort {
    void sendEvent(LogEventMessage event);
}
