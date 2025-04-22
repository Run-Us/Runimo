package org.runimo.runimo.records.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.runimo.runimo.common.scale.Distance;

class RunningRecordTest {

    @Test
    void 제목을_입력하지_않으면_기본_제목_설정() {
        RunningRecord runningRecordWithoutTitle = RunningRecord.builder()
            .userId(1L)
            .startedAt(LocalDateTime.now())
            .endAt(LocalDateTime.now().plusHours(1))
            .isRewarded(false)
            .totalDistance(new Distance(1000L))
            .pacePerKm(null)
            .build();

        assertNotNull(runningRecordWithoutTitle.getTitle());
    }


    @ParameterizedTest(name = "{0}시에 달리면 {1} 제목이 설정됨")
    @CsvSource({
        "2020-01-01T07:00:00, 개운한 아침런",
        "2020-01-01T13:00:00, 활기찬 오후런",
        "2020-01-01T19:00:00, 두근두근 저녁런",
        "2020-01-01T01:00:00, 고요한 심야런"
    })
    void 기본_제목_설정시_시간에_따라_제목이_달라진다(String dateTimeStr, String expectedTitle) {
        // given
        LocalDateTime runAt = LocalDateTime.parse(dateTimeStr);

        RunningRecord runningRecordWithoutTitle = RunningRecord.builder()
            .userId(1L)
            .startedAt(runAt)
            .endAt(runAt.plusHours(1))
            .isRewarded(false)
            .totalDistance(new Distance(1000L))
            .pacePerKm(null)
            .build();

        // then
        assertEquals(expectedTitle, runningRecordWithoutTitle.getTitle());
    }

}