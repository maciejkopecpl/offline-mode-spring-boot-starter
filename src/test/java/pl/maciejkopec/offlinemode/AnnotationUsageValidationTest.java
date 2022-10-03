package pl.maciejkopec.offlinemode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.maciejkopec.offlinemode.annotation.OfflineMode;
import pl.maciejkopec.offlinemode.config.AnnotationUsageValidation;
import pl.maciejkopec.offlinemode.test.TestFullDto;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AnnotationUsageValidationTest {

  @InjectMocks private AnnotationUsageValidation annotationUsageValidation;
  @Mock private Object object;

  @Test
  void shouldThrowExceptionWhenMissingElementClass() {
    final var object = new TestClassMisconfiguredCollection();
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> annotationUsageValidation.postProcessAfterInitialization(object, null));

    assertThat(exception).hasMessageContaining("Define elementClass() in OfflineMode annotation");
  }

  @Test
  void shouldThrowExceptionWhenMissingKeyClass() {
    final var object = new TestClassMisconfiguredMap();
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> annotationUsageValidation.postProcessAfterInitialization(object, null));

    assertThat(exception).hasMessageContaining("Define keyClass() in OfflineMode annotation");
  }

  private static class TestClassMisconfiguredCollection {

    @OfflineMode
    private List<TestFullDto> test() {
      return List.of();
    }
  }

  private static class TestClassMisconfiguredMap {

    @OfflineMode
    private Map<String, TestFullDto> test() {
      return Map.of();
    }
  }
}
