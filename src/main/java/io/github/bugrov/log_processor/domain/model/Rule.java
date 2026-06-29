package io.github.bugrov.log_processor.domain.model;

import lombok.Builder;
import lombok.Getter;
import com.fasterxml.jackson.databind.JsonNode;

@Getter
@Builder
public class Rule {
    private String id;
    private String name;
    private String description;
    private String conditionType;   // 'RATE_LIMIT', 'IP_BLACKLIST'
    private JsonNode conditionConfig;
    private String action;          // 'ALERT', 'BLOCK', 'LOG'
    private boolean enabled;
}
