package io.github.bugrov.log_processor.api.controller;

import io.github.bugrov.log_processor.api.dto.LogRequest;
import io.github.bugrov.log_processor.domain.model.EventLog;
import io.github.bugrov.log_processor.domain.service.LogIngestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Slf4j
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class LogController {

    private final LogIngestionService ingestionService;

    @PostMapping
    public ResponseEntity<Void> ingestLog(@Valid @RequestBody LogRequest request) {
        EventLog eventLog = EventLog.builder()
                .source(request.getSource())
                .ipAddress(request.getIpAddress())
                .userAgent(request.getUserAgent())
                .rawMessage(request.getRawMessage())
                .timestamp(request.getTimestamp() != null ? Instant.ofEpochSecond(request.getTimestamp()) : Instant.now())
                .build();

        ingestionService.process(eventLog);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build(); // 202 Accepted
    }
}
