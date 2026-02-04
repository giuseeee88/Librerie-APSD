package apsd.interfaces.containers.base;

import apsd.classes.utilities.Natural;

public interface Container {
  
  Natural Size();
 
  default boolean IsEmpty() {
    return Size().IsZero();
  }
}