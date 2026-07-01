package io.github.bugrov.log_processor.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.bugrov.log_processor.domain.model.EventLog;
import io.github.bugrov.log_processor.domain.model.Rule;
import io.github.bugrov.log_processor.domain.port.outgoing.IPStatisticsPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuleEngine {

    private final IPStatisticsPort ipStatisticsPort;

    public boolean evaluate(EventLog eventLog, List<Rule> rules) {
        for (Rule rule : rules) {
            if (!rule.isEnabled()) continue;
            if (applyRule(eventLog, rule)) {
                return true; // любое сработавшее правило вызывает ALERT
            }
        }
        return false;
    }

    private boolean applyRule(EventLog eventLog, Rule rule) {
        String conditionType = rule.getConditionType();
        JsonNode config = rule.getConditionConfig();

        if ("RATE_LIMIT".equals(conditionType)) {
            int threshold = config.get("threshold").asInt();
            int windowSeconds = config.get("windowSeconds").asInt();
            Instant since = Instant.now().minusSeconds(windowSeconds);
            long count = ipStatisticsPort.countEventsByIP(eventLog.getIpAddress(), since);
            return count >= threshold;
        }
        // Можно добавить другие типы правил (IP_BLACKLIST и т.д.)
        return false;
    }
}