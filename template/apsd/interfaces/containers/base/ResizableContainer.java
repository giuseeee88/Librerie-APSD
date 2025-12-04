package apsd.interfaces.containers.base;

import apsd.classes.utilities.Natural;

public interface ResizableContainer extends ReallocableContainer {

  double THRESHOLD_FACTOR = 2.0;

  void Expand();

  void Expand(Natural n);

  void Reduce();

  void Reduce(Natural n);

  @Override
  Natural Size();

  @Override
  void Grow(Natural n);

  @Override
  void Shrink();

}