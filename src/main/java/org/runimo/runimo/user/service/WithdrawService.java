package org.runimo.runimo.user.service;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.OAuthInfo;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.repository.OAuthInfoRepository;
import org.runimo.runimo.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final OAuthInfoRepository oAuthInfoRepository;
    private final UserRepository userRepository;

    @Transactional
    public void withdraw(Long userId) {
        OAuthInfo oAuthInfo = oAuthInfoRepository.findByUserId(userId)
            .orElseThrow(NoSuchElementException::new);
        User user = oAuthInfo.getUser();
        oAuthInfoRepository.delete(oAuthInfo);
        userRepository.delete(user);
    }
}
