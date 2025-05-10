package org.runimo.runimo.auth.controller.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthSignupConverter implements Converter<String, AuthSignupRequest> {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public AuthSignupRequest convert(@NotNull String source) {
        return objectMapper.readValue(source, AuthSignupRequest.class);
    }
}
