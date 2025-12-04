package apsd.interfaces.containers.iterators;

import apsd.classes.utilities.Natural;
import apsd.interfaces.traits.Predicate;

public interface BackwardIterator<Data> extends Iterator<Data> {

  void Prev();

  void Prev(Natural n);

  Data DataNPrev();

  boolean ForEachBackward(Predicate<Data> predicate);

}