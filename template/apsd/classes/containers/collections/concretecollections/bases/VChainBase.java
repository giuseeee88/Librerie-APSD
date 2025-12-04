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

/** Object: Abstract list base implementation on (dynamic circular) vector. */
public class VChainBase<Data> implements Chain<Data> {

  // Campo definito nel diagramma a Pagina 10
  protected DynVector<Data> vec;

  // Metodo factory protetto definito nel diagramma a Pagina 10
  protected void NewChain(DynVector<Data> vec) {
    this.vec = vec;
  }

  /* ************************************************************************ */
  /* Override specific member functions from Container                        */
  /* ************************************************************************ */

  @Override
  public Natural Size() {
    return vec.Size();
  }

  @Override
  public boolean IsEmpty() {
    return vec.IsEmpty();
  }

  /* ************************************************************************ */
  /* Override specific member functions from ClearableContainer               */
  /* ************************************************************************ */

  @Override
  public void Clear() {
    vec.Clear();
  }

  /* ************************************************************************ */
  /* Override specific member functions from RemovableContainer               */
  /* ************************************************************************ */

  // Checked nella tabella VChainB
  @Override
  public boolean Remove(Data dat) {
    // Usiamo il Search del vettore sottostante per trovare l'indice
    Natural idx = vec.Search(dat);
    if (idx != null) {
      vec.RemoveAt(idx);
      return true;
    }
    return false;
  }

  /* ************************************************************************ */
  /* Override specific member functions from IterableContainer                */
  /* ************************************************************************ */

  @Override
  public ForwardIterator<Data> FIterator() {
    return vec.FIterator();
  }

  @Override
  public BackwardIterator<Data> BIterator() {
    return vec.BIterator();
  }

  @Override
  public boolean IsEqual(IterableContainer<Data> container) {
      return vec.IsEqual(container);
  }

  @Override
  public boolean TraverseForward(Predicate<Data> predicate) {
      return vec.TraverseForward(predicate);
  }

  @Override
  public boolean TraverseBackward(Predicate<Data> predicate) {
      return vec.TraverseBackward(predicate);
  }

  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  @Override
  public Data GetAt(Natural n) {
    return vec.GetAt(n);
  }

  @Override
  public Data GetFirst() {
    return vec.GetFirst();
  }

  @Override
  public Data GetLast() {
    return vec.GetLast();
  }

  @Override
  public boolean IsInBound(Natural n) {
    return vec.IsInBound(n);
  }

  // Checked nella tabella VChainB
  @Override
  public Sequence<Data> SubSequence(Natural start, Natural end) {
    return vec.SubVector(start, end);
  }

  /* ************************************************************************ */
  /* Override specific member functions from RemovableAtSequence              */
  /* ************************************************************************ */

  @Override
  public void RemoveAt(Natural n) {
    vec.RemoveAt(n);
  }

  // Checked nella tabella VChainB
  @Override
  public Data AtNRemove(Natural n) {
    return vec.AtNRemove(n);
  }

  @Override
  public void RemoveFirst() {
    vec.RemoveFirst();
  }

  @Override
  public Data FirstNRemove() {
    return vec.FirstNRemove();
  }

  @Override
  public void RemoveLast() {
    vec.RemoveLast();
  }

  @Override
  public Data LastNRemove() {
    return vec.LastNRemove();
  }

  /* ************************************************************************ */
  /* Override specific member functions from Collection                       */
  /* ************************************************************************ */

  // Checked nella tabella VChainB
  @Override
  public boolean Filter(Predicate<Data> predicate) {
    // Iteriamo all'indietro per rimuovere in sicurezza senza invalidare indici
    boolean modified = false;
    long sz = Size().ToLong();
    for (long i = sz - 1; i >= 0; i--) {
        Natural idx = Natural.Of(i);
        Data elem = vec.GetAt(idx);
        if (!predicate.Apply(elem)) {
            vec.RemoveAt(idx);
            modified = true;
        }
    }
    return modified;
  }

  @Override
  public boolean Exists(Data dat) {
      return vec.Exists(dat);
  }

  /* ************************************************************************ */
  /* Metodi di Chain (Abstract in Base o Implementati)                        */
  /* ************************************************************************ */

  @Override
  public boolean InsertIfAbsent(Data dat) {
      if (!Exists(dat)) {
          return Insert(dat); // Insert è astratto/delegato alle sottoclassi
      }
      return false;
  }

@Override
public boolean Insert(Data dat) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean InsertAll(TraversableContainer<Data> container) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean InsertSome(TraversableContainer<Data> container) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean RemoveAll(TraversableContainer<Data> container) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean RemoveSome(TraversableContainer<Data> container) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public void RemoveOccurrences(Data dat) {
	// TODO Auto-generated method stub
	
}

@Override
public Chain<Data> SubChain(Natural start, Natural end) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Natural Search(Data dat) {
	// TODO Auto-generated method stub
	return null;
}

}