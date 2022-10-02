package pl.maciejkopec.offlinemode.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OfflineMode {
  /** Spring Expression Language (SpEL) expression for computing the key dynamically. */
  String key() default "";

  /**
   * Class type of the element of the Collection (like List or Set), or the type of the map value.
   */
  Class<?> elementClass() default Void.class;

  /** Class type of the map's element. Only simple types are supported (String, Integer etc). */
  Class<?> keyClass() default Void.class;

  /** Flag to override */
  boolean enabled() default true;
}
