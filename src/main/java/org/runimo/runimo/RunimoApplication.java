package org.runimo.runimo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RunimoApplication {

  public static void main(String[] args) {
    SpringApplication.run(RunimoApplication.class, args);
  }

}
