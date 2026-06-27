package io.github.bugrov.log_processor.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogEventMessage {
    private String id;              // можно генерировать UUID
    private String source;
    private String ipAddress;
    private String userAgent;
    private String rawMessage;
    private Instant timestamp;
}
