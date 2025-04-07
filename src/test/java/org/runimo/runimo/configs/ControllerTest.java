package org.runimo.runimo.configs;

import org.runimo.runimo.exceptions.GlobalExceptionHandler;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
@Import({TestConfig.class, TestWebMvcConfig.class, TestSecurityConfig.class, GlobalExceptionHandler.class})
public @interface ControllerTest {
  Class<?>[] controllers() default {};
}