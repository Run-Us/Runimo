package org.runimo.runimo.common.log.model;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

@Builder(access = AccessLevel.PRIVATE)
public record MethodStartLogInfo(
    String className,
    String methodName,
    boolean authenticated,
    String userId,
    Map<String, String> params
) {

    public static MethodStartLogInfo of(JoinPoint joinPoint, boolean authenticated, String userId) {
        String className = getClassName(joinPoint);

        Map<String, String> params = getParamMap(joinPoint);

        return MethodStartLogInfo.builder()
            .className(className)
            .methodName(joinPoint.getSignature().getName())
            .authenticated(authenticated)
            .userId(userId)
            .params(params)
            .build();
    }

    private static Map<String, String> getParamMap(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();

        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], parameterValues[i].toString());
        }
        return params;
    }

    private static String getClassName(JoinPoint joinPoint) {
        String classPath = joinPoint.getSignature().getDeclaringTypeName();
        return classPath.substring(classPath.lastIndexOf(".") + 1);
    }
}
