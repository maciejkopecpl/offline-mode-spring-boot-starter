package pl.maciejkopec.offlinemode.expression;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import pl.maciejkopec.offlinemode.annotation.OfflineMode;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ExpressionEvaluator extends CachedExpressionEvaluator {

  private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();
  private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);
  private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

  /**
   * Create the suitable {@link EvaluationContext} for the offline mode handling on the specified
   * method.
   */
  public EvaluationContext createEvaluationContext(
      final Object object, final Class<?> targetClass, final Method method, final Object[] args) {
    final var METHOD = "createEvaluationContext(Object, Class, Method, Object[])";
    log.debug("Entering {}", METHOD);

    final var targetMethod = getTargetMethod(targetClass, method);
    final var root = new ExpressionRootObject(object, args);
    final var evaluationContext =
        new MethodBasedEvaluationContext(root, targetMethod, args, this.paramNameDiscoverer);

    log.debug("Leaving {}", METHOD);
    return evaluationContext;
  }

  /** Generate a key based on SPeL expression passed in {@link OfflineMode#key()} */
  public Object key(
      final String keyExpression,
      final AnnotatedElementKey methodKey,
      final EvaluationContext evalContext) {
    final var METHOD = "key(String, AnnotatedElementKey, EvaluationContext)";
    log.debug("Entering {}", METHOD);

    final var key =
        getExpression(this.conditionCache, methodKey, keyExpression).getValue(evalContext);

    log.debug("Leaving {}", METHOD);
    return key;
  }

  private Method getTargetMethod(final Class<?> targetClass, final Method method) {
    final var METHOD = "getTargetMethod(Class, Method)";
    log.debug("Entering {}", METHOD);

    final var methodKey = new AnnotatedElementKey(method, targetClass);
    final var cachedTargetMethod = this.targetMethodCache.get(methodKey);
    if (cachedTargetMethod == null) {
      final var targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
      this.targetMethodCache.put(methodKey, targetMethod);
      log.debug(
          "Target method is not cached. Generated targetMethod: `{}` added to the cache.",
          targetMethod.getName());
      log.debug("Leaving {}", METHOD);

      return targetMethod;
    } else {
      log.debug("Target method `{}` is cached.", cachedTargetMethod.getName());
      log.debug("Leaving {}", METHOD);

      return cachedTargetMethod;
    }
  }
}
