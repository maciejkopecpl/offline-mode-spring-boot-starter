package pl.maciejkopec.offlinemode.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import pl.maciejkopec.offlinemode.annotation.OfflineMode;
import pl.maciejkopec.offlinemode.config.OfflineModeConfiguration;

import java.util.Collection;
import java.util.Map;

import static pl.maciejkopec.offlinemode.config.OfflineModeConfiguration.Mode;
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
    final var signature = joinPoint.getSignature();
    final var returnType = ((MethodSignature) signature).getReturnType();

    if (Mode.isLearningEnabled(configuration.getMode())) {

      if (Mode.LEARNING_SKIP_EXISTING.equals(configuration.getMode())
          && fileHandler.fileExists(key)) {
        final var json = fileHandler.read(key);

        final var clazz = offlineMode.elementClass();

        final var valueType = resolveReturnType(offlineMode, returnType, clazz);
        final var value = objectMapper.readValue(json, valueType);

        log.debug("Read existing file for key = {}", key);
        log.debug("Leaving {}", METHOD);

        return value;
      } else {

        final var object = joinPoint.proceed();

        final var serializedObject = objectMapper.writeValueAsString(object);

        fileHandler.write(key, serializedObject);

        log.debug("Generated file for key = {}", key);
        log.debug("Leaving {}", METHOD);
        return object;
      }
    } else {

      final var json = fileHandler.read(key);

      if (json.exists()) {
        final var clazz = offlineMode.elementClass();

        final var valueType = resolveReturnType(offlineMode, returnType, clazz);
        final var value = objectMapper.readValue(json, valueType);

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

  private JavaType resolveReturnType(
      final OfflineMode offlineMode, final Class<?> returnType, final Class<?> clazz) {
    final var METHOD = "resolveReturnType(OfflineMode, Class, Class<?>)";
    log.debug("Entering {}", METHOD);

    JavaType type;
    if (Map.class.isAssignableFrom(returnType)) {
      type =
          objectMapper
              .getTypeFactory()
              .constructMapType((Class<? extends Map>) returnType, offlineMode.keyClass(), clazz);
    } else if (Collection.class.isAssignableFrom(returnType)) {
      type =
          objectMapper
              .getTypeFactory()
              .constructCollectionType((Class<? extends Collection>) returnType, clazz);
    } else if (returnType.isArray()) {
      type = objectMapper.getTypeFactory().constructArrayType(clazz);
    } else {
      type = objectMapper.getTypeFactory().constructType(returnType);
    }

    log.debug("Leaving {}, type={}", METHOD, type);

    return type;
  }
}
