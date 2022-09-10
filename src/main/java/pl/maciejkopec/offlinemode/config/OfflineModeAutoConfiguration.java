package pl.maciejkopec.offlinemode.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.maciejkopec.offlinemode.aspect.OfflineModeAspect;
import pl.maciejkopec.offlinemode.service.FileHandler;
import pl.maciejkopec.offlinemode.service.KeyGenerator;
import pl.maciejkopec.offlinemode.service.ResponseCaptor;

@Configuration
@EnableConfigurationProperties(OfflineModeConfiguration.class)
@Slf4j
@RequiredArgsConstructor
public class OfflineModeAutoConfiguration {

  private final OfflineModeConfiguration configuration;

  @Bean
  public OfflineModeAspect offlineModeAspect() {
    final var METHOD = "offlineModeAspect()";
    log.debug("Entering {}", METHOD);

    final var keyGenerator = new KeyGenerator();
    final var fileHandler = new FileHandler(configuration);
    final var responseCaptor = new ResponseCaptor(keyGenerator, fileHandler, configuration);
    final var offlineModeAspect = new OfflineModeAspect(responseCaptor, configuration);

    log.debug("Leaving {}", METHOD);
    return offlineModeAspect;
  }
}
