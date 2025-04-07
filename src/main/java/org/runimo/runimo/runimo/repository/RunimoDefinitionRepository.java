package org.runimo.runimo.runimo.repository;

import org.runimo.runimo.runimo.domain.RunimoDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RunimoDefinitionRepository extends JpaRepository<RunimoDefinition, Long> {
    Optional<RunimoDefinition> findByCode(String runimoCode);
}
