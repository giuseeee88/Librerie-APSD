package apsd.classes.containers.collections.concretecollections.bases;

import apsd.classes.containers.sequences.DynCircularVector;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.iterators.BackwardIterator;
import apsd.interfaces.containers.iterators.ForwardIterator;
import apsd.interfaces.containers.sequences.DynVector;
import apsd.interfaces.containers.sequences.Sequence;
import apsd.interfaces.traits.Predicate;

/** * Object: Abstract list base implementation on (dynamic circular) vector. 
 * Deve essere ABSTRACT perché Insert e SubChain dipendono dalla natura ordinata o meno della sottoclasse.
 */
abstract public class VChainBase<Data> implements Chain<Data> {

  protected DynVector<Data> vec;

  protected void NewChain(DynVector<Data> vec) {
    this.vec = vec;
  }

  /* ************************************************************************ */
  /* Override specific member functions from Container                        */
  /* ************************************************************************ */

  @Override
  public Natural Size() { return vec.Size(); }
  @Override
  public boolean IsEmpty() { return vec.IsEmpty(); }
  @Override
  public void Clear() { vec.Clear(); }

  /* ************************************************************************ */
  /* Override specific member functions from RemovableContainer               */
  /* ************************************************************************ */

  @Override
  public boolean Remove(Data dat) {
    Natural idx = vec.Search(dat);
    if (idx != null) {
      vec.RemoveAt(idx);
      return true;
    }
    return false;
  }

  @Override
  public boolean RemoveAll(TraversableContainer<Data> container) {
      if (container == null) return false;
      final boolean[] modified = {false};
      // Iteriamo sul container esterno e proviamo a rimuovere ogni elemento da noi
      container.TraverseForward(dat -> {
          if (Remove(dat)) modified[0] = true;
          return false; // Continua l'attraversamento
      });
      return modified[0];
  }

  @Override
  public boolean RemoveSome(TraversableContainer<Data> container) {
      return RemoveAll(container);
  }

  /* ************************************************************************ */
  /* Override specific member functions from IterableContainer                */
  /* ************************************************************************ */

  @Override
  public ForwardIterator<Data> FIterator() { return vec.FIterator(); }
  @Override
  public BackwardIterator<Data> BIterator() { return vec.BIterator(); }
  @Override
  public boolean IsEqual(IterableContainer<Data> container) { return vec.IsEqual(container); }
  @Override
  public boolean TraverseForward(Predicate<Data> predicate) { return vec.TraverseForward(predicate); }
  @Override
  public boolean TraverseBackward(Predicate<Data> predicate) { return vec.TraverseBackward(predicate); }

  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  @Override
  public Data GetAt(Natural n) { return vec.GetAt(n); }
  @Override
  public Data GetFirst() { return vec.GetFirst(); }
  @Override
  public Data GetLast() { return vec.GetLast(); }
  @Override
  public boolean IsInBound(Natural n) { return vec.IsInBound(n); }
  @Override
  public Sequence<Data> SubSequence(Natural start, Natural end) { return vec.SubVector(start, end); }

  /* ************************************************************************ */
  /* Override specific member functions from RemovableAtSequence              */
  /* ************************************************************************ */

  @Override
  public void RemoveAt(Natural n) { vec.RemoveAt(n); }
  @Override
  public Data AtNRemove(Natural n) { return vec.AtNRemove(n); }
  @Override
  public void RemoveFirst() { vec.RemoveFirst(); }
  @Override
  public Data FirstNRemove() { return vec.FirstNRemove(); }
  @Override
  public void RemoveLast() { vec.RemoveLast(); }
  @Override
  public Data LastNRemove() { return vec.LastNRemove(); }

  /* ************************************************************************ */
  /* Override specific member functions from Collection                       */
  /* ************************************************************************ */

  @Override
  public boolean Filter(Predicate<Data> predicate) {
    boolean modified = false;
    long sz = Size().ToLong();
    // Iteriamo all'indietro per rimuovere in sicurezza
    for (long i = sz - 1; i >= 0; i--) {
        Natural idx = Natural.Of(i);
        Data elem = vec.GetAt(idx);
        // Se il predicato restituisce false, l'elemento va rimosso
        if (!predicate.Apply(elem)) {
            vec.RemoveAt(idx);
            modified = true;
        }
    }
    return modified;
  }

  @Override
  public boolean Exists(Data dat) { return vec.Exists(dat); }

  /* ************************************************************************ */
  /* Metodi di Chain (Implementati o Astratti)                                */
  /* ************************************************************************ */

  /**
   * Questo metodo è ASTRATTO perché:
   * - In VList è un'append (InsertLast).
   * - In VSortedChain è un inserimento ordinato.
   */
  @Override
  abstract public boolean Insert(Data dat);

  @Override
  public boolean InsertIfAbsent(Data dat) {
      if (!Exists(dat)) {
          return Insert(dat); // Chiama l'implementazione concreta della sottoclasse
      }
      return false;
  }
  
  @Override
  public boolean InsertAll(TraversableContainer<Data> container) {
      if (container == null) return false;
      final boolean[] modified = {false};
      container.TraverseForward(dat -> {
          if (Insert(dat)) modified[0] = true;
          return false;
      });
      return modified[0];
  }

  @Override
  public boolean InsertSome(TraversableContainer<Data> container) {
      return InsertAll(container);
  }

  @Override
  public void RemoveOccurrences(Data dat) {
      // Rimuovi tutti gli elementi uguali a 'dat'.
      // Filter rimuove se il predicato ritorna false.
      // Quindi ritorniamo false se (elem == dat).
      Filter(d -> (d == null) ? dat != null : !d.equals(dat));
  }

  /**
   * Anche SubChain deve essere ASTRATTO (o delegato), perché deve ritornare
   * una nuova istanza della classe concreta (VList o VSortedChain),
   * che VChainBase non conosce.
   */
  @Override
  abstract public Chain<Data> SubChain(Natural start, Natural end);

  @Override
  public Natural Search(Data dat) {
      return vec.Search(dat);
  }
}