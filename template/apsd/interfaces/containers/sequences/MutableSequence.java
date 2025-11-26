package apsd.interfaces.containers.sequences;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.MutableIterableContainer;
import apsd.interfaces.containers.iterators.MutableForwardIterator;

/** Interface: Sequence & MutableIterableContainer con supporto alla scrittura tramite posizione. */
public interface MutableSequence<Data> extends Sequence<Data>, MutableIterableContainer<Data> { // Must extend Sequence and MutableIterableContainer

  void SetAt(Data dat, Natural n);

  Data GetNSetAt(Data dat, Natural n);

  void SetFirst(Data dat);

  Data GetNSetFirst(Data dat);

  void SetLast(Data dat);

  Data GetNSetLast(Data dat);

  void Swap(Natural pos1, Natural pos2);
  
  @Override
  MutableSequence<Data> SubSequence(Natural start, Natural end);

}
