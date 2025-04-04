package org.runimo.runimo.runimo.repository;

import org.runimo.runimo.runimo.domain.UserRunimo;
import org.runimo.runimo.runimo.service.model.RunimoSimpleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRunimoRepository extends JpaRepository<UserRunimo, Long> {

    Optional<UserRunimo> findByRunimoId(Long runimoId);

    @Query("""
        select new org.runimo.runimo.runimo.service.model.RunimoSimpleModel(r.id, r.name, r.imgUrl, r.code, r.type, r.description)
        from UserRunimo ur
        join Runimo r on r.id = ur.runimoId
        where ur.userId = :userId
    """)
    List<RunimoSimpleModel> findAllByUserId(@Param("userId") Long userId);
}
