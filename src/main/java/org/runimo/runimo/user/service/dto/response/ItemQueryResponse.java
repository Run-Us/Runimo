package org.runimo.runimo.user.service.dto.response;

import java.util.List;
import org.runimo.runimo.user.service.dto.InventoryItem;

public record ItemQueryResponse(
    List<InventoryItem> items
) {

}
