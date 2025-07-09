package org.runimo.runimo.user.service.usecases;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.runimo.runimo.user.UserFixtures;
import org.runimo.runimo.user.controller.request.UpdateNotificationAllowedRequst;
import org.runimo.runimo.user.domain.DevicePlatform;
import org.runimo.runimo.user.domain.UserDeviceToken;
import org.runimo.runimo.user.repository.UserDeviceTokenRepository;
import org.runimo.runimo.user.service.UserFinder;
import org.runimo.runimo.user.service.dto.command.UpdateNotificationAllowedCommand;

class UpdateUserDetailUsecaseImplTest {

    private UpdateUserDetailUsecase updateUserDetailUsecase;
    @Mock
    private UserFinder userFinder;
    @Mock
    private UserDeviceTokenRepository userDeviceTokenRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateUserDetailUsecase = new UpdateUserDetailUsecaseImpl(userFinder,
            userDeviceTokenRepository);
    }

    @Test
    void 토큰이_없으면_새로_생성() {
        // given
        given(userDeviceTokenRepository.findByUserId(any()))
            .willReturn(Optional.empty());
        given(userFinder.findUserById(any()))
            .willReturn(Optional.ofNullable(UserFixtures.getDefaultUser()));
        given(userDeviceTokenRepository.save(any()))
            .willReturn(UserDeviceToken.from("token", DevicePlatform.APNS, true));
        var command = UpdateNotificationAllowedCommand.of(
            1L,
            new UpdateNotificationAllowedRequst(true, "token", "APNS")
        );

        // when
        updateUserDetailUsecase.updateUserNotificationAllowed(command);

        // then
        verify(userDeviceTokenRepository, times(1)).save(any());
    }

    @Test
    void 토큰_있으면_생성안함() {
        given(userDeviceTokenRepository.findByUserId(any()))
            .willReturn(
                Optional.ofNullable(UserDeviceToken.from("token", DevicePlatform.APNS, true)));
        given(userFinder.findUserById(any()))
            .willReturn(Optional.ofNullable(UserFixtures.getDefaultUser()));
        given(userDeviceTokenRepository.save(any()))
            .willReturn(UserDeviceToken.from("token", DevicePlatform.APNS, true));
        var command = UpdateNotificationAllowedCommand.of(
            1L,
            new UpdateNotificationAllowedRequst(true, "token", "APNS")
        );

        // when
        updateUserDetailUsecase.updateUserNotificationAllowed(command);

        // then
        verify(userDeviceTokenRepository, times(0)).save(any());
    }

}