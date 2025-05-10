package org.runimo.runimo.user.service;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.service.EncryptUtil;
import org.runimo.runimo.auth.service.login.apple.AppleTokenVerifier;
import org.runimo.runimo.user.domain.AppleUserToken;
import org.runimo.runimo.user.domain.OAuthInfo;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.repository.AppleUserTokenRepository;
import org.runimo.runimo.user.repository.OAuthInfoRepository;
import org.runimo.runimo.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final OAuthInfoRepository oAuthInfoRepository;
    private final UserRepository userRepository;
    private final AppleTokenVerifier appleTokenVerifier;
    private final AppleUserTokenRepository appleUserTokenRepository;
    private final EncryptUtil encryptUtil;

    @Transactional
    public void withdraw(Long userId) {
        OAuthInfo oAuthInfo = oAuthInfoRepository.findByUserId(userId)
            .orElseThrow(NoSuchElementException::new);
        User user = oAuthInfo.getUser();
        if (oAuthInfo.getProvider() == SocialProvider.APPLE) {
            withdrawAppleUser(user);
        }
        oAuthInfoRepository.delete(oAuthInfo);
        userRepository.delete(user);
    }

    private void withdrawAppleUser(User user) {
        AppleUserToken appleUserToken = appleUserTokenRepository
            .findByUserId(user.getId())
            .orElseThrow(NoSuchElementException::new);
        String decodedRefreshToken = getDecryptedToken(appleUserToken.getRefreshToken());
        appleTokenVerifier.revoke(decodedRefreshToken);
        appleUserTokenRepository.delete(appleUserToken);
    }

    private String getDecryptedToken(String encryptedRefreshToken) {
        try {
            return encryptUtil.decrypt(encryptedRefreshToken);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt ID token", e);
        }
    }
}
