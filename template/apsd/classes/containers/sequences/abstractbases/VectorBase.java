package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.MutableNatural;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.iterators.MutableBackwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
import apsd.interfaces.containers.sequences.MutableSequence;
import apsd.interfaces.containers.sequences.Vector;

/** Object: Abstract vector base implementation. */
abstract public class VectorBase<Data> implements Vector<Data> { // Must implement Vector

  protected Data[] arr;

  // VectorBase

  void NewVector(Data[] data) {
	  arr = data;  
  }

  @SuppressWarnings("unchecked")
  protected void ArrayAlloc(Natural newsize) {
    long size = newsize.ToLong();
    if (size >= Integer.MAX_VALUE) { throw new ArithmeticException("Overflow: size cannot exceed Integer.MAX_VALUE!"); }
    arr = (Data[]) new Object[(int) size];
  }
  
  @Override
  public void Clear() {
	
  }
  
  @Override
  public Natural Capacity() {
  	
  }
  
  @Override
  public MutableForwardIterator<Data> FIterator() {
	
  }
  
  @Override
  public MutableBackwardIterator<Data> BIterator() {
	
  }
  
  @Override
  public MutableSequence<Data> SubSequence(Natural start, Natural end) {
	
  }
}