package apsd.interfaces.containers.collections;

public interface OrderedSet<Data extends Comparable<? super Data>> extends Set<Data> { // Must extend Set

  Data min();
  Data max();

  void RemoveMin();
  void RemoveMax();

  Data MinNRemove();
  Data MaxNRemove();

  Data Predecessor(Data dat);
  Data Successor(Data dat);

  void RemovePredecessor(Data dat);
  void RemoveSuccessor(Data dat);

  Data PredecessorNRemove(Data dat);
  Data SuccessorNRemove(Data dat);

}
