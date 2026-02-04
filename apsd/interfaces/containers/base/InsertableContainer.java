package apsd.interfaces.containers.base;

public interface InsertableContainer<Data> extends Container {
  
  boolean Insert(Data dat);
  
  default boolean InsertAll(TraversableContainer<Data> container) {
    if (container == null) {
      return false;
    }
    final boolean[] inserted = {false};
    container.TraverseForward(dat -> {
      if (Insert(dat)) {
        inserted[0] = true;
      }
      return false;
    });
    return inserted[0];
  }
  
  default boolean InsertSome(TraversableContainer<Data> container) {
    return InsertAll(container);
  }
}