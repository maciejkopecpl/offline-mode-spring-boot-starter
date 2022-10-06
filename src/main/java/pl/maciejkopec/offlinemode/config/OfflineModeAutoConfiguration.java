package pl.maciejkopec.offlinemode.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
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
  public OfflineModeAspect offlineModeAspect(
      final ObjectMapper objectMapper, final ResourceLoader resourceLoader) {
    final var METHOD = "offlineModeAspect()";
    log.debug("Entering {}", METHOD);

    final var keyGenerator = new KeyGenerator();
    final var fileHandler = new FileHandler(configuration, resourceLoader);
    final var responseCaptor =
        new ResponseCaptor(keyGenerator, fileHandler, configuration, objectMapper);
    final var offlineModeAspect = new OfflineModeAspect(responseCaptor, configuration);

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
