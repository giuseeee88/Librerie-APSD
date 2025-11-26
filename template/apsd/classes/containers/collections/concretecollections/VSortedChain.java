package apsd.classes.containers.collections.concretecollections;

import apsd.classes.containers.collections.concretecollections.bases.VChainBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Set;
import apsd.interfaces.containers.collections.SortedChain;
import apsd.interfaces.containers.sequences.DynVector;

/** Object: Concrete set implementation via (dynamic circular) vector. */
public class VSortedChain<Data extends Comparable<? super Data>> extends VChainBase<Data> implements SortedChain<Data>{

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

  // public VSortedChain()

  // public VSortedChain(VSortedChain<Data> chn)

  // public VSortedChain(TraversableContainer<Data> con)

  // protected VSortedChain(DynVector<Data> vec)

  // NewChain

  /* ************************************************************************ */
  /* Override specific member functions from InsertableContainer              */
  /* ************************************************************************ */

  // ...

  /* ************************************************************************ */
  /* Override specific member functions from Chain                            */
  /* ************************************************************************ */

  // ...

}
