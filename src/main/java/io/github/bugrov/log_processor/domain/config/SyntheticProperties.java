package io.github.bugrov.log_processor.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "synthetic.generator")
public class SyntheticProperties {
    private boolean enabled = false;
    private long backgroundIntervalMs = 7000;
    private long anomalyIntervalMs = 120000;
    private int anomalyBurstSize = 10;
}