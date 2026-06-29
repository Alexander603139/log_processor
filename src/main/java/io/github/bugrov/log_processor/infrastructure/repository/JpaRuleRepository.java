package io.github.bugrov.log_processor.infrastructure.repository;

import io.github.bugrov.log_processor.infrastructure.entity.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaRuleRepository extends JpaRepository<RuleEntity, String> {
    List<RuleEntity> findByEnabledTrue();
}