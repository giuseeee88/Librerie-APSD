package apsd.interfaces.containers.deqs;

import apsd.interfaces.containers.base.ClearableContainer;
import apsd.interfaces.containers.base.InsertableContainer;

public interface Queue<Data> extends ClearableContainer, InsertableContainer<Data> {

  Data Head();

  void Dequeue();

  Data HeadNDequeue();

  void Enqueue(Data dat);

  @Override
  void Clear();

  @Override
  boolean Insert(Data dat);

}