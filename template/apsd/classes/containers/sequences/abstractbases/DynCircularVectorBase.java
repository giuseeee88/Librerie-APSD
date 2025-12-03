package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.sequences.DynVector;

/** Object: Abstract dynamic circular vector base implementation. */
abstract public class DynCircularVectorBase<Data> extends CircularVectorBase<Data> implements DynVector<Data>{ // Must extend CircularVectorBase and implement DynVector

  protected long size = 0L;

  // DynCircularVectorBase
  
  @Override
  public Natural Size() {
	  
  }
  
  @Override
  public void Clear() {
	  
  }
  
  @Override
  public void Realloc(Natural size) {
	  
  }
  
  @Override
  public void Expand(Natural size) {
	  
  }
  
  @Override
  public void Reduce(Natural size) {
	  
  }
  
  @Override
  public void ShiftLeft(Natural start, Natural end) {
	  
  }
  
  @Override
  public void ShiftRight(Natural start, Natural end) {
	  
  }
  
  @Override
  public void ArrayAlloc(Natural newsize) {

  }
}