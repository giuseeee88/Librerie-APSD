package apsd.classes.containers.collections.abstractcollections;

import apsd.classes.containers.collections.abstractcollections.bases.WSetBase;
import apsd.classes.containers.collections.concretecollections.VList;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.collections.Set;

/** Object: Wrapper set implementation via chain. */
public class WSet<Data> extends WSetBase<Data, VList<Data>> {

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
		
	} // Must extend WSetBase

  // public WSet()

  // public WSet(Chain<Data> chn)

  // public WSet(TraversableContainer<Data> con)

  // public WSet(Chain<Data> chn, TraversableContainer<Data> con)

  // @Override
  // protected void ChainAlloc() { chn = new VList<>(); }

}
