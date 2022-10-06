package pl.maciejkopec.offlinemode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import pl.maciejkopec.offlinemode.test.*;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
@TestPropertySource(
    value = "classpath:application.yaml",
    properties = {"offline-mode.mode=serving", "offline-mode.path=data/serving"})
class ServingModeTests {

  @Autowired private TestService testService;

  private static final String EXPECTED_STATIC_VALUE = "static_value";
  private static final String EXPECTED_DYNAMIC_VALUE = "dynamic_data";

  @Test
  void smokeTest() {
    assertThat(testService).isNotNull();
  }

  @Nested
  @DisplayName("General tests cases")
  class GeneralTests {
    @Test
    void shouldReturnSavedSimpleResponse() {
      final var result = testService.simpleCall();

      assertThat(result).isEqualTo(EXPECTED_STATIC_VALUE);
    }

    @Test
    void shouldReturnSavedFullDtoResponse() {
      final var result = testService.dtoCall();

      assertThat(result.getValue()).isEqualTo(EXPECTED_STATIC_VALUE);
    }

    @Test
    void shouldReturnSavedFullDtoResponseWithParam() {
      final var result = testService.dtoCall("param_value");

      assertThat(result.getValue()).isEqualTo(EXPECTED_STATIC_VALUE);
    }

    @Test
    void shouldReturnSavedFullDtoResponseWithComplexParam() {
      final var complexObject = new TestFullDto();
      complexObject.setValue("complex");
      final var result = testService.dtoCall("param_value", complexObject);

      assertThat(result.getValue()).isEqualTo(EXPECTED_STATIC_VALUE);
    }

    @Test
    void shouldReturnSavedFullDtoResponseWithComplexParamDifferentValue() {
      final var complexObject = new TestFullDto();
      complexObject.setValue("complex_different_value");
      final var result = testService.dtoCall("param_value", complexObject);

      assertThat(result.getValue()).isEqualTo(EXPECTED_STATIC_VALUE);
    }

    @Test
    void shouldReturnSavedTestFullDtoWithoutEquals() {
      final var complexObject = new TestFullDtoWithoutEquals("value");
      final var result = testService.dtoCallWithCustomStaticKey("param_value", complexObject);

      assertThat(result.getValue()).isEqualTo(EXPECTED_STATIC_VALUE);
    }

    @Test
    void shouldReturnSavedTestFullDto() {
      final var complexObject = new TestFullDtoWithoutEquals("test_parameter");
      final var result = testService.dtoCallWithCustomComplexKey(complexObject);

      assertThat(result.getValue()).isEqualTo(EXPECTED_STATIC_VALUE);
    }

    @Test
    void shouldReuseCachedExpression() {
      final var complexObject = new TestFullDtoWithoutEquals("test_parameter");
      testService.dtoCallWithCustomComplexKey(complexObject);
      final var result = testService.dtoCallWithCustomComplexKey(complexObject);

      assertThat(result.getValue()).isEqualTo(EXPECTED_STATIC_VALUE);
    }

    @Test
    void shouldNotFailIfDataFileIsMissing() {
      final var result = testService.missConfigured();

      assertThat(result.getValue()).isEqualTo(EXPECTED_DYNAMIC_VALUE);
    }

    @Test
    void shouldReturnSavedTestRecordType() {
      final var testRecord = new TestRecord("test_parameter");
      final var result = testService.dtoCallWithRecordType(testRecord);

      assertThat(result.value()).isEqualTo(EXPECTED_STATIC_VALUE);
    }
  }

  @Nested
  @DisplayName("Tests related with collection return types")
  class CollectionReturnTypeTests {
    @Test
    void shouldReturnSavedCollectionListJson() {
      final var result = testService.dtoCallWithListResponse();

      assertThat(result).hasSize(1);
      assertThat(result.get(0).value()).isEqualTo(EXPECTED_STATIC_VALUE);
    }

    @Test
    void shouldReturnSavedCollectionSetJson() {
      final var result = testService.dtoCallWithSetResponse();

      assertThat(result).hasSize(1);
      assertThat(result.stream().findFirst().get().value()).isEqualTo(EXPECTED_STATIC_VALUE);
    }

    @Test
    void shouldReturnSavedCollectionMapJson() {
      final var result = testService.dtoCallWithMapResponse();

      assertThat(result).hasSize(1);
      assertThat(result.get("key").value()).isEqualTo(EXPECTED_STATIC_VALUE);
    }

    @Test
    void shouldReturnSavedCollectionArrayJson() {
      final var result = testService.dtoCallWithArrayResponse();

      assertThat(result).hasSize(1);
      assertThat(Arrays.stream(result).findFirst().get().value()).isEqualTo(EXPECTED_STATIC_VALUE);
    }
  }

  @Nested
  @DisplayName("Deserialization related tests")
  class DeserializationTests {

    @Test
    void shouldDeserializeWithoutConstructor() {
      final var result = testService.noConstructorDtoCall();

      assertThat(result).hasSize(1);
      assertThat(result.get(0).getValue()).isEqualTo(EXPECTED_STATIC_VALUE);
    }
  }
}
