package org.runimo.runimo.user.service.dtos;

import java.util.List;

public record ItemQueryResponse(
    List<InventoryItem> items
) {

}
