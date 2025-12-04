package apsd.interfaces.containers.base;

import apsd.interfaces.containers.iterators.BackwardIterator;
import apsd.interfaces.containers.iterators.ForwardIterator;
import apsd.interfaces.traits.Predicate;

public interface IterableContainer<Data> extends TraversableContainer<Data> {

  ForwardIterator<Data> FIterator();

  BackwardIterator<Data> BIterator();
  
  boolean IsEqual(IterableContainer<Data> container);

  @Override
  boolean TraverseForward(Predicate<Data> predicate);

  @Override
  boolean TraverseBackward(Predicate<Data> predicate);

}