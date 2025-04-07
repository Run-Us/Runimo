package org.runimo.runimo.runimo.repository;

import org.runimo.runimo.runimo.domain.Runimo;
import org.runimo.runimo.runimo.service.model.RunimoSimpleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RunimoRepository extends JpaRepository<Runimo, Long> {

    boolean existsByUserIdAndRunimoDefinitionId(Long UserId, Long runimoDefinitionId);

    @Query("""
        select new org.runimo.runimo.runimo.service.model.RunimoSimpleModel(r.id, r.name, r.imgUrl, r.code, r.type, r.description)
        from Runimo ur
        join RunimoDefinition r on r.id = ur.runimoDefinitionId
        where ur.userId = :userId
    """)
    List<RunimoSimpleModel> findAllByUserId(@Param("userId") Long userId);
}
