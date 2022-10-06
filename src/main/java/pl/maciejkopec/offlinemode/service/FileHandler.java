package pl.maciejkopec.offlinemode.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;
import org.springframework.util.StreamUtils;
import pl.maciejkopec.offlinemode.config.OfflineModeConfiguration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class FileHandler {

  private static final String EXTENSION = ".json";
  private final OfflineModeConfiguration configuration;
  private final ResourceLoader resourceLoader;

  public Optional<String> read(@NonNull final String key) throws IOException {
    final var METHOD = "read(String)";
    log.debug("Entering {}", METHOD);

    final var location = configuration.getPath() + "/" + key + EXTENSION;
    final var resource = resourceLoader.getResource(location);

    if (!resource.exists()) {
      log.debug("Leaving {} location={}", METHOD, location);
      return Optional.empty();
    }

    final var path = resource.getInputStream();
    final var file = Optional.of(StreamUtils.copyToString(path, Charset.defaultCharset()));
    log.debug("Leaving {} location={} ", METHOD, location);
    return file;
  }

  public void write(@NonNull final String key, @NonNull final String serializedObject) {
    final var METHOD = "write(String, String)";
    log.debug("Entering {}", METHOD);

    try {
      final var path = resourceLoader.getResource(configuration.getPath()).getFile().toPath();
      final var dir = Files.createDirectories(path);
      final var file = dir.resolve(key + EXTENSION);
      Files.writeString(file, serializedObject);
      log.debug("Writing file completed. path={} dir={} file={}", path, dir, file);
    } catch (final IOException e) {
      log.error("Failed to write a file in {} for key {}", configuration.getPath(), key, e);
    } finally {
      log.debug("Leaving {}", METHOD);
    }
  }
}
