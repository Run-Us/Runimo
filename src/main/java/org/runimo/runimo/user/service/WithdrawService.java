package org.runimo.runimo.user.service;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final UserRepository userRepository;

    @Transactional
    public void execute(Long userId) {
        User savedUser = userRepository.findById(userId)
            .orElseThrow(NoSuchElementException::new);
        userRepository.delete(savedUser);
    }
}
