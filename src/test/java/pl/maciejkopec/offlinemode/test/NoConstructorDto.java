package pl.maciejkopec.offlinemode.test;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoConstructorDto {

  private String value;
}
