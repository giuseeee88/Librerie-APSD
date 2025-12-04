package apsd.interfaces.containers.base;

/** Interface: Container con supporto all'inserimento di un dato. */
public interface InsertableContainer<Data> extends Container { // Must extend Container

  boolean Insert(Data dat);
  
  boolean InsertAll(TraversableContainer<Data> container);
  
  boolean InsertSome(TraversableContainer<Data> container);

}
