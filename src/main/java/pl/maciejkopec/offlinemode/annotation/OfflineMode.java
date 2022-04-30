package pl.maciejkopec.offlinemode.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** TODO rename to memoize? Memoizing */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OfflineMode {
  /** Spring Expression Language (SpEL) expression for computing the key dynamically. */
  String key() default "";
}
