package apsd.interfaces.containers.base;

import apsd.classes.utilities.Natural;
import apsd.interfaces.traits.Reallocable;

public interface ReallocableContainer extends ClearableContainer, Reallocable {

  double GROW_FACTOR = 2.0;
  double SHRINK_FACTOR = 2.0;

  Natural Capacity();

  void Grow();

  void Grow(Natural n);

  void Shrink();

  @Override
  Natural Size();

  @Override
  void Clear();

}