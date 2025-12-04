package apsd.interfaces.containers.sequences;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;

public interface Sequence<Data> extends IterableContainer<Data> {

  Data GetAt(Natural n);

  Data GetFirst();

  Data GetLast();

  Natural Search(Data dat);

  boolean IsInBound(Natural n);

  default long ExcIfOutOfBound(Natural num) {
    if (num == null) throw new NullPointerException("Natural number cannot be null!");
    long idx = num.ToLong();
    if (idx >= Size().ToLong()) throw new IndexOutOfBoundsException("Index out of bounds: " + idx + "; Size: " + Size() + "!");
    return idx;
  }

  Sequence<Data> SubSequence(Natural start, Natural end);

}