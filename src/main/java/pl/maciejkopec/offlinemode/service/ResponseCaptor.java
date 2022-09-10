package pl.maciejkopec.offlinemode.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import pl.maciejkopec.offlinemode.annotation.OfflineMode;
import pl.maciejkopec.offlinemode.config.OfflineModeConfiguration;

import static pl.maciejkopec.offlinemode.config.OfflineModeConfiguration.Mode.LEARNING;

@RequiredArgsConstructor
@Slf4j
public class ResponseCaptor {

  private final KeyGenerator keyGenerator;
  private final FileHandler fileHandler;
  private final OfflineModeConfiguration configuration;

  final ObjectMapper objectMapper = new ObjectMapper();

  public Object capture(final ProceedingJoinPoint joinPoint, final OfflineMode offlineMode)
      throws Throwable {
    final var METHOD = "capture(ProceedingJoinPoint, OfflineMode)";
    log.debug("Entering {}", METHOD);

    final var key = keyGenerator.generate(joinPoint, offlineMode);

    if (LEARNING.equals(configuration.getMode())) {

      final var object = joinPoint.proceed();

      final var serializedObject = objectMapper.writeValueAsString(object);

      fileHandler.write(key, serializedObject);

      log.debug("Leaving {}", METHOD);
      return object;
    } else {

      final var read = fileHandler.read(key);

      if (read.exists()) {
        final var signature = joinPoint.getSignature();
        final var returnType = ((MethodSignature) signature).getReturnType();
        final var value = objectMapper.readValue(read, returnType);

        log.debug("Leaving {}", METHOD);
        return value;
      } else {
        log.warn(
            "No captured data found for '{}' in {}, calling actual method. Switch to '{}' mode to generate offline file.",
            key,
            configuration.getPath(),
            LEARNING);
        final var proceed = joinPoint.proceed();

        log.debug("Leaving {}", METHOD);
        return proceed;
      }
    }
  }
}
