package org.runimo.runimo.records.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public enum DefaultTitle {
    MORNING("개운한 아침런"),
    AFTERNOON("활기찬 오후런"),
    NIGHT("두근두근 저녁런"),
    MIDNIGHT("고요한 심야런");

    private final String title;

    DefaultTitle(String title) {
        this.title = title;
    }

    public static DefaultTitle fromTime(LocalDateTime time) {
        int hour = time.getHour();
        if (hour < 6) {
            return MIDNIGHT;
        } else if (hour < 12) {
            return MORNING;
        } else if (hour < 18) {
            return AFTERNOON;
        } else {
            return NIGHT;
        }
    }
}
