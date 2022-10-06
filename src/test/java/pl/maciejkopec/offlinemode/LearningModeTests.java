package pl.maciejkopec.offlinemode;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import pl.maciejkopec.offlinemode.test.TestApplication;
import pl.maciejkopec.offlinemode.test.TestFullDtoWithoutEquals;
import pl.maciejkopec.offlinemode.test.TestRecord;
import pl.maciejkopec.offlinemode.test.TestService;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.maciejkopec.offlinemode.LearningModeTests.TEST_FILES_PATH;

@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
@TestPropertySource(
    value = "classpath:application.yaml",
    properties = {"offline-mode.mode=learning", "offline-mode.path=classpath:" + TEST_FILES_PATH})
class LearningModeTests {

  @Autowired private TestService testService;

  static final String TEST_FILES_PATH = "data/learning";
  private static final String TEST_FILE_ABSOLUTE_PREFIX = "build/resources/test/";

  private static final String EXPECTED_DYNAMIC_VALUE = "dynamic_data";

  @AfterEach
  void tearDown() {
    Arrays.stream(
            Objects.requireNonNull(
                new File(TEST_FILE_ABSOLUTE_PREFIX + TEST_FILES_PATH).listFiles()))
        .filter(not(p -> p.getName().equals(".gitkeep")))
        .forEach(File::delete);
  }

  @Test
  void smokeTest() {
    assertThat(testService).isNotNull();
  }

  @Nested
  @DisplayName("General test cases")
  class GeneralTests {
    @Test
    void shouldReturnSavedSimpleResponse() {
      final var file =
          new File(
              TEST_FILE_ABSOLUTE_PREFIX
                  + TEST_FILES_PATH
                  + "/pl.maciejkopec.offlinemode.test.TestService_simpleCall_.json");
      assertThat(file).doesNotExist();
      final var lastModified = file.lastModified();

      final var result = testService.simpleCall();

      assertThat(result).isEqualTo(EXPECTED_DYNAMIC_VALUE);
      assertThat(file).exists();
      assertThat(file.lastModified()).isGreaterThan(lastModified);
    }

    @Test
    void shouldReturnSavedFullDtoResponse() {
      final var file =
          new File(
              TEST_FILE_ABSOLUTE_PREFIX
                  + TEST_FILES_PATH
                  + "/pl.maciejkopec.offlinemode.test.TestService_dtoCall_.json");
      assertThat(file).doesNotExist();
      final var result = testService.dtoCall();

      assertThat(result.getValue()).isEqualTo(EXPECTED_DYNAMIC_VALUE);
      assertThat(file).exists();
    }

    @Test
    void shouldReturnSavedFullDtoResponseWithParam() {
      final var file =
          new File(
              TEST_FILE_ABSOLUTE_PREFIX
                  + TEST_FILES_PATH
                  + "/pl.maciejkopec.offlinemode.test.TestService_dtoCall_dynamic_data.json");
      assertThat(file).doesNotExist();

      final var result = testService.dtoCall("dynamic_data");

      assertThat(result.getValue()).isEqualTo(EXPECTED_DYNAMIC_VALUE);
      assertThat(file).exists();
    }

    @Test
    void shouldReturnSavedTestFullDtoWithoutEquals() {
      final var file = new File(TEST_FILE_ABSOLUTE_PREFIX + TEST_FILES_PATH + "/test.json");
      assertThat(file).doesNotExist();

      final var complexObject = new TestFullDtoWithoutEquals("value");
      final var result = testService.dtoCallWithCustomStaticKey("dynamic_data", complexObject);

      assertThat(result.getValue()).isEqualTo(EXPECTED_DYNAMIC_VALUE);
      assertThat(file).exists();
    }

    @Test
    void shouldReturnSavedTestFullDto() {
      final var file =
          new File(TEST_FILE_ABSOLUTE_PREFIX + TEST_FILES_PATH + "/prefix_test_parameter.json");
      assertThat(file).doesNotExist();
      final var complexObject = new TestFullDtoWithoutEquals("test_parameter");
      final var result = testService.dtoCallWithCustomComplexKey(complexObject);

      assertThat(result.getValue()).isEqualTo(EXPECTED_DYNAMIC_VALUE);
      assertThat(file).exists();
    }

    @Test
    void shouldReturnSavedRecord() {
      final var file =
          new File(
              TEST_FILE_ABSOLUTE_PREFIX + TEST_FILES_PATH + "/prefix_record_test_parameter.json");
      assertThat(file).doesNotExist();
      final var testRecord = new TestRecord("test_parameter");
      final var result = testService.dtoCallWithRecordType(testRecord);

      assertThat(result.value()).isEqualTo(EXPECTED_DYNAMIC_VALUE);
      assertThat(file).exists();
    }
  }

  @Nested
  @DisplayName("Collection related test cases")
  class CollectionTests {
    @Test
    void shouldGenerateCollectionListJson() {
      final var file =
          new File(TEST_FILE_ABSOLUTE_PREFIX + TEST_FILES_PATH + "/collection_list.json");
      assertThat(file).doesNotExist();
      final var result = testService.dtoCallWithListResponse();

      assertThat(result).hasSize(1);
      assertThat(file).exists();
    }

    @Test
    void shouldGenerateCollectionSetJson() {
      final var file =
          new File(TEST_FILE_ABSOLUTE_PREFIX + TEST_FILES_PATH + "/collection_set.json");
      assertThat(file).doesNotExist();
      final var result = testService.dtoCallWithSetResponse();

      assertThat(result).hasSize(1);
      assertThat(file).exists();
    }

    @Test
    void shouldGenerateCollectionMapJson() {
      final var file =
          new File(TEST_FILE_ABSOLUTE_PREFIX + TEST_FILES_PATH + "/collection_map.json");
      assertThat(file).doesNotExist();
      final var result = testService.dtoCallWithMapResponse();

      assertThat(result).hasSize(1);
      assertThat(file).exists();
    }

    @Test
    void shouldGenerateCollectionArrayJson() {
      final var file =
          new File(TEST_FILE_ABSOLUTE_PREFIX + TEST_FILES_PATH + "/collection_array.json");
      assertThat(file).doesNotExist();
      final var result = testService.dtoCallWithArrayResponse();

      assertThat(result).hasSize(1);
      assertThat(file).exists();
    }
  }
}
