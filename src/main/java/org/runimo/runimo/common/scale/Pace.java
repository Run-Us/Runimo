package org.runimo.runimo.common.scale;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pace implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Column(name = "pace_in_milli_seconds")
    private Long paceInMilliSeconds;

    public Pace(Long paceInMilliSeconds) {
        this.paceInMilliSeconds = paceInMilliSeconds;
    }

    public Long getPaceInMilliSeconds() {
        return paceInMilliSeconds;
    }
}
