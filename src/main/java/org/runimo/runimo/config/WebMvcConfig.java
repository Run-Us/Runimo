package org.runimo.runimo.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.controller.request.AuthSignupConverter;
import org.runimo.runimo.user.controller.UserIdResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserIdResolver userIdResolver;
    private final AuthSignupConverter authSignupConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(authSignupConverter);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdResolver);
    }
}
