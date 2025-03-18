package org.runimo.runimo.item.repository;

import org.runimo.runimo.item.domain.ItemActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemActivityRepository extends JpaRepository<ItemActivity, Long> {
}
