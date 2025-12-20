package apsd.classes.containers.collections.concretecollections.bases;

import apsd.classes.containers.sequences.Vector;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.iterators.BackwardIterator;
import apsd.interfaces.containers.iterators.ForwardIterator;
import apsd.interfaces.containers.sequences.DynVector;
import apsd.interfaces.containers.sequences.Sequence;
import apsd.interfaces.traits.Predicate;

abstract public class VChainBase<Data> implements Chain<Data> {

  protected DynVector<Data> vec;

  protected void NewChain(DynVector<Data> vec) { this.vec = vec; }

  @Override public Natural Size() { return vec.Size(); }
  @Override public boolean IsEmpty() { return vec.IsEmpty(); }
  @Override public void Clear() { vec.Clear(); }

  @Override
  public boolean Remove(Data dat) {
      Natural res = Search(dat);
      if (res != null) { vec.RemoveAt(res); return true; }
      return false;
  }

  @Override
  public boolean RemoveAll(TraversableContainer<Data> container) {
      if (container == null) return false;
      final boolean[] modified = {false};
      container.TraverseForward(dat -> { if (Remove(dat)) modified[0] = true; return false; });
      return modified[0];
  }
  @Override public boolean RemoveSome(TraversableContainer<Data> container) { return RemoveAll(container); }
  @Override public void RemoveOccurrences(Data dat) { while(Remove(dat)){}; }

  @Override public ForwardIterator<Data> FIterator() { return vec.FIterator(); }
  @Override public BackwardIterator<Data> BIterator() { return vec.BIterator(); }

  @Override public boolean IsEqual(IterableContainer<Data> container) { return vec.IsEqual(container); }
  @Override public boolean TraverseForward(Predicate<Data> predicate) { return vec.TraverseForward(predicate); }
  @Override public boolean TraverseBackward(Predicate<Data> predicate) { return vec.TraverseBackward(predicate); }

  @Override public Data GetAt(Natural n) { return vec.GetAt(n); }
  @Override public Data GetFirst() { if(IsEmpty()) throw new IndexOutOfBoundsException(); return vec.GetFirst(); }
  @Override public Data GetLast() { if(IsEmpty()) throw new IndexOutOfBoundsException(); return vec.GetLast(); }
  @Override public boolean IsInBound(Natural n) { return vec.IsInBound(n); }

  @Override public Sequence<Data> SubSequence(Natural start, Natural end) { return vec.SubSequence(start, end); }
  @Override public Chain<Data> SubChain(Natural start, Natural end) { return (Chain<Data>) SubSequence(start, end); }

  @Override public Natural Search(Data dat) { return vec.Search(dat); }
  @Override public boolean Exists(Data dat) { return vec.Exists(dat); }
  @Override public boolean InsertAll(TraversableContainer<Data> container) {
      if(container==null) return false;
      container.TraverseForward(dat -> { Insert(dat); return false; });
      return true;
  }
  @Override public boolean InsertSome(TraversableContainer<Data> container) { return InsertAll(container); }

  @Override public void RemoveAt(Natural n) { vec.RemoveAt(n); }
  @Override public Data AtNRemove(Natural n) { return vec.AtNRemove(n); }

  // FIX: Controlli di sicurezza !IsEmpty()
  @Override public void RemoveFirst() { if (!IsEmpty()) vec.RemoveFirst(); }
  @Override public Data FirstNRemove() { return (!IsEmpty()) ? vec.FirstNRemove() : null; }
  @Override public void RemoveLast() { if (!IsEmpty()) vec.RemoveLast(); }
  @Override public Data LastNRemove() { return (!IsEmpty()) ? vec.LastNRemove() : null; }

  @Override public boolean Filter(Predicate<Data> predicate) {
      // Implementazione semplice via rimozione
      boolean mod = false;
      // Per evitare problemi di indici durante l'iterazione e rimozione, creiamo una lista di da rimuovere o ricostruiamo
      // Usiamo una strategia sicura: iteriamo e se da rimuovere, rimuoviamo (attenzione indici)
      // Meglio usare traverse backward
      long sz = Size().ToLong();
      for (long i = sz-1; i >= 0; i--) {
          Data d = vec.GetAt(Natural.Of(i));
          if (!predicate.Apply(d)) {
              vec.RemoveAt(Natural.Of(i));
              mod = true;
          }
      }
      return mod;
  }
}