package org.runimo.runimo.user.service.usecases.items;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.item.service.ItemActivityCreator;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.service.UserItemFinder;
import org.runimo.runimo.user.service.dto.command.UseItemCommand;
import org.runimo.runimo.user.service.dto.response.UseItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UseItemUsecaseImpl implements UseItemUsecase {

    private final UserItemFinder userItemFinder;
    private final ItemActivityCreator itemActivityCreator;

    @Override
    @Transactional
    public UseItemResponse useItem(UseItemCommand command) {
        UserItem userItem = userItemFinder.findByUserIdAndItemIdWithXLock(command.userId(),
                command.itemId())
            .orElseThrow(NoSuchElementException::new);
        userItem.useItem(command.quantity());
        itemActivityCreator.createItemActivity(UseItemCommand.toItemUseActivityCommand(command));
        return new UseItemResponse(userItem.getItemId(), userItem.getQuantity());
    }
}
