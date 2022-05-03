package pl.maciejkopec.offlinemode;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import pl.maciejkopec.offlinemode.test.TestApplication;
import pl.maciejkopec.offlinemode.test.TestFullDto;
import pl.maciejkopec.offlinemode.test.TestFullDtoWithoutEquals;
import pl.maciejkopec.offlinemode.test.TestService;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.maciejkopec.offlinemode.LearningModeTests.TEST_FILES_PATH;

@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
@TestPropertySource(
    value = "classpath:application.yaml",
    properties = {
      "offline-mode.mode=learning",
      "offline-mode.path=" + TEST_FILES_PATH
    })
class LearningModeTests {

  @Autowired private TestService testService;

  static final String TEST_FILES_PATH = "src/test/resources/data/learning";
  private static final String EXPECTED_DYNAMIC_VALUE = "dynamic_data";

  @AfterEach
  void tearDown() {
    Arrays.stream(Objects.requireNonNull(new File(TEST_FILES_PATH).listFiles())).forEach(File::delete);
  }

  @Test
  void smokeTest() {
    assertThat(testService).isNotNull();
  }

  @Test
  void shouldReturnSavedSimpleResponse() {
    final File file = new File(TEST_FILES_PATH + "/pl.maciejkopec.offlinemode.test.TestService_simpleCall_.json");
    assertThat(file).doesNotExist();

    final String result = testService.simpleCall();

    assertThat(result).isEqualTo(EXPECTED_DYNAMIC_VALUE);
    assertThat(file).exists();

  }

  @Test
  void shouldReturnSavedFullDtoResponse() {
    final File file = new File(TEST_FILES_PATH + "/pl.maciejkopec.offlinemode.test.TestService_dtoCall_.json");
    assertThat(file).doesNotExist();
    final TestFullDto result = testService.dtoCall();

    assertThat(result.getValue()).isEqualTo(EXPECTED_DYNAMIC_VALUE);
    assertThat(file).exists();

  }

  @Test
  void shouldReturnSavedFullDtoResponseWithParam() {
    final File file = new File(TEST_FILES_PATH + "/pl.maciejkopec.offlinemode.test.TestService_dtoCall_dynamic_data.json");
    assertThat(file).doesNotExist();

    final TestFullDto result = testService.dtoCall("dynamic_data");

    assertThat(result.getValue()).isEqualTo(EXPECTED_DYNAMIC_VALUE);
    assertThat(file).exists();

  }

  @Test
  void shouldReturnSavedTestFullDtoWithoutEquals() {
    final File file = new File(TEST_FILES_PATH + "/test.json");
    assertThat(file).doesNotExist();

    final TestFullDtoWithoutEquals complexObject = new TestFullDtoWithoutEquals("value");
    final TestFullDto result = testService.dtoCallWithCustomStaticKey("dynamic_data", complexObject);

    assertThat(result.getValue()).isEqualTo(EXPECTED_DYNAMIC_VALUE);
    assertThat(file).exists();
  }



  @Test
  void shouldReturnSavedTestFullDto() {
    final File file = new File(TEST_FILES_PATH + "/prefix_test_parameter.json");
    assertThat(file).doesNotExist();
    final TestFullDtoWithoutEquals complexObject = new TestFullDtoWithoutEquals("test_parameter");
    final TestFullDto result = testService.dtoCallWithCustomComplexKey(complexObject);

    assertThat(result.getValue()).isEqualTo(EXPECTED_DYNAMIC_VALUE);
    assertThat(file).exists();
  }

}
