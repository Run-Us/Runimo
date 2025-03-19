package org.runimo.runimo.item.repository;

import org.runimo.runimo.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

  @Query("select i.id from Item i")
  List<Long> findAllItemIds();
}
