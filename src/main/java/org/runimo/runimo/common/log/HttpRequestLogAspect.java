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
public class HttpRequestLogAspect {

    private final LogMessageFormatter logMessageFormatter;

    @Pointcut("execution(* org.runimo.runimo..controller.*Controller.*(..))")
    private void controller() {
    }

    @Before("controller()")
    public void apiRequestLogger() {
        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (attributes == null) {
            log.info("ServletRequestAttributes is null");
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        HttpRequestLogInfo logInfo = HttpRequestLogInfo.of(request);

        log.info(logMessageFormatter.toHttpRequestLogMessage(logInfo));
    }
    
}
