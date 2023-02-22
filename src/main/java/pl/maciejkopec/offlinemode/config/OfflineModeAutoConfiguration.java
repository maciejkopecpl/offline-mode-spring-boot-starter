package pl.maciejkopec.offlinemode.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import pl.maciejkopec.offlinemode.aspect.OfflineModeAspect;
import pl.maciejkopec.offlinemode.service.FileHandler;
import pl.maciejkopec.offlinemode.service.KeyGenerator;
import pl.maciejkopec.offlinemode.service.ResponseCaptor;

@Slf4j
@RequiredArgsConstructor
@AutoConfiguration
@EnableConfigurationProperties(OfflineModeConfiguration.class)
@ConditionalOnProperty(prefix = "offline-mode", name = "enabled", havingValue = "true")
public class OfflineModeAutoConfiguration {

  private final OfflineModeConfiguration configuration;

  @Bean
  public OfflineModeAspect offlineModeAspect(
      final ObjectMapper objectMapper, final ResourceLoader resourceLoader) {
    final var METHOD = "offlineModeAspect()";
    log.debug("Entering {}", METHOD);

    final var keyGenerator = new KeyGenerator();
    final var fileHandler = new FileHandler(configuration, resourceLoader);
    final var responseCaptor =
        new ResponseCaptor(keyGenerator, fileHandler, configuration, objectMapper);
    final var offlineModeAspect = new OfflineModeAspect(responseCaptor);

    log.debug("Leaving {}", METHOD);
    return offlineModeAspect;
  }

  @Bean
  public BeanPostProcessor annotationUsageValidationBean() {
    final var METHOD = "annotationUsageValidationBean()";
    log.debug("Entering {}", METHOD);
    final var annotationUsageValidation = new AnnotationUsageValidation();

    log.debug("Leaving {}", METHOD);
    return annotationUsageValidation;
  }
}
