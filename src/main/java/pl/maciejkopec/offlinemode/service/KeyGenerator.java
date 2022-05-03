package pl.maciejkopec.offlinemode.service;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import pl.maciejkopec.offlinemode.annotation.OfflineMode;
import pl.maciejkopec.offlinemode.expression.ExpressionEvaluator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
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

  private static final Function<Object, String> MAP_ARGUMENTS =
      argument -> {
        if (BeanUtils.isSimpleProperty(argument.getClass())) {
          return argument.toString();
        }

        return String.valueOf(Objects.hashCode(argument));
      };

  public String generate(final ProceedingJoinPoint joinPoint, final OfflineMode offlineMode) {
    final String METHOD = "generate(ProceedingJoinPoint, OfflineMode)";
    log.debug("Entering {}", METHOD);

    final String key =
        Objects.equals(offlineMode.key(), "")
            ? argumentBasedKey(joinPoint)
            : spelBasedKey(joinPoint, offlineMode);

    log.debug("Leaving {}", METHOD);
    return key;
  }

  private String spelBasedKey(final ProceedingJoinPoint joinPoint, final OfflineMode offlineMode) {
    final String METHOD = "spelBasedKey(ProceedingJoinPoint, OfflineMode)";
    log.debug("Entering {}", METHOD);

    final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    final Method method = signature.getMethod();
    final Class<?> targetClass = joinPoint.getTarget().getClass();
    final EvaluationContext evaluationContext =
        evaluator.createEvaluationContext(
            joinPoint.getTarget(), targetClass, method, joinPoint.getArgs());
    final AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);

    final String key = evaluator.key(offlineMode.key(), methodKey, evaluationContext).toString();

    log.debug("Generated key: `{}` for OfflineMode: `{}`", key, offlineMode);

    log.debug("Leaving {}", METHOD);
    return key;
  }

  private String argumentBasedKey(final ProceedingJoinPoint joinPoint) {
    final String METHOD = "argumentBasedKey(ProceedingJoinPoint)";
    log.debug("Entering {}", METHOD);

    final String arguments =
        Arrays.stream(joinPoint.getArgs()).map(MAP_ARGUMENTS).collect(Collectors.joining("~"));

    final String key =
        String.format(
            "%s_%s_%s",
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            arguments);

    log.debug("Generated key: `{}`", key);

    log.debug("Leaving {}", METHOD);
    return key;
  }
}
