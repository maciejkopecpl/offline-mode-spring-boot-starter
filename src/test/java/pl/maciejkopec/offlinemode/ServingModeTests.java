package pl.maciejkopec.offlinemode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import pl.maciejkopec.offlinemode.test.TestApplication;
import pl.maciejkopec.offlinemode.test.TestFullDto;
import pl.maciejkopec.offlinemode.test.TestFullDtoWithoutEquals;
import pl.maciejkopec.offlinemode.test.TestService;

@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
@TestPropertySource(
    value = "classpath:application.yaml",
    properties = {
      "offline-mode.mode=serving",
      "offline-mode.path='src/test/resources/data/serving'"
    })
class ServingModeTests {

  @Autowired private TestService testService;

  private static final String EXPECTED_STATIC_VALUE = "static_value";

  @Test
  void smokeTest() {
    assertThat(testService).isNotNull();
  }

  @Test
  void shouldReturnSavedSimpleResponse() {
    final String result = testService.simpleCall();

    assertThat(result).isEqualTo(EXPECTED_STATIC_VALUE);
  }

  @Test
  void shouldReturnSavedFullDtoResponse() {
    final TestFullDto result = testService.dtoCall();

    assertThat(result.getValue()).isEqualTo(EXPECTED_STATIC_VALUE);
  }

  @Test
  void shouldReturnSavedFullDtoResponseWithParam() {
    final TestFullDto result = testService.dtoCall("param_value");

    assertThat(result.getValue()).isEqualTo(EXPECTED_STATIC_VALUE);
  }

  @Test
  void shouldReturnSavedFullDtoResponseWithComplexParam() {
    final TestFullDto complexObject = new TestFullDto();
    complexObject.setValue("complex");
    final TestFullDto result = testService.dtoCall("param_value", complexObject);

    assertThat(result.getValue()).isEqualTo(EXPECTED_STATIC_VALUE);
  }

  @Test
  void shouldReturnSavedFullDtoResponseWithComplexParamDifferentValue() {
    final TestFullDto complexObject = new TestFullDto();
    complexObject.setValue("complex_different_value");
    final TestFullDto result = testService.dtoCall("param_value", complexObject);

    assertThat(result.getValue()).isEqualTo(EXPECTED_STATIC_VALUE);
  }

  @Test
  void shouldReturnSavedTestFullDtoWithoutEquals() {
    final TestFullDtoWithoutEquals complexObject = new TestFullDtoWithoutEquals("value");
    final TestFullDto result = testService.dtoCallWithCustomStaticKey("param_value", complexObject);

    assertThat(result.getValue()).isEqualTo(EXPECTED_STATIC_VALUE);
  }

  @Test
  void shouldReturnSavedTestFullDto() {
    final TestFullDtoWithoutEquals complexObject = new TestFullDtoWithoutEquals("test_parameter");
    final TestFullDto result = testService.dtoCallWithCustomComplexKey(complexObject);

    assertThat(result.getValue()).isEqualTo(EXPECTED_STATIC_VALUE);
  }
}
