package pl.maciejkopec.offlinemode.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import pl.maciejkopec.offlinemode.config.OfflineModeConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Slf4j
public class FileHandler {

  private final OfflineModeConfiguration configuration;

  public File read(@NonNull final String key) {
    final String METHOD = "read(String)";
    log.debug("Entering {}", METHOD);

    final String filename = key + ".json";
    final Path path = Paths.get(configuration.getPath()).toAbsolutePath().normalize();

    final File file = path.resolve(filename).toFile();

    log.debug("Leaving {}", METHOD);
    return file;
  }

  public void write(@NonNull final String key, @NonNull final String serializedObject) {
    final String METHOD = "write(String, String)";
    log.debug("Entering {}", METHOD);

    final String filename = key + ".json";
    final Path path = Paths.get(configuration.getPath()).toAbsolutePath().normalize();

    try {
      final Path dir = Files.createDirectories(path);
      Files.write(dir.resolve(filename), serializedObject.getBytes());
    } catch (final IOException e) {
      log.error("Failed to write a file in {} for key {}", configuration.getPath(), key, e);
    } finally {
      log.debug("Leaving {}", METHOD);
    }
  }
}
