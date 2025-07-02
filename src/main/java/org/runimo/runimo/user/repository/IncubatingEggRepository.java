package org.runimo.runimo.user.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import org.runimo.runimo.user.domain.IncubatingEgg;
import org.runimo.runimo.user.service.dto.IncubatingEggView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface IncubatingEggRepository extends JpaRepository<IncubatingEgg, Integer> {

    Optional<IncubatingEgg> findById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
    @Query("select ie from IncubatingEgg ie where ie.userId = :userId and ie.id = :eggId")
    Optional<IncubatingEgg> findByUserIdAndEggIdForUpdate(Long userId, Long eggId);

    @Query("select ie from IncubatingEgg ie where ie.userId = :userId and (ie.status = 'INCUBATING' or ie.status = 'INCUBATED')")
    List<IncubatingEgg> findAllByUserId(Long userId);

    @Query(
        "select new org.runimo.runimo.user.service.dto.IncubatingEggView(ie.id, e.name, e.imgUrl, ie.hatchRequireAmount, ie.currentLovePointAmount, ie.status, e.eggType.code) "
            +
            "from IncubatingEgg ie " +
            "join Egg e on e.id = ie.eggId " +
            "where ie.userId = :userId and (ie.status = 'INCUBATING' or ie.status = 'INCUBATED')")
    List<IncubatingEggView> findAllViewByUserId(Long userId);
}
