package org.runimo.runimo.user.repository;

import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.service.dtos.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyItemRepository extends JpaRepository<UserItem, Integer> {

  @Query("SELECT new org.runimo.runimo.user.service.dtos.InventoryItem(i.itemId, it.name, i.quantity, it.imgUrl) " +
      "FROM UserItem i" +
      " join Item it on i.itemId = it.id" +
      " WHERE i.userId = :userId")
  List<InventoryItem> findInventoryItemsByUserId(Long userId);
}
