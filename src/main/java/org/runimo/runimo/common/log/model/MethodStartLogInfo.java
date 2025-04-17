package org.runimo.runimo.common.log.model;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

@Builder(access = AccessLevel.PRIVATE)
public record MethodStartLogInfo(
    String methodName,
    boolean authenticated,
    String userId,
    Map<String, String> params
) {

    public static MethodStartLogInfo of(JoinPoint joinPoint, boolean authenticated, String userId) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();

        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], parameterValues[i].toString());
        }

        return MethodStartLogInfo.builder()
            .methodName(joinPoint.getSignature().getName())
            .authenticated(authenticated)
            .userId(userId)
            .params(params)
            .build();
    }
}
