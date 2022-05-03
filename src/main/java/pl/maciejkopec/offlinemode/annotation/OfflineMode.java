package pl.maciejkopec.offlinemode.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OfflineMode {
  /** Spring Expression Language (SpEL) expression for computing the key dynamically. */
  String key() default "";
}
