package org.runimo.runimo.item.repository;

import java.util.List;
import org.runimo.runimo.item.domain.EggType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EggTypeRepository extends JpaRepository<EggType, Long> {

    List<EggType> findAllByOrderByIdAsc();

    List<EggType> findEggTypeByRequiredDistanceInMetersLessThanEqual(Long totalDistanceInMeters);

    List<EggType> findEggTypeByLevelLessThan(Integer level);
}