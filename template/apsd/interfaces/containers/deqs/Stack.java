package apsd.interfaces.containers.deqs;

import apsd.interfaces.containers.base.ClearableContainer;
import apsd.interfaces.containers.base.InsertableContainer;

public interface Stack<Data> extends ClearableContainer, InsertableContainer<Data> {

  Data Top();

  void Pop();

  Data TopNPop();

  void SwapTop(Data dat);

  Data TopNSwap(Data dat);

  void Push(Data dat);

  @Override
  void Clear();

  @Override
  boolean Insert(Data dat);

}