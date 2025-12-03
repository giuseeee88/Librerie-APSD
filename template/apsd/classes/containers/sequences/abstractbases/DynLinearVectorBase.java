package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.sequences.DynVector;

/** Object: Abstract dynamic linear vector base implementation. */
abstract public class DynLinearVectorBase<Data> extends LinearVectorBase<Data> implements DynVector<Data>{ // Must extend LinearVectorBase and implement DynVector

  protected long size = 0L;

  // DynLinearVectorBase
  
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
  public void ArrayAlloc(Natural newsize) {

  }
}
