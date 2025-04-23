package org.runimo.runimo.item.repository;

import java.util.List;
import org.runimo.runimo.item.domain.EggType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EggTypeRepository extends JpaRepository<EggType, Long> {

    @Query("select et from EggType et order by et.id")
    List<EggType> findAllOrderById();

    List<EggType> findEggTypeByRequiredDistanceInMetersLessThanEqual(Long totalDistanceInMeters);

    List<EggType> findEggTypeByLevelLessThan(Integer level);
}