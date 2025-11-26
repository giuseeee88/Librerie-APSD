package apsd.classes.containers.collections.abstractcollections;

import apsd.classes.containers.collections.abstractcollections.bases.WOrderedSetBase;
import apsd.classes.containers.collections.concretecollections.VSortedChain;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Set;
import apsd.interfaces.containers.collections.SortedChain;

/** Object: Wrapper ordered set implementation via ordered chain. */
public class WOrderedSet<Data extends Comparable<? super Data>> extends WOrderedSetBase<Data, VSortedChain<Data>> {

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

  // public WOrderedSet()

  // public WOrderedSet(Chain<Data> chn)

  // public WOrderedSet(TraversableContainer<Data> con)

  // public WOrderedSet(Chain<Data> chn, TraversableContainer<Data> con)

  // ...

}
