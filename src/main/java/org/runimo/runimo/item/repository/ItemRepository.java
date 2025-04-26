package org.runimo.runimo.item.repository;

import java.util.List;
import java.util.Optional;
import org.runimo.runimo.item.domain.Egg;
import org.runimo.runimo.item.domain.EggType;
import org.runimo.runimo.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i.id from Item i")
    List<Long> findAllItemIds();

    @Query("select e from Egg e where e.eggType = :eggType")
    Optional<Egg> findByEggType(EggType eggType);
}
