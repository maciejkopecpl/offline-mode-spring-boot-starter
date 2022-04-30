package pl.maciejkopec.offlinemode.expression;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Class describing the root object used during the expression evaluation. */
@Getter
@RequiredArgsConstructor
class ExpressionRootObject {
  private final Object object;
  private final Object[] args;
}
