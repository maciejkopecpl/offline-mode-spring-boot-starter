package pl.maciejkopec.offlinemode.service;

import static pl.maciejkopec.offlinemode.config.OfflineModeConfiguration.Mode.LEARNING;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import pl.maciejkopec.offlinemode.annotation.OfflineMode;
import pl.maciejkopec.offlinemode.config.OfflineModeConfiguration;

@RequiredArgsConstructor
@Slf4j
public class ResponseCaptor {

  private final KeyGenerator keyGenerator;
  private final FileHandler fileHandler;
  private final OfflineModeConfiguration configuration;

  final ObjectMapper objectMapper = new ObjectMapper();

  public Object capture(final ProceedingJoinPoint joinPoint, final OfflineMode offlineMode)
      throws Throwable {
    final String METHOD = "capture(ProceedingJoinPoint, OfflineMode)";
    log.debug("Entering {}", METHOD);

    final String key = keyGenerator.generate(joinPoint, offlineMode);

    if (LEARNING.equals(configuration.getMode())) {

      final Object object = joinPoint.proceed();

      final String serializedObject = objectMapper.writeValueAsString(object);

      fileHandler.write(key, serializedObject);

      log.debug("Leaving {}", METHOD);
      return object;
    } else {

      final File read = fileHandler.read(key);

      if (read.exists()) {
        final Signature signature = joinPoint.getSignature();
        final Class<?> returnType = ((MethodSignature) signature).getReturnType();
        final Object value = objectMapper.readValue(read, returnType);

        log.debug("Leaving {}", METHOD);
        return value;
      } else {
        log.warn(
            "No captured data found for '{}' in {}, calling actual method. Switch to '{}' mode to generate offline file.",
            key,
            configuration.getPath(),
            LEARNING);
        final Object proceed = joinPoint.proceed();

        log.debug("Leaving {}", METHOD);
        return proceed;
      }
    }
  }
}
