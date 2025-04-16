package org.runimo.runimo.runimo.repository;

import java.util.List;
import java.util.Optional;
import org.runimo.runimo.runimo.domain.RunimoDefinition;
import org.runimo.runimo.runimo.service.dtos.RunimoTypeSimpleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RunimoDefinitionRepository extends JpaRepository<RunimoDefinition, Long> {

    Optional<RunimoDefinition> findByCode(String runimoCode);

    @Query("""
          select new org.runimo.runimo.runimo.service.dtos.RunimoTypeSimpleModel(rd.name, rd.imgUrl, rd.code, rd.type, rd.description)
          from RunimoDefinition rd
        """)
    List<RunimoTypeSimpleModel> findAllToSimpleModel();
}
