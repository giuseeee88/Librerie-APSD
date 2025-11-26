package apsd.interfaces.containers.sequences;

import apsd.classes.utilities.Natural;

/** Interface: Sequence con supporto alla rimozione di un dato tramite posizione. */
public interface RemovableAtSequence<Data> extends Sequence<Data> { // Must extend Sequence
  void RemoveAt(Natural n);
  Data AtNRemove(Natural n);
  void RemoveFirst();
  Data FirstNRemove();
  void RemoveLast();
  Data LastNRemove();

}
