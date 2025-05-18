package org.runimo.runimo.user.service.usecases;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.runimo.runimo.user.domain.Gender;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.UserCreator;
import org.runimo.runimo.user.service.UserItemCreator;
import org.runimo.runimo.user.service.UserRegisterService;
import org.runimo.runimo.user.service.dto.command.UserRegisterCommand;

class UserRegisterServiceTest {

    @Mock
    private UserCreator userCreator;

    @Mock
    private UserItemCreator userItemCreator;

    @InjectMocks
    private UserRegisterService userRegisterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 회원가입_알_지급_테스트() {
        // given
        String providerId = "providerId";
        UserRegisterCommand command =
            new UserRegisterCommand(
                "test-nickname",
                "https://test.com",
                Gender.UNKNOWN,
                providerId,
                SocialProvider.KAKAO
            );
        User mockUser = mock(User.class);
        when(userCreator.createUser(any())).thenReturn(mockUser);

        // when
        User res = userRegisterService.registerUser(command);

        // then
        assertNotNull(res);
        verify(userCreator, times(1)).createUser(any());
        verify(userCreator, times(1)).createUserOAuthInfo(mockUser, SocialProvider.KAKAO,
            providerId);
        verify(userCreator, times(1)).createLovePoint(anyLong());
        verify(userItemCreator, times(1)).createAll(anyLong());
    }
}
