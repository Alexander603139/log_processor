package io.github.bugrov.log_processor.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.time.Instant;

@Getter
@Builder
public class EventLog {
    private String id;
    private String source;
    private String ipAddress;
    private String userAgent;
    private String rawMessage;
    private Instant timestamp;

    // Бизнес-метод для тестов (чистая логика, без БД)
    public boolean isSuspicious() {
        return rawMessage != null && rawMessage.toLowerCase().contains("error")
                && ipAddress.startsWith("10."); // Пример: внутренние ошибки
    }
}
