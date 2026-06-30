package io.github.bugrov.log_processor.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandMessage {
    private String ipAddress;
    private String action;      // например, "BLOCK", "ALERT"
    private String reason;
    private String ruleId;
}
