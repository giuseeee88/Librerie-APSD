package apsd.interfaces.containers.sequences;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.ResizableContainer;

public interface DynVector<Data> extends ResizableContainer, InsertableAtSequence<Data>, RemovableAtSequence<Data>, Vector<Data> { // Must extend ResizableContainer, InsertableAtSequence, RemovableAtSequence, and Vector
	
  @Override
  void InsertAt(Data dat, Natural pos);
  
  @Override
  Data AtNRemove(Natural pos);
  
  @Override
  void ShiftLeft(Natural pos1, Natural pos2);
  
  @Override
  void ShiftRight(Natural pos1, Natural pos2);
  
  @Override
  DynVector<Data> SubVector(Natural start, Natural end);
  
  @Override
  Natural Size();

}
