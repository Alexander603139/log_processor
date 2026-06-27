package io.github.bugrov.log_processor.domain.port.outgoing;

import io.github.bugrov.log_processor.domain.model.EventLog;

public interface EventLogRepositoryPort {
    void save(EventLog eventLog); // Синхронное сохранение
}
