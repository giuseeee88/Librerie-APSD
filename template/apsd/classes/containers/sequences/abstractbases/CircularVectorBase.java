package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;

/** Object: Abstract (static) circular vector base implementation. */
abstract public class CircularVectorBase<Data> extends VectorBase<Data>{ // Must extend VectorBase

  protected long start = 0L;

  // CircularVectorBase
  
  @Override
  public void Realloc(Natural n) {
	  
  }
  
  @Override
  public Data GetAt(Natural n) {
	  
  }
  
  @Override
  public void SetAt(Data dat, Natural n) {
	  
  }
  
  @Override
  public void ShiftLeft(Natural start, Natural end) {
	  
  }
  
  @Override
  public void ShiftRight(Natural start, Natural end) {
	  
  }
  
  @Override
  public void ArrayAlloc(Natural n) {
	  
  }
}
