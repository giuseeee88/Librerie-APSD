package apsd.interfaces.containers.collections;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.sequences.SortedSequence;

public interface SortedChain<Data extends Comparable<? super Data>> extends OrderedChain<Data>, SortedSequence<Data> {

  Natural SearchPredecessor(Data dat);

  Natural SearchSuccessor(Data dat);

  default void Intersection(SortedChain<Data> chn) {
	    Natural i = Natural.ZERO, j = Natural.ZERO;
	    while (i.compareTo(Size()) < 0 && j.compareTo(chn.Size()) < 0) {
	      int cmp = GetAt(i).compareTo(chn.GetAt(j));
	      if (cmp < 0) {
	        RemoveAt(i);
	      } else {
	        j = j.Increment();
	        if (cmp == 0) { i = i.Increment(); }
	      }
	    }
	    while (i.compareTo(Size()) < 0) {
	      RemoveAt(i);
	    }
	  }

  @Override
  Natural Search(Data dat);

  @Override
  Data Min();

  @Override
  Data Max();

  @Override
  void RemoveMin();

  @Override
  void RemoveMax();

  @Override
  Data MinNRemove();

  @Override
  Data MaxNRemove();

  @Override
  Data Predecessor(Data dat);

  @Override
  Data Successor(Data dat);

  @Override
  void RemovePredecessor(Data dat);

  @Override
  void RemoveSuccessor(Data dat);

  @Override
  Data PredecessorNRemove(Data dat);

  @Override
  Data SuccessorNRemove(Data dat);

}