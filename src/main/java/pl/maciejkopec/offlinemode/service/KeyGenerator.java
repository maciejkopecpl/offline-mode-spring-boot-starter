package pl.maciejkopec.offlinemode.service;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import pl.maciejkopec.offlinemode.annotation.OfflineMode;
import pl.maciejkopec.offlinemode.expression.ExpressionEvaluator;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Generator used to create a key based on the method definition. <br>
 * It supports two modes: argument based and SPeL based. <br>
 * Argument based mode will generate key based on: type name, name of the method and method
 * parameters. <br>
 * SPeL mode use the SPeL expression defined in {@link OfflineMode#key()} to generate the key.
 */
@Slf4j
public class KeyGenerator {
  private final ExpressionEvaluator evaluator = new ExpressionEvaluator();

  public String generate(final ProceedingJoinPoint joinPoint, final OfflineMode offlineMode) {
    final var METHOD = "generate(ProceedingJoinPoint, OfflineMode)";
    log.debug("Entering {}", METHOD);

    final var key =
        Objects.equals(offlineMode.key(), "")
            ? argumentBasedKey(joinPoint)
            : spelBasedKey(joinPoint, offlineMode);

    log.debug("Leaving {} key={}", METHOD, key);
    return key;
  }

  private String spelBasedKey(final ProceedingJoinPoint joinPoint, final OfflineMode offlineMode) {
    final var METHOD = "spelBasedKey(ProceedingJoinPoint, OfflineMode)";
    log.debug("Entering {}", METHOD);

    final var signature = (MethodSignature) joinPoint.getSignature();
    final var method = signature.getMethod();
    final var targetClass = joinPoint.getTarget().getClass();
    final var evaluationContext =
        evaluator.createEvaluationContext(
            joinPoint.getTarget(), targetClass, method, joinPoint.getArgs());
    final var methodKey = new AnnotatedElementKey(method, targetClass);

    final var key = evaluator.key(offlineMode.key(), methodKey, evaluationContext).toString();

    log.debug("Generated key: `{}` for OfflineMode: `{}`", key, offlineMode);

    log.debug("Leaving {}", METHOD);
    return key;
  }

  private String argumentBasedKey(final ProceedingJoinPoint joinPoint) {
    final var METHOD = "argumentBasedKey(ProceedingJoinPoint)";
    log.debug("Entering {}", METHOD);

    final var arguments =
        Arrays.stream(joinPoint.getArgs())
            .map(this::generateArgument)
            .collect(Collectors.joining("~"));

    final var key =
        String.format(
            "%s_%s_%s",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            arguments);

    log.debug("Generated key: `{}`", key);

    log.debug("Leaving {}", METHOD);
    return key;
  }

  private String generateArgument(final Object argument) {
    if (BeanUtils.isSimpleProperty(argument.getClass())) {
      return argument.toString();
    }

    return String.valueOf(Objects.hashCode(argument));
  }
}
