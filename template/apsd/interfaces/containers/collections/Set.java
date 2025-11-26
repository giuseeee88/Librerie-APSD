package apsd.interfaces.containers.collections;

import apsd.interfaces.containers.base.IterableContainer;

public interface Set<Data> extends Collection<Data> {
	
	boolean Exists(Data dat);

	boolean Insert(Data dat);
	
	boolean Remove(Data dat);

	void Union(Set<Data> set);
	
	void Difference(Set<Data> set);
	
	void Intersection(Set<Data> set);
	
	boolean IsEqual(IterableContainer<Data> set);

  /* ************************************************************************ */
  /* Override specific member functions from IterableContainer                */
  /* ************************************************************************ */

  // ...

}
