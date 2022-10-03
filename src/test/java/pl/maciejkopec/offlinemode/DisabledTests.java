package pl.maciejkopec.offlinemode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import pl.maciejkopec.offlinemode.test.TestApplication;
import pl.maciejkopec.offlinemode.test.TestService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = TestApplication.class)
@TestPropertySource(
    value = "classpath:application.yaml",
    properties = {
      "offline-mode.enabled=false",
      "offline-mode.mode=serving",
      "offline-mode.path=src/test/resources/data/serving"
    })
class DisabledTests {

  @Autowired private TestService testService;
  private static final String EXPECTED_DYNAMIC_VALUE = "dynamic_data";

  @Test
  void shouldCallActualMethodWhenDisabled() {
    final var result = testService.simpleCall();

    assertThat(result).isEqualTo(EXPECTED_DYNAMIC_VALUE);
  }
}
