package apsd.classes.containers.collections.concretecollections;

import apsd.classes.containers.collections.concretecollections.bases.LLChainBase;
import apsd.classes.containers.collections.concretecollections.bases.LLNode;
import apsd.classes.utilities.Box;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Set;
import apsd.interfaces.containers.collections.SortedChain;
import apsd.interfaces.containers.iterators.ForwardIterator;

/** Object: Concrete sorted chain implementation on linked-list. */
public class LLSortedChain<Data extends Comparable<? super Data>> extends LLChainBase<Data> implements SortedChain<Data> {

	@Override
	public Data min() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data max() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void RemoveMin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveMax() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Data MinNRemove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data MaxNRemove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data Predecessor(Data dat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data Successor(Data dat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void RemovePredecessor(Data dat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void RemoveSuccessor(Data dat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Data PredecessorNRemove(Data dat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data SuccessorNRemove(Data dat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean Exists(Data dat) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Insert(Data dat) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Remove(Data dat) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void Union(Set<Data> set) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Difference(Set<Data> set) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Intersection(Set<Data> set) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean IsEqual(IterableContainer<Data> set) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void Clear() {
		// TODO Auto-generated method stub
		
	}

  // public LLSortedChain()

  // public LLSortedChain(LLSortedChain<Data> chn)

  // public LLSortedChain(TraversableContainer<Data> con)

  // protected LLSortedChain(long size, LLNode<Data> head, LLNode<Data> tail)

  // NewChain

  /* ************************************************************************ */
  /* Specific member functions of LLSortedChain                               */
  /* ************************************************************************ */

  // ...

  /* ************************************************************************ */
  /* Override specific member functions from InsertableContainer              */
  /* ************************************************************************ */

  // ...

  /* ************************************************************************ */
  /* Override specific member functions from RemovableContainer               */
  /* ************************************************************************ */

  // ...

  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  // ...

  /* ************************************************************************ */
  /* Override specific member functions from SortedSequence                   */
  /* ************************************************************************ */

  // ...

  /* ************************************************************************ */
  /* Override specific member functions from OrderedSet                       */
  /* ************************************************************************ */

  // ...

  /* ************************************************************************ */
  /* Override specific member functions from Chain                            */
  /* ************************************************************************ */

  // ...

}
