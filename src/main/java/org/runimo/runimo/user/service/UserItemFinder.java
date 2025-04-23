package org.runimo.runimo.user.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.repository.UserItemRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserItemFinder {

    private final UserItemRepository userItemRepository;

    @Transactional(readOnly = true)
    public List<UserItem> findEggsByUserId(Long userId) {
        return userItemRepository.findAllEggsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Optional<UserItem> findEggByUserIdAndEggCode(Long userId, String eggCode) {
        return userItemRepository.findByUserIdAndEggCode(userId, eggCode);
    }

    @Transactional
    public Optional<UserItem> findByUserIdAndItemIdWithXLock(Long userId, Long itemId) {
        return userItemRepository.findByUserIdAndItemIdForUpdate(userId, itemId);
    }
}
