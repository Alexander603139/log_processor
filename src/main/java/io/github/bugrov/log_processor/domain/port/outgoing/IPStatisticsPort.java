package io.github.bugrov.log_processor.domain.port.outgoing;

import java.time.Instant;

public interface IPStatisticsPort {
    long countEventsByIP(String ipAddress, Instant since);
}