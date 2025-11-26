package apsd.interfaces.containers.base;

import apsd.classes.utilities.Natural;
import apsd.interfaces.traits.Reallocable;

/** Interface: ClearableContainer che è anche Reallocable. */
public interface ReallocableContainer extends ClearableContainer, Reallocable { // Must extend ClearableContainer, Reallocable

  double GROW_FACTOR = 2.0; // Must be strictly greater than 1.0
  double SHRINK_FACTOR = 2.0; // Must be strictly greater than 1.0

  Natural Capacity();
  void Grow();
  void Grow(Natural n);
  default void Shrink() {};

  @Override
  Natural Size();
  
  @Override
  void Clear();
}
