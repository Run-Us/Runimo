package org.runimo.runimo.runimo.repository;

import java.util.List;
import java.util.Optional;
import org.runimo.runimo.runimo.domain.Runimo;
import org.runimo.runimo.runimo.service.dtos.MainRunimoStat;
import org.runimo.runimo.runimo.service.dtos.RunimoSimpleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RunimoRepository extends JpaRepository<Runimo, Long> {

    boolean existsByUserIdAndRunimoDefinitionId(Long UserId, Long runimoDefinitionId);

    @Query("""
            select new org.runimo.runimo.runimo.service.dtos.RunimoSimpleModel(r.id, rd.code, r.totalRunCount, r.totalDistanceInMeters)
            from Runimo r
            join RunimoDefinition rd on rd.id = r.runimoDefinitionId
            where r.userId = :userId
        """)
    List<RunimoSimpleModel> findAllByUserId(@Param("userId") Long userId);

    @Query("""
            select new org.runimo.runimo.runimo.service.dtos.MainRunimoStat(rd.name, rd.imgUrl, r.totalRunCount, r.totalDistanceInMeters)
                  from Runimo r
                  join RunimoDefinition rd on rd.id = r.runimoDefinitionId
                  where r.id = :runimoId
        """)
    Optional<MainRunimoStat> findMainRunimoStatByRunimoId(Long runimoId);
}
