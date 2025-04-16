package org.runimo.runimo.user.service.usecases.query;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.repository.MyItemRepository;
import org.runimo.runimo.user.service.dtos.InventoryItem;
import org.runimo.runimo.user.service.dtos.response.ItemQueryResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyItemQueryUsecaseImpl implements MyItemQueryUsecase {

    private final MyItemRepository myItemRepository;

    @Override
    public ItemQueryResponse queryMyEggs(Long userId) {
        List<InventoryItem> myItems = myItemRepository.findMyEggsByUserId(userId);
        return new ItemQueryResponse(myItems);
    }

    @Override
    public ItemQueryResponse queryMyAllItems(Long userId) {
        List<InventoryItem> myItems = myItemRepository.findInventoryItemsByUserId(userId);
        return new ItemQueryResponse(myItems);
    }
}
