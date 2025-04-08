package org.runimo.runimo.item.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.item.domain.ItemActivity;
import org.runimo.runimo.item.repository.ItemActivityRepository;
import org.runimo.runimo.item.service.dtos.CreateActivityCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemActivityCreatorImpl implements ItemActivityCreator {

    private final ItemActivityRepository itemActivityRepository;

    @Override
    @Transactional
    public void createItemActivity(CreateActivityCommand command) {
        ItemActivity activity = ItemActivity.builder()
            .userId(command.userId())
            .itemId(command.itemId())
            .quantity(command.quantity())
            .type(command.activityType())
            .build();
        itemActivityRepository.save(activity);
    }
}
