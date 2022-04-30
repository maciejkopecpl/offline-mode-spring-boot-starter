package pl.maciejkopec.offlinemode.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "offline-mode")
@ConstructorBinding
@Getter
public class OfflineModeConfiguration {

  private final boolean enabled;
  private final Mode mode;
  private final String path;

  public OfflineModeConfiguration(
      @DefaultValue("false") final boolean enabled,
      @DefaultValue("SERVING") final Mode mode,
      @DefaultValue("offline") final String path) {
    this.enabled = enabled;
    this.mode = mode;
    this.path = path;
  }

  public enum Mode {
    /** TODO add documentation */
    LEARNING,
    SERVING
  }
}
