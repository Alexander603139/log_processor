package io.github.bugrov.log_processor.infrastructure.synthetic;

import net.datafaker.Faker;
import io.github.bugrov.log_processor.api.dto.LogEventMessage;
import io.github.bugrov.log_processor.domain.config.SyntheticProperties;
import io.github.bugrov.log_processor.domain.port.outgoing.KafkaProducerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "synthetic.generator.enabled", havingValue = "true")
public class SyntheticDataGenerator {

    private final KafkaProducerPort kafkaProducer;
    private final SyntheticProperties properties;
    private final Faker faker = new Faker();

    // Фоновый шум – каждые N мс
    @Scheduled(fixedDelayString = "${synthetic.generator.background-interval-ms:7000}")
    public void generateBackgroundEvent() {
        LogEventMessage event = buildRandomEvent(false);
        kafkaProducer.sendEvent(event);
        log.info("Generated background event: {}", event);
    }

    // Аномалия – всплеск событий
    @Scheduled(fixedDelayString = "${synthetic.generator.anomaly-interval-ms:120000}")
    public void generateAnomalyBurst() {
        int burst = properties.getAnomalyBurstSize();
        log.info("Generating anomaly burst of {} events", burst);
        for (int i = 0; i < burst; i++) {
            LogEventMessage event = buildRandomEvent(true);
            kafkaProducer.sendEvent(event);
        }
    }

    private LogEventMessage buildRandomEvent(boolean isAnomaly) {
        String ip = faker.internet().ipV4Address();
        String source = faker.options().option("NGINX", "APP", "SYSTEM", "PYTHON_APP");
        String userAgent = faker.internet().userAgent(); // исправлено!
        String rawMessage;
        if (isAnomaly || ThreadLocalRandom.current().nextDouble() < 0.1) {
            rawMessage = "{\"error\":\"" + faker.lorem().sentence(3) + "\"}";
        } else {
            rawMessage = "{\"info\":\"" + faker.lorem().sentence(5) + "\"}";
        }

        return LogEventMessage.builder()
                .id(null)
                .source(source)
                .ipAddress(ip)
                .userAgent(userAgent)
                .rawMessage(rawMessage)
                .timestamp(Instant.now())
                .build();
    }
}