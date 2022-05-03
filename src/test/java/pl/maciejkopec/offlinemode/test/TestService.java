package pl.maciejkopec.offlinemode.test;

import org.springframework.stereotype.Service;
import pl.maciejkopec.offlinemode.annotation.OfflineMode;

@Service
public class TestService {

  public static final String DYNAMIC_DATA = "dynamic_data";

  @OfflineMode
  public String simpleCall() {
    return DYNAMIC_DATA;
  }

  @OfflineMode
  public TestFullDto dtoCall() {
    final TestFullDto testFullDto = new TestFullDto();
    testFullDto.setValue(DYNAMIC_DATA);
    return testFullDto;
  }

  @OfflineMode
  public TestFullDto dtoCall(final String param) {
    final TestFullDto testFullDto = new TestFullDto();
    testFullDto.setValue(param);
    return testFullDto;
  }

  @OfflineMode
  public TestFullDto dtoCall(final String param, final TestFullDto complexObject) {
    final TestFullDto testFullDto = new TestFullDto();
    testFullDto.setValue(param);
    return testFullDto;
  }

  @OfflineMode(key = "'test'")
  public TestFullDto dtoCallWithCustomStaticKey(
      final String param, final TestFullDtoWithoutEquals complexObject) {
    final TestFullDto testFullDto = new TestFullDto();
    testFullDto.setValue(param);
    return testFullDto;
  }

  @OfflineMode(key = "'prefix_' + #complexObject.value")
  public TestFullDto dtoCallWithCustomComplexKey(final TestFullDtoWithoutEquals complexObject) {
    final TestFullDto testFullDto = new TestFullDto();
    testFullDto.setValue(DYNAMIC_DATA);
    return testFullDto;
  }

  @OfflineMode(key = "'missConfigured'")
  public TestFullDto missConfigured(final TestFullDtoWithoutEquals complexObject) {
    final TestFullDto testFullDto = new TestFullDto();
    testFullDto.setValue(DYNAMIC_DATA);
    return testFullDto;
  }
}
