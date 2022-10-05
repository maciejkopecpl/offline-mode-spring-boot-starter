package pl.maciejkopec.offlinemode.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class NoConstructorDtoMixin {

  @JsonCreator
  public NoConstructorDtoMixin(@JsonProperty("value") String value) {}
}
