package pl.maciejkopec.offlinemode.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@SpringBootApplication(scanBasePackages = "pl.maciejkopec.offlinemode")
public class TestApplication {

  public static void main(final String[] args) {
    SpringApplication.run(TestApplication.class, args);
  }

  @Bean
  public ObjectMapper objectMapper() {
    return JsonMapper.builder()
        .addMixIn(NoConstructorDto.class, NoConstructorDtoMixin.class)
        .build();
  }
}
