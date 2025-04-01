package org.runimo.runimo.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {

    Server server = new Server();
    server.setUrl("https://toy.hyeonjae.dev");

    return new OpenAPI()
        .info(new Info()
            .title("Runimo User API")
            .version("1.0")
            .description("Runimo 프로젝트의 사용자 인증 API 문서입니다.")
            .contact(new Contact()
                .name("Runimo Support")
                .email("support@runimo.org"))
            .license(new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0"))
        ).addSecurityItem(new SecurityRequirement().addList("JWT"))
        .servers(Collections.singletonList(server))
        .components(new io.swagger.v3.oas.models.Components()
            .addSecuritySchemes("JWT",
                new SecurityScheme()
                    .name("JWT")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer customizer() {
    return builder -> builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
  }
}

