package org.runimo.runimo.common.log.model;

import lombok.AccessLevel;
import lombok.Builder;
import org.aspectj.lang.JoinPoint;

@Builder(access = AccessLevel.PRIVATE)
public record MethodEndLogInfo(
    String className,
    String methodName,
    long elapsedTimeMillis,
    String returnData
) {

    public static MethodEndLogInfo of(JoinPoint joinPoint, long elapsedTimeMillis,
        Object returnData) {
        return MethodEndLogInfo.builder()
            .className(getClassName(joinPoint))
            .methodName(joinPoint.getSignature().getName())
            .elapsedTimeMillis(elapsedTimeMillis)
            .returnData(String.valueOf(returnData))
            .build();
    }

    private static String getClassName(JoinPoint joinPoint) {
        String classPath = joinPoint.getSignature().getDeclaringTypeName();
        return classPath.substring(classPath.lastIndexOf(".") + 1);
    }
}
