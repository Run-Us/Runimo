package org.runimo.runimo.records;

import java.time.LocalDateTime;
import java.util.List;
import org.runimo.runimo.records.controller.requests.RecordSaveRequest;
import org.runimo.runimo.records.service.usecases.dtos.SegmentPace;

public class RecordFixtures {

    public static RecordSaveRequest createRecordSaveRequest() {
        return new RecordSaveRequest(
            LocalDateTime.of(2025, 3, 30, 9, 30, 0),
            LocalDateTime.of(2025, 3, 30, 10, 0, 0),
            5000L,
            360000L,
            List.of(new SegmentPace(1.0, 732000))
        );
    }
}
