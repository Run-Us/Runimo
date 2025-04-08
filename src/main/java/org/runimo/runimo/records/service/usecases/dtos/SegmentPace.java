package org.runimo.runimo.records.service.usecases.dtos;

import java.io.Serializable;

public record SegmentPace(
    double distance,
    long pace
) implements Serializable {

}
