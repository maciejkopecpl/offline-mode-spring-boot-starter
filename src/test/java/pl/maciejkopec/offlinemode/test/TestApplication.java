package pl.maciejkopec.offlinemode.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "pl.maciejkopec.offlinemode")
public class TestApplication {

  public static void main(final String[] args) {
    SpringApplication.run(TestApplication.class, args);
  }

  @Bean
  public ObjectMapper objectMapper() {
    final var objectMapper = new ObjectMapper();

    objectMapper.addMixIn(NoConstructorDto.class, NoConstructorDtoMixin.class);
    return objectMapper;
  }
}
