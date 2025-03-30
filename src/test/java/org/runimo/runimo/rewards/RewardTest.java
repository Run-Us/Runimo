package org.runimo.runimo.rewards;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.common.scale.Distance;
import org.runimo.runimo.common.scale.Pace;
import org.runimo.runimo.records.service.usecases.RecordCreateUsecase;
import org.runimo.runimo.records.service.usecases.dtos.RecordCreateCommand;
import org.runimo.runimo.records.service.usecases.dtos.RecordSaveResponse;
import org.runimo.runimo.rewards.service.RewardService;
import org.runimo.runimo.rewards.service.dtos.RewardClaimCommand;
import org.runimo.runimo.rewards.service.dtos.RewardResponse;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.service.UserItemFinder;
import org.runimo.runimo.user.service.dtos.UserSignupCommand;
import org.runimo.runimo.user.service.usecases.auth.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
class RewardTest {

  @Autowired
  private UserRegisterService userRegisterService;
  @Autowired
  private RewardService rewardService;

  @Autowired
  private RecordCreateUsecase recordCreateUsecase;

  private User savedUser;
  @Autowired
  private UserItemFinder userItemFinder;
  @Autowired
  private CleanUpUtil cleanUpUtil;

  @BeforeEach
  void setUp() {
    //given
    UserSignupCommand command = new UserSignupCommand("test", SocialProvider.KAKAO, "1234");
    savedUser = userRegisterService.register(command, "1234");
  }

  @AfterEach
  void tearDown() {
    cleanUpUtil.cleanUpUserInfos();
  }

  @Test
  @Sql(scripts = "/sql/egg_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void 보상_요청_테스트() {
    RecordCreateCommand recordCreateCommand = new RecordCreateCommand(
        savedUser.getId(),
        LocalDateTime.now(),
        LocalDateTime.now().plusHours(1),
        new Pace(1909L),
        new Distance(10000L),
        List.of()
    );
    RecordSaveResponse response = recordCreateUsecase.execute(recordCreateCommand);
    RewardClaimCommand rewardClaimCommand = new RewardClaimCommand(savedUser.getId(), response.savedId());
    RewardResponse rewardResponse = rewardService.claimReward(rewardClaimCommand);

    UserItem savedItem = userItemFinder.
        findEggByUserIdAndEggType(
            savedUser.getId(),
            rewardResponse.eggType())
        .get();

    assertNotNull(rewardResponse.eggCode());
    assertNotNull(savedItem);
    assertNotEquals(0, savedItem.getQuantity());
  }
}
