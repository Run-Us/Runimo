package org.runimo.runimo.common.log.model;

import lombok.AccessLevel;
import lombok.Builder;
import org.aspectj.lang.JoinPoint;

@Builder(access = AccessLevel.PRIVATE)
public record MethodEndLogInfo(
    String methodName,
    long elapsedTimeMillis,
    String returnData
) {

    public static MethodEndLogInfo of(JoinPoint joinPoint, long elapsedTimeMillis,
        Object returnData) {
        return MethodEndLogInfo.builder()
            .methodName(joinPoint.getSignature().getName())
            .elapsedTimeMillis(elapsedTimeMillis)
            .returnData(returnData.toString())
            .build();
    }
}
