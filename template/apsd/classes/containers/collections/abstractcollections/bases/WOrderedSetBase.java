package apsd.classes.containers.collections.abstractcollections.bases;

import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.collections.OrderedSet;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.OrderedSet;
import apsd.interfaces.containers.collections.SortedChain;

/** Object: Abstract wrapper set base implementation via chain. */
abstract public class WOrderedSetBase<Data extends Comparable<? super Data>, Chn extends SortedChain<Data>> extends WSetBase<Data, Chn> implements OrderedSet<Data> {
  // WOrderedSetBase

  /* ************************************************************************ */
  /* Override specific member functions from IterableContainer                */
  /* ************************************************************************ */

  // ...

  /* ************************************************************************ */
  /* Override specific member functions from OrderedSet                       */
  /* ************************************************************************ */

  // ...

}
