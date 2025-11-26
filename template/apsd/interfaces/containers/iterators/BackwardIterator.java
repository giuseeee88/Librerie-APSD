package apsd.interfaces.containers.iterators;

import apsd.classes.utilities.Natural;
import apsd.interfaces.traits.Predicate;

/** Interface: Iteratore all'indietro. */
public interface BackwardIterator<Data> extends Iterator<Data> { // Must extend Iterator

  void Prev();
  
  void Prev(Natural n);

  Data DataNPrev(Data dat);

  boolean ForEachBackward(Predicate<Data> predicate);

}