package pl.maciejkopec.offlinemode.test;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestServiceConfig {

  @Bean
  public TestService testService() {
    return new TestService();
  }
}
