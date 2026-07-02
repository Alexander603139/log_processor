package io.github.bugrov.log_processor.domain.service;

import io.github.bugrov.log_processor.infrastructure.repository.JpaEventLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class CleanupService {

    private final JpaEventLogRepository repository;

    @Scheduled(cron = "0 0 3 * * ?") // каждую ночь в 3:00
    @Transactional
    public void cleanOldLogs() {
        Instant cutoff = Instant.now().minusSeconds(7 * 24 * 60 * 60);
        int deleted = repository.deleteOlderThan(cutoff);
        log.info("Deleted {} old log records", deleted);
    }
}