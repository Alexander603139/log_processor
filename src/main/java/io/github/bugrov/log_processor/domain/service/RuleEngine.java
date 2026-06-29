package io.github.bugrov.log_processor.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.bugrov.log_processor.domain.model.EventLog;
import io.github.bugrov.log_processor.domain.model.Rule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuleEngine {

    // Временно заглушка - позже будем запрашивать правила из БД и статистику из Mongo
    public boolean evaluate(EventLog eventLog, List<Rule> rules) {
        // Пока просто проверяем isSuspicious()
        return eventLog.isSuspicious();
    }
}
