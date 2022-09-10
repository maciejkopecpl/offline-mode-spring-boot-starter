package pl.maciejkopec.offlinemode.expression;

import java.util.Arrays;
import java.util.Objects;

/** Class describing the root object used during the expression evaluation. */
record ExpressionRootObject(Object object, Object[] args) {

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (ExpressionRootObject) obj;
    return Objects.equals(this.object, that.object) && Arrays.equals(this.args, that.args);
  }

  @Override
  public int hashCode() {
    return Objects.hash(object, Arrays.hashCode(args));
  }

  @Override
  public String toString() {
    return "ExpressionRootObject["
        + "object="
        + object
        + ", "
        + "args="
        + Arrays.toString(args)
        + ']';
  }
}
