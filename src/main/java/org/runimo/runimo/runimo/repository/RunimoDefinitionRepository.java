package org.runimo.runimo.runimo.repository;

import java.util.List;
import java.util.Optional;
import org.runimo.runimo.item.domain.EggType;
import org.runimo.runimo.runimo.domain.RunimoDefinition;
import org.runimo.runimo.runimo.service.dto.RunimoTypeSimpleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RunimoDefinitionRepository extends JpaRepository<RunimoDefinition, Long> {

    Optional<RunimoDefinition> findByCode(String runimoCode);

    @Query("""
        select rd.id from RunimoDefinition rd
        where rd.type in :eggTypes
        """)
    List<Long> findIdInEggTypes(List<EggType> eggTypes);

    @Query("""
            select new org.runimo.runimo.runimo.service.dto.RunimoTypeSimpleModel(
              rd.type.id, rd.code, rd.name, rd.imgUrl, rd.description
            ) from RunimoDefinition rd
            where rd.type in :eggTypes
        """)
    List<RunimoTypeSimpleModel> findRunimoSimpleTypeModelByType(List<EggType> eggTypes);
}
