package com.integration.poc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.integration.poc.dtos.internal.ApiRequestConfig;

@SpringBootApplication
@EnableJpaRepositories
public class PocApplication {

  public static void main(String[] args) {
    SpringApplication.run(PocApplication.class, args);
  }

}

