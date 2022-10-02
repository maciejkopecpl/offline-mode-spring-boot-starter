package pl.maciejkopec.offlinemode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import pl.maciejkopec.offlinemode.test.TestApplication;
import pl.maciejkopec.offlinemode.test.TestService;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.maciejkopec.offlinemode.LearningSkipExistingModeTests.TEST_FILES_PATH;

@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
@TestPropertySource(
    value = "classpath:application.yaml",
    properties = {
      "offline-mode.mode=learning_skip_existing",
      "offline-mode.path=" + TEST_FILES_PATH
    })
class LearningSkipExistingModeTests {

  @Autowired private TestService testService;

  static final String TEST_FILES_PATH = "src/test/resources/data/learning_skip_existing";
  private static final String EXPECTED_DYNAMIC_VALUE = "dynamic_data";

  @Test
  void smokeTest() {
    assertThat(testService).isNotNull();
  }

  @Nested
  @DisplayName("General test cases")
  class GeneralTests {

    @Test
    void shouldNotOverwriteExistingFile() {
      final var file =
          new File(
              TEST_FILES_PATH + "/pl.maciejkopec.offlinemode.test.TestService_simpleCall_.json");
      assertThat(file).exists();
      final var lastModified = file.lastModified();
      final var result = testService.simpleCall();

      assertThat(result).isEqualTo(EXPECTED_DYNAMIC_VALUE);
      assertThat(file).exists();
      assertThat(file.lastModified()).isEqualTo(lastModified);
    }

    @Test
    void shouldGenerateCollectionSetJson() {
      final var file = new File(TEST_FILES_PATH + "/collection_set.json");
      assertThat(file).doesNotExist();
      final var result = testService.dtoCallWithSetResponse();

      assertThat(result).hasSize(1);
      assertThat(file).exists();
      assertThat(file.delete()).isTrue();
    }
  }
}
