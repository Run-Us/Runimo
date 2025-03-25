package org.runimo.runimo.user.service.usecases;

import org.junit.jupiter.api.Test;
import org.runimo.runimo.item.repository.ItemRepository;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.repository.UserItemRepository;
import org.runimo.runimo.user.service.dtos.UserSignupCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
class UserRegisterServiceTest {

  @Autowired
  private UserRegisterService userRegisterService;

  @Autowired
  private UserItemRepository userItemRepository;
  @Autowired
  private ItemRepository itemRepository;

  @Test
  void 회원가입_알_지급_테스트() {
    // given
    UserSignupCommand command = new UserSignupCommand("test", SocialProvider.KAKAO, "1234");
    User createdUser = userRegisterService.register(command, SocialProvider.KAKAO, "1234");
    UserItem ui = userItemRepository.findByUserIdAndItemId(createdUser.getId(), 1L).get();
    assertNotNull(ui);
    assertEquals(1L, ui.getQuantity());
  }
}