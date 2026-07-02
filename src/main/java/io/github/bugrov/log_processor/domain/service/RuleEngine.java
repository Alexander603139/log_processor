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
        log.info("=== EVALUATING {} rules for IP: {}", rules.size(), eventLog.getIpAddress());
        for (Rule rule : rules) {
            if (!rule.isEnabled()) continue;
            log.info("=== Checking rule: id={}, type={}, config={}", rule.getId(), rule.getConditionType(), rule.getConditionConfig());
            if (applyRule(eventLog, rule)) {
                log.info("=== RULE TRIGGERED: {}", rule.getName());
                return true;
            }
        }
        log.info("=== NO RULE TRIGGERED");
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
            log.info("=== Rate limit: threshold={}, count={}, window={}s", threshold, count, windowSeconds);
            return count >= threshold;
        }
        return false;
    }
}