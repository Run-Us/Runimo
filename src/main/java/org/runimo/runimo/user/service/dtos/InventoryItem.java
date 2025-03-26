package org.runimo.runimo.user.service.dtos;

public record InventoryItem(
    Long itemId,
    String name,
    Long amount,
    String imgUrl
) {
}
