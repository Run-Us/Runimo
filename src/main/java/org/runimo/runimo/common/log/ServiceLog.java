package org.runimo.runimo.common.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 비즈니스 로그 기록 어노테이션 - 메서드 실행 시 메서드명, 인자 목록, 반환값, 소요시간 등의 정보를 로깅
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceLog {

}
