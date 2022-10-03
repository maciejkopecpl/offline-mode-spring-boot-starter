package pl.maciejkopec.offlinemode.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import pl.maciejkopec.offlinemode.annotation.OfflineMode;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class AnnotationUsageValidation implements BeanPostProcessor {

  @Override
  public Object postProcessAfterInitialization(final Object bean, final String beanName)
      throws BeansException {
    final var METHOD = "postProcessAfterInitialization(Object, String)";
    log.debug("Entering {}", METHOD);
    final var type = bean.getClass();
    final var methods = type.getDeclaredMethods();

    Arrays.stream(methods)
        .map(
            method ->
                new ValidationContext(
                    method, AnnotationUtils.findAnnotation(method, OfflineMode.class)))
        .filter(context -> Objects.nonNull(context.offlineMode()))
        .forEach(this::validateOfflineModeUsage);

    log.debug("Leaving {}", METHOD);

    return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
  }

  private void validateOfflineModeUsage(final ValidationContext context) {
    final var METHOD = "validateOfflineModeUsage(ValidationContext)";
    log.debug("Entering {}", METHOD);

    final var returnType = context.method().getReturnType();
    final var offlineMode = context.offlineMode();

    if (Void.class.equals(offlineMode.keyClass()) && Map.class.isAssignableFrom(returnType)) {
      log.error(
          "OfflineMode is misconfigured. For Map-like return types define keyClass. Only simple types are supported. Details = {} ",
          context);
      throw new IllegalArgumentException("Define keyClass() in OfflineMode annotation");
    }

    if (Void.class.equals(offlineMode.elementClass())
        && (Collection.class.isAssignableFrom(returnType)
            || Map.class.isAssignableFrom(returnType))) {
      log.error(
          "OfflineMode is misconfigured. For Collection-like return types define elementClass. Details = {}",
          context);
      throw new IllegalArgumentException("Define elementClass() in OfflineMode annotation");
    }

    log.debug("Leaving {}", METHOD);
  }

  private record ValidationContext(Method method, OfflineMode offlineMode) {}
}
