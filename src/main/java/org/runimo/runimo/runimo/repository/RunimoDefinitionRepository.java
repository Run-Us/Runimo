package org.runimo.runimo.runimo.repository;

import java.util.Optional;
import org.runimo.runimo.runimo.domain.RunimoDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunimoDefinitionRepository extends JpaRepository<RunimoDefinition, Long> {

    Optional<RunimoDefinition> findByCode(String runimoCode);
}
