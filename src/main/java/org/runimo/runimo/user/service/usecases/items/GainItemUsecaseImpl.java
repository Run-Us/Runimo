package org.runimo.runimo.user.service.usecases.items;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.repository.UserItemRepository;
import org.runimo.runimo.user.service.UserItemFinder;
import org.runimo.runimo.user.service.dtos.command.GainItemCommand;
import org.runimo.runimo.user.service.dtos.response.GainItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GainItemUsecaseImpl implements GainItemUsecase {

    private final UserItemFinder userItemFinder;
    private final UserItemRepository userItemRepository;

    @Override
    @Transactional
    public GainItemResponse gainItem(GainItemCommand command) {
        UserItem userItem = userItemFinder.findByUserIdAndItemIdWithXLock(command.userId(),
                command.itemId())
            .orElseThrow(NoSuchElementException::new);
        userItem.gainItem(command.quantity());
        userItemRepository.save(userItem);
        return new GainItemResponse(
            userItem.getItemId(),
            userItem.getQuantity()
        );
    }
}
