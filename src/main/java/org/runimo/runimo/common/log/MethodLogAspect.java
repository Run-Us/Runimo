package org.runimo.runimo.common.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.runimo.runimo.common.log.model.HttpRequestLogInfo;
import org.runimo.runimo.common.log.model.MethodEndLogInfo;
import org.runimo.runimo.common.log.model.MethodStartLogInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Slf4j
@RequiredArgsConstructor
@Component
public class MethodLogAspect {

    private final LogMessageFormatter logMessageFormatter;

    @Pointcut("execution(* (@org.runimo.runimo.common.log.ServiceLog *).*(..))")
    private void service() {
    }

    @Around("service()")
    public Object calledMethodLogger(ProceedingJoinPoint pjp) throws Throwable {
        MethodStartLogInfo methodStartLogInfo = getMethodStartLogInfo(pjp);
        log.info(logMessageFormatter.toMethodStartLogMessage(methodStartLogInfo));

        long startTime = getCurrentTimeMillis();
        Object proceedReturn = pjp.proceed();
        long endTime = getCurrentTimeMillis();

        MethodEndLogInfo methodEndLogInfo = MethodEndLogInfo.of(pjp, endTime - startTime,
            proceedReturn);
        log.info(logMessageFormatter.toMethodEndLogMessage(methodEndLogInfo));

        return proceedReturn;
    }

    private static MethodStartLogInfo getMethodStartLogInfo(ProceedingJoinPoint pjp) {
        MethodStartLogInfo methodStartLogInfo;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            methodStartLogInfo = MethodStartLogInfo.of(pjp, true, authentication.getName());
        } else {
            methodStartLogInfo = MethodStartLogInfo.of(pjp, false, null);
        }

        return methodStartLogInfo;
    }

    private long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

}
