package org.runimo.runimo.rewards;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.CleanUpUtil;
import org.runimo.runimo.auth.domain.SignupToken;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.repository.SignupTokenRepository;
import org.runimo.runimo.auth.service.SignUpUsecase;
import org.runimo.runimo.auth.service.dto.UserSignupCommand;
import org.runimo.runimo.common.scale.Distance;
import org.runimo.runimo.common.scale.Pace;
import org.runimo.runimo.records.service.usecases.RecordCreateUsecase;
import org.runimo.runimo.records.service.usecases.dtos.RecordCreateCommand;
import org.runimo.runimo.records.service.usecases.dtos.RecordSaveResponse;
import org.runimo.runimo.rewards.service.RewardService;
import org.runimo.runimo.rewards.service.dto.RewardClaimCommand;
import org.runimo.runimo.rewards.service.dto.RewardResponse;
import org.runimo.runimo.user.domain.DevicePlatform;
import org.runimo.runimo.user.domain.Gender;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.repository.UserRepository;
import org.runimo.runimo.user.service.UserItemFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@SpringBootTest
class RewardTest {

    @Autowired
    private SignUpUsecase signUpUsecaseImpl;
    @Autowired
    private RewardService rewardService;

    @Autowired
    private RecordCreateUsecase recordCreateUsecase;

    private User savedUser;
    @Autowired
    private UserItemFinder userItemFinder;
    @Autowired
    private CleanUpUtil cleanUpUtil;
    @Autowired
    private JwtTokenFactory jwtTokenFactory;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SignupTokenRepository signupTokenRepository;

    @BeforeEach
    void setUp() {
        //given
        String key = UUID.randomUUID().toString();

        String registerToken = jwtTokenFactory.generateSignupTemporalToken(
            "test-pid",
            SocialProvider.KAKAO,
            key
        );
        signupTokenRepository.save(new SignupToken(
            key,
            "test-pid",
            null,
            SocialProvider.KAKAO
        ));
        UserSignupCommand command = new UserSignupCommand(registerToken, "nickname", null,
            Gender.UNKNOWN,
            "device_token", DevicePlatform.APNS);
        Long useId = signUpUsecaseImpl.register(command).userId();
        savedUser = userRepository.findById(useId).orElse(null);
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
            1800L,
            new Pace(1909L),
            new Distance(10000L),
            List.of()
        );
        RecordSaveResponse response = recordCreateUsecase.execute(recordCreateCommand);
        RewardClaimCommand rewardClaimCommand = new RewardClaimCommand(savedUser.getId(),
            response.savedId());
        RewardResponse rewardResponse = rewardService.claimReward(rewardClaimCommand);

        UserItem savedItem = userItemFinder.
            findEggByUserIdAndEggCode(
                savedUser.getId(),
                rewardResponse.eggCode())
            .orElse(null);

        assertNotNull(rewardResponse.eggCode());
        assertNotNull(savedItem);
        assertNotEquals(0, savedItem.getQuantity());
    }
}
