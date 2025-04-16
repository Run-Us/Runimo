package org.runimo.runimo.user.service.dtos.response;

import java.util.List;
import org.runimo.runimo.user.service.dtos.InventoryItem;

public record ItemQueryResponse(
    List<InventoryItem> items
) {

}
