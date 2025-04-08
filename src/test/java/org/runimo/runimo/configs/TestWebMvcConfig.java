package org.runimo.runimo.configs;

import java.util.List;
import org.runimo.runimo.user.controller.UserIdResolver;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@TestConfiguration
public class TestWebMvcConfig implements WebMvcConfigurer {

    private final UserIdResolver userIdResolver;

    public TestWebMvcConfig(UserIdResolver userIdResolver) {
        this.userIdResolver = userIdResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdResolver);
    }
}
