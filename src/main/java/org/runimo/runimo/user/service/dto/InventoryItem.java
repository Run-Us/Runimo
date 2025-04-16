package org.runimo.runimo.user.service.dto;

public record InventoryItem(
    Long itemId,
    String name,
    Long amount,
    String imgUrl
) {

}
