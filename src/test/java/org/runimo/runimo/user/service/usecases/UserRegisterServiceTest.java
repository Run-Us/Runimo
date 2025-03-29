package org.runimo.runimo.user.service.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.runimo.runimo.rewards.service.eggs.EggGrantService;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.UserCreator;
import org.runimo.runimo.user.service.UserItemCreator;
import org.runimo.runimo.user.service.dtos.UserSignupCommand;
import org.runimo.runimo.user.service.usecases.auth.UserRegisterService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserRegisterServiceTest {

  @Mock
  private UserCreator userCreator;

  @Mock
  private UserItemCreator userItemCreator;

  @Mock
  private EggGrantService eggGrantService;

  @InjectMocks
  private UserRegisterService userRegisterService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void 회원가입_알_지급_테스트() {
    // given
    UserSignupCommand command = new UserSignupCommand("test", SocialProvider.KAKAO, "1234");
    User mockUser = mock(User.class);
    when(userCreator.createUser(any(UserSignupCommand.class))).thenReturn(mockUser);

    // when
    User createdUser = userRegisterService.register(command, "1234");

    // then
    assertNotNull(createdUser);
    verify(userCreator, times(1)).createUser(command);
    verify(userCreator, times(1)).createUserOAuthInfo(mockUser, eq(SocialProvider.KAKAO), eq("1234"));
    verify(userCreator, times(1)).createLovePoint(anyLong());
    verify(userItemCreator, times(1)).createAll(anyLong());
    verify(eggGrantService, times(1)).grantGreetingEggToUser(mockUser);
  }
}
