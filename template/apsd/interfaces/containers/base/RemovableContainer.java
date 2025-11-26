package apsd.interfaces.containers.base;

/** Interface: Container con supporto alla rimozione di un dato. */
public interface RemovableContainer<Data> extends Container { // Must extend Container

  boolean Remove(Data dat);

  boolean RemoveAll(TraversableContainer<Data> container);

  boolean RemoveSome(TraversableContainer<Data> container);

}