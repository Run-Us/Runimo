package org.runimo.runimo.rewards.service.lovepoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.runimo.runimo.common.scale.Distance;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.user.UserFixtures;
import org.runimo.runimo.user.service.LovePointProcessor;
import org.runimo.runimo.user.service.UserFinder;

class LoveGrantServiceTest {


    @InjectMocks
    private LoveGrantService loveGrantService;
    @Mock
    private UserFinder userFinder;
    @Mock
    private LovePointProcessor lovePointProcessor;

    @BeforeEach
    void setup() {
        initMocks(this);
    }

    @Test
    void km_당_1포인트를_지급한다() {

        RunningRecord record = RunningRecord.builder()
            .isRewarded(false)
            .startedAt(LocalDateTime.of(2020, 1, 1, 12, 0, 0))
            .endAt(LocalDateTime.of(2020, 1, 1, 12, 30, 0))
            .totalDistance(new Distance(7800L))
            .build();

        when(userFinder.findUserById(any())).thenReturn(
            Optional.ofNullable(UserFixtures.getUserWithId(1L)));
        Long amount = loveGrantService.grantLoveToUserWithDistance(record);

        assertEquals(7L, amount);
    }

    @Test
    void 달린거리_1km보다_작으면_지급하지_않는다() {
        RunningRecord record = RunningRecord.builder()
            .isRewarded(false)
            .startedAt(LocalDateTime.of(2020, 1, 1, 12, 0, 0))
            .endAt(LocalDateTime.of(2020, 1, 1, 12, 30, 0))
            .totalDistance(new Distance(900L))
            .build();

        when(userFinder.findUserById(any())).thenReturn(
            Optional.ofNullable(UserFixtures.getUserWithId(1L)));
        Long amount = loveGrantService.grantLoveToUserWithDistance(record);

        assertEquals(0L, amount);
    }

}