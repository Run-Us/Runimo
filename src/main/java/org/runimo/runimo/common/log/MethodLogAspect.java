package org.runimo.runimo.common.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.runimo.runimo.common.log.model.MethodEndLogInfo;
import org.runimo.runimo.common.log.model.MethodStartLogInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@RequiredArgsConstructor
@Component
public class MethodLogAspect {

    private final LogMessageFormatter logMessageFormatter;

    @Pointcut("@within(org.runimo.runimo.common.log.ServiceLog) || @annotation(org.runimo.runimo.common.log.ServiceLog)")
    private void annotatedClassAndMethod() {
    }

    @Around("annotatedClassAndMethod()")
    public Object calledMethodLogger(ProceedingJoinPoint pjp) throws Throwable {
        MethodStartLogInfo methodStartLogInfo = getMethodStartLogInfo(pjp);
        log.info(logMessageFormatter.toMethodStartLogMessage(methodStartLogInfo));

        long startTime = getCurrentTimeMillis();
        long endTime;
        Object proceedReturn = null;
        try {
            proceedReturn = pjp.proceed();
        } catch (Throwable ex) {
            log.error(logMessageFormatter.toMethodErrorLogMessage(ex));
        }
        endTime = getCurrentTimeMillis();

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
