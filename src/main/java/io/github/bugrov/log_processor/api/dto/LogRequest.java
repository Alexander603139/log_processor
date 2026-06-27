package io.github.bugrov.log_processor.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogRequest {
    @NotBlank
    private String source;        // Например: "PYTHON_APP", "NGINX"
    @NotBlank
    private String ipAddress;
    private String userAgent;
    private String rawMessage;    // JSON-блоб от источника
    private Long timestamp;       // Unix time
}
