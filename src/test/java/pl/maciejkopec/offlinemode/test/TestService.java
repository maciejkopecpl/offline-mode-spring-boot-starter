package pl.maciejkopec.offlinemode.test;

import org.springframework.stereotype.Service;
import pl.maciejkopec.offlinemode.annotation.OfflineMode;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TestService {

  public static final String DYNAMIC_DATA = "dynamic_data";

  @OfflineMode
  public String simpleCall() {
    return DYNAMIC_DATA;
  }

  @OfflineMode
  public TestFullDto dtoCall() {
    final var testFullDto = new TestFullDto();
    testFullDto.setValue(DYNAMIC_DATA);
    return testFullDto;
  }

  @OfflineMode
  public TestFullDto dtoCall(final String param) {
    final var testFullDto = new TestFullDto();
    testFullDto.setValue(param);
    return testFullDto;
  }

  @OfflineMode
  public TestFullDto dtoCall(final String param, final TestFullDto complexObject) {
    final var testFullDto = new TestFullDto();
    testFullDto.setValue(param);
    return testFullDto;
  }

  @OfflineMode(key = "'test'")
  public TestFullDto dtoCallWithCustomStaticKey(
      final String param, final TestFullDtoWithoutEquals complexObject) {
    final var testFullDto = new TestFullDto();
    testFullDto.setValue(param);
    return testFullDto;
  }

  @OfflineMode(key = "'prefix_' + #complexObject.value")
  public TestFullDto dtoCallWithCustomComplexKey(final TestFullDtoWithoutEquals complexObject) {
    final var testFullDto = new TestFullDto();
    testFullDto.setValue(DYNAMIC_DATA);
    return testFullDto;
  }

  @OfflineMode(key = "'missConfigured'")
  public TestFullDto missConfigured() {
    final var testFullDto = new TestFullDto();
    testFullDto.setValue(DYNAMIC_DATA);
    return testFullDto;
  }

  @OfflineMode(key = "'prefix_record_' + #testRecord.value")
  public TestRecord dtoCallWithRecordType(final TestRecord testRecord) {
    return new TestRecord(DYNAMIC_DATA);
  }

  @OfflineMode(key = "'collection_list'", elementClass = TestRecord.class)
  public List<TestRecord> dtoCallWithListResponse() {
    return List.of(new TestRecord(DYNAMIC_DATA));
  }

  @OfflineMode(key = "'collection_set'", elementClass = TestRecord.class)
  public Set<TestRecord> dtoCallWithSetResponse() {
    return Set.of(new TestRecord(DYNAMIC_DATA));
  }

  @OfflineMode(key = "'collection_map'", elementClass = TestRecord.class, keyClass = String.class)
  public Map<String, TestRecord> dtoCallWithMapResponse() {
    return Map.of("key", new TestRecord(DYNAMIC_DATA));
  }

  @OfflineMode(key = "'collection_array'", elementClass = TestRecord.class)
  public TestRecord[] dtoCallWithArrayResponse() {
    return new TestRecord[] {new TestRecord(DYNAMIC_DATA)};
  }

  @OfflineMode(key = "'deserialization'", elementClass = NoConstructorDto.class)
  public List<NoConstructorDto> noConstructorDtoCall() {
    return List.of(NoConstructorDto.builder().value("test").build());
  }
}
