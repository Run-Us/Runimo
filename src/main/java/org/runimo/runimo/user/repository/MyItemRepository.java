package org.runimo.runimo.user.repository;

import java.util.List;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.service.dtos.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MyItemRepository extends JpaRepository<UserItem, Integer> {

    @Query(
        "SELECT new org.runimo.runimo.user.service.dtos.InventoryItem(i.itemId, it.name, i.quantity, it.imgUrl) "
            +
            "FROM UserItem i" +
            " join Item it on i.itemId = it.id" +
            " WHERE i.userId = :userId")
    List<InventoryItem> findInventoryItemsByUserId(Long userId);

    @Query(
        "SELECT new org.runimo.runimo.user.service.dtos.InventoryItem(it.itemId, e.name, it.quantity, e.imgUrl) "
            +
            "FROM UserItem it" +
            " join Egg e on it.itemId = e.id" +
            " WHERE it.userId = :userId")
    List<InventoryItem> findMyEggsByUserId(Long userId);
}
