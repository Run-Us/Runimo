package org.runimo.runimo.rewards.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.runimo.runimo.common.scale.Distance;
import org.runimo.runimo.item.domain.Egg;
import org.runimo.runimo.item.domain.EggType;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.service.RecordFinder;
import org.runimo.runimo.rewards.service.dto.RewardClaimCommand;
import org.runimo.runimo.rewards.service.dto.RewardResponse;
import org.runimo.runimo.rewards.service.eggs.EggGrantService;
import org.runimo.runimo.rewards.service.lovepoint.LoveGrantService;
import org.runimo.runimo.user.UserFixtures;
import org.runimo.runimo.user.service.UserFinder;
import org.springframework.test.util.ReflectionTestUtils;

class RewardServiceTest {

    private RewardService rewardService;
    @Mock
    private RecordFinder recordFinder;
    @Mock
    private UserFinder userFinder;
    @Mock
    private EggGrantService eggGrantService;
    @Mock
    private LoveGrantService loveGrantService;

    private static RunningRecord getRunningRecordWithIds(Long userId, Long recordId,
        boolean isRewarded) {
        RunningRecord runningRecord = RunningRecord.builder()
            .userId(userId)
            .isRewarded(isRewarded)
            .totalDistance(new Distance(1000L))
            .startedAt(LocalDateTime.now())
            .build();
        ReflectionTestUtils.setField(runningRecord, "id", recordId);
        ReflectionTestUtils.setField(runningRecord, "recordPublicId",
            UUID.randomUUID().toString());
        return runningRecord;
    }

    @BeforeEach
    void setUp() {
        openMocks(this);
        rewardService = new RewardService(recordFinder, userFinder, eggGrantService,
            loveGrantService);
        when(userFinder.findUserById(any())).thenReturn(
            Optional.of(UserFixtures.getUserWithId(1L)));
    }

    @Test
    void 보상을_받지_않은_이번주_첫_기록이라면_알을_지급한다() {
        RunningRecord unRewardedRecord = getRunningRecordWithIds(1L, 1L, false);
        RewardClaimCommand command = new RewardClaimCommand(1L, unRewardedRecord.getRecordPublicId());

        when(recordFinder.findByPublicId(any())).thenReturn(java.util.Optional.of(unRewardedRecord));
        when(recordFinder.findFirstRunOfCurrentWeek(any())).thenReturn(
            java.util.Optional.of(unRewardedRecord));
        when(eggGrantService.grantRandomEggToUser(any())).thenReturn(
            Egg.builder().eggType(EggType.of(
                "MADANG",
                "hi",
                0L,
                0
            )).build());
        when(loveGrantService.grantLoveToUserWithDistance(any())).thenReturn(10L);
        RewardResponse res = rewardService.claimReward(command);
        assertNotNull(res);
        assertTrue(res.isRewarded());
        assertEquals("MADANG", res.eggType());
        assertNotEquals(0, res.lovePointAmount());
    }

    @Test
    void 이미_보상을_받은_기록이면_예외를_던진다() {
        RunningRecord alreadyRewardedRecord = getRunningRecordWithIds(1L, 1L, true);
        RewardClaimCommand command = new RewardClaimCommand(1L,
            alreadyRewardedRecord.getRecordPublicId());

        when(recordFinder.findByPublicId(any())).thenReturn(java.util.Optional.of(alreadyRewardedRecord));
        when(recordFinder.findFirstRunOfCurrentWeek(any())).thenReturn(
            java.util.Optional.of(alreadyRewardedRecord));
        when(eggGrantService.grantRandomEggToUser(any())).thenReturn(
            Egg.builder().eggType(EggType.of(
                "MADANG",
                "hi",
                0L,
                0
            )).build());
        when(loveGrantService.grantLoveToUserWithDistance(any())).thenReturn(10L);

        assertThrows(IllegalStateException.class, () -> rewardService.claimReward(command));
    }

    @Test
    void 이번주_첫_기록이_아니면_알을_지급하지_않는다() {
        RunningRecord unRewardedRecord = getRunningRecordWithIds(1L, 1L, false);
        RunningRecord anotherRecord = getRunningRecordWithIds(1L, 2L, false);
        RewardClaimCommand command = new RewardClaimCommand(1L, unRewardedRecord.getRecordPublicId());

        when(recordFinder.findByPublicId(any())).thenReturn(java.util.Optional.of(unRewardedRecord));
        when(recordFinder.findFirstRunOfCurrentWeek(any())).thenReturn(Optional.of(anotherRecord));
        when(loveGrantService.grantLoveToUserWithDistance(any())).thenReturn(10L);

        RewardResponse res = rewardService.claimReward(command);
        verify(eggGrantService, never()).grantRandomEggToUser(any());
        assertEquals(false, res.isRewarded());
        assertEquals(Egg.EMPTY.getItemCode(), res.eggCode());
        assertEquals(Egg.EMPTY.getName(), res.eggType());
    }
}