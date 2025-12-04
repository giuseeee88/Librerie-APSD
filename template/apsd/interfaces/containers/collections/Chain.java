package apsd.interfaces.containers.collections;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.sequences.RemovableAtSequence;

public interface Chain<Data> extends Collection<Data>, RemovableAtSequence<Data> {

  boolean InsertIfAbsent(Data dat);

  void RemoveOccurrences(Data dat);

  Chain<Data> SubChain(Natural start, Natural end);

  @Override
  Natural Search(Data dat);

}