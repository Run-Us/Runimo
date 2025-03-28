package org.runimo.runimo.user.service.usecases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.item.repository.ItemRepository;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.repository.UserItemRepository;
import org.runimo.runimo.user.service.dtos.UserSignupCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

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
  @Autowired
  private CleanUpUtil cleanUpUtil;

  @AfterEach
  void tearDown() {
    cleanUpUtil.cleanUpUserInfos();
  }

  @Test
  @Sql(scripts = "/sql/egg_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void 회원가입_알_지급_테스트() {
    // given
    UserSignupCommand command = new UserSignupCommand("test", SocialProvider.KAKAO, "1234");
    User createdUser = userRegisterService.register(command, "1234");
    UserItem ui = userItemRepository.findByUserIdAndItemId(createdUser.getId(), 1L).get();
    assertNotNull(ui);
    assertEquals(1L, ui.getQuantity());
  }
}