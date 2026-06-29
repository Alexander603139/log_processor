CREATE TABLE IF NOT EXISTS rules (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    condition_type VARCHAR(50) NOT NULL, -- например, 'RATE_LIMIT', 'IP_BLACKLIST'
    condition_config JSONB NOT NULL,     -- параметры правила, например, {"threshold": 5, "windowSeconds": 60}
    action VARCHAR(50) NOT NULL,         -- 'ALERT', 'BLOCK', 'LOG'
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Добавим индекс для быстрого поиска активных правил
CREATE INDEX idx_rules_enabled ON rules(enabled);