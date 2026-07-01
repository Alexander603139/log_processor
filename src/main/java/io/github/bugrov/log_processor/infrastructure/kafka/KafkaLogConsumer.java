package io.github.bugrov.log_processor.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bugrov.log_processor.api.dto.CommandMessage;
import io.github.bugrov.log_processor.api.dto.LogEventMessage;
import io.github.bugrov.log_processor.domain.model.EventLog;
import io.github.bugrov.log_processor.domain.model.Rule;
import io.github.bugrov.log_processor.domain.port.outgoing.EventLogRepositoryPort;
import io.github.bugrov.log_processor.domain.port.outgoing.KafkaCommandProducerPort;
import io.github.bugrov.log_processor.domain.service.RuleEngine;
import io.github.bugrov.log_processor.infrastructure.entity.RuleEntity;
import io.github.bugrov.log_processor.infrastructure.repository.JpaRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaLogConsumer {

    private final EventLogRepositoryPort repository;
    private final JpaRuleRepository ruleRepository;
    private final RuleEngine ruleEngine;
    private final KafkaCommandProducerPort commandProducer;

    @KafkaListener(topics = "${app.kafka.topic.raw-events}", groupId = "${app.kafka.consumer.group-id}")
    public void consume(LogEventMessage message) {
        log.info("Received event from Kafka: {}", message);

        // 1. Преобразуем в доменную модель
        EventLog eventLog = EventLog.builder()
                .id(message.getId() != null ? message.getId() : UUID.randomUUID().toString())
                .source(message.getSource())
                .ipAddress(message.getIpAddress())
                .userAgent(message.getUserAgent())
                .rawMessage(message.getRawMessage())
                .timestamp(message.getTimestamp() != null ? message.getTimestamp() : Instant.now())
                .build();

        // 2. Загружаем активные правила из БД
        List<RuleEntity> ruleEntities = ruleRepository.findByEnabledTrue();
        List<Rule> rules = ruleEntities.stream()
                .map(entity -> {
                    try {
                        return Rule.builder()
                                .id(entity.getId())
                                .name(entity.getName())
                                .description(entity.getDescription())
                                .conditionType(entity.getConditionType())
                                .conditionConfig(entity.getConditionConfig() == null ? null : new ObjectMapper().readTree(entity.getConditionConfig()))
                                .action(entity.getAction())
                                .enabled(entity.getEnabled())
                                .build();
                    } catch (JsonProcessingException e) {
                        log.error("Failed to parse conditionConfig for rule {}: {}", entity.getId(), entity.getConditionConfig(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.info("Loaded {} active rules: {}", rules.size(), rules);

        // 3. Применяем правила
        boolean isAlert = ruleEngine.evaluate(eventLog, rules);

        // 4. Если тревога – отправляем команду
        if (isAlert) {
            CommandMessage command = CommandMessage.builder()
                    .ipAddress(eventLog.getIpAddress())
                    .action("BLOCK")
                    .reason("Suspicious activity detected")
                    .ruleId(rules.stream().filter(Rule::isEnabled).findFirst().map(Rule::getId).orElse(null))
                    .build();
            commandProducer.sendCommand(command);
            log.warn("ALERT triggered for event: {}", eventLog);
        }

        // 5. Сохраняем в БД
        log.info("Calling repository.save for event: {}", eventLog);
        repository.save(eventLog);
    }
}