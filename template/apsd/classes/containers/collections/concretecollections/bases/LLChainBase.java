package apsd.classes.containers.collections.concretecollections.bases;

import apsd.classes.containers.sequences.Vector;
import apsd.classes.utilities.Box;
import apsd.classes.utilities.MutableNatural;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.iterators.BackwardIterator;
import apsd.interfaces.containers.iterators.ForwardIterator;
import apsd.interfaces.containers.iterators.MutableBackwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
import apsd.interfaces.containers.sequences.Sequence;
import apsd.interfaces.traits.Predicate;

/** Object: Abstract chain base implementation on linked-list. */
abstract public class LLChainBase<Data> implements Chain<Data> {

  protected final MutableNatural size = new MutableNatural();
  protected final Box<LLNode<Data>> headref = new Box<>();
  protected final Box<LLNode<Data>> tailref = new Box<>();

  public LLChainBase(TraversableContainer<Data> con) {
    size.Assign(con.Size());
    final Box<Boolean> first = new Box<>(true);
    con.TraverseForward(dat -> {
      LLNode<Data> node = new LLNode<>(dat);
      if (first.Get()) {
        headref.Set(node);
        first.Set(false);
      } else {
        tailref.Get().SetNext(node);
      }
      tailref.Set(node);
      return false;
    });
  }

  // Metodo Factory Astratto (implementato da LLList e LLSortedChain)
  abstract public LLChainBase<Data> NewChain(long capacity, LLNode<Data> head, LLNode<Data> tail);

  /* ************************************************************************ */
  /* Specific member functions from LLChainBase                               */
  /* ************************************************************************ */

  protected MutableForwardIterator<Box<LLNode<Data>>> FRefIterator() {
    return new MutableForwardIterator<Box<LLNode<Data>>>() {
      // Current box pointing to the node we are visiting
      Box<LLNode<Data>> current = headref;

      @Override
      public boolean IsValid() {
        return current != null && !current.IsNull();
      }

      @Override
      public Box<LLNode<Data>> GetCurrent() {
        if (!IsValid()) throw new IllegalStateException();
        return current;
      }

      @Override
      public void SetCurrent(Box<LLNode<Data>> dat) {
        // Not supported/needed for RefIterator logic usually
        throw new UnsupportedOperationException();
      }

      @Override
      public void Next() {
        if (IsValid()) {
          current = current.Get().GetNext();
        }
      }

      @Override
      public void Next(Natural n) {
        for (long i = 0; i < n.ToLong() && IsValid(); i++) {
          Next();
        }
      }

      @Override
      public Box<LLNode<Data>> DataNNext() {
        Box<LLNode<Data>> d = GetCurrent();
        Next();
        return d;
      }

      @Override
      public void Reset() {
        current = headref;
      }
    };
  }

  /* ************************************************************************ */
  /* Override specific member functions from Container                        */
  /* ************************************************************************ */

  @Override
  public Natural Size() {
    return new Natural(size);
  }

  @Override
  public boolean IsEmpty() {
    return size.IsZero();
  }

  /* ************************************************************************ */
  /* Override specific member functions from ClearableContainer               */
  /* ************************************************************************ */

  @Override
  public void Clear() {
    headref.Set(null);
    tailref.Set(null);
    size.Zero();
  }

  /* ************************************************************************ */
  /* Override specific member functions from RemovableContainer               */
  /* ************************************************************************ */

  @Override
  public boolean Remove(Data dat) {
    if (dat == null || IsEmpty()) return false;
    
    // Box ausiliario per tracciare il predecessore (necessario per aggiornare tailref)
    final Box<LLNode<Data>> prevNodeBox = new Box<>(null); 
    
    return FRefIterator().ForEachForward(curBox -> {
      LLNode<Data> node = curBox.Get();
      
      if (node.Get().equals(dat)) {
        // Unlink: curBox (che è headref o un next) ora punta al successivo del nodo corrente
        curBox.Set(node.GetNext().Get());
        
        // Fix Tail: Se abbiamo rimosso la coda, la nuova coda è il nodo precedente
        if (tailref.Get() == node) {
          tailref.Set(prevNodeBox.Get()); // Se prev è null, la lista diventa vuota (corretto)
        }
        
        size.Decrement();
        return true; // Stop dopo la prima rimozione
      }
      
      prevNodeBox.Set(node); // Avanzamento del predecessore
      return false;
    });
  }

  /* ************************************************************************ */
  /* Override specific member functions from IterableContainer                */
  /* ************************************************************************ */

  @Override
  public ForwardIterator<Data> FIterator() {
    return new ForwardIterator<Data>() {
      MutableForwardIterator<Box<LLNode<Data>>> refIter = FRefIterator();

      @Override
      public boolean IsValid() { return refIter.IsValid(); }
      @Override
      public Data GetCurrent() { return refIter.GetCurrent().Get().Get(); }
      @Override
      public void Next() { refIter.Next(); }
      @Override
      public void Next(Natural n) { refIter.Next(n); }
      @Override
      public Data DataNNext() { return refIter.DataNNext().Get().Get(); }
      @Override
      public void Reset() { refIter.Reset(); }
      // Default ForEachForward is used
    };
  }

  @Override
  public BackwardIterator<Data> BIterator() {
    // Lista concatenata singola: iterazione all'indietro inefficiente.
    // Copiamo in un vettore temporaneo per iterare all'indietro.
    Vector<Data> tempVec = new Vector<>(this);
    return tempVec.BIterator();
  }
  
  @Override
  public boolean IsEqual(IterableContainer<Data> container) {
      if (container == null || Size().compareTo(container.Size()) != 0) return false;
      final ForwardIterator<Data> iter = FIterator();
      return !container.TraverseForward(dat -> {
          Data myDat = iter.DataNNext();
          return !myDat.equals(dat);
      });
  }
  
  @Override
  public boolean TraverseForward(Predicate<Data> predicate) {
      MutableForwardIterator<Box<LLNode<Data>>> it = FRefIterator();
      while(it.IsValid()) {
          if (predicate.Apply(it.DataNNext().Get().Get())) return true;
      }
      return false;
  }
  
  @Override
  public boolean TraverseBackward(Predicate<Data> predicate) {
      Vector<Data> tempVec = new Vector<>(this);
      return tempVec.TraverseBackward(predicate);
  }

  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  @Override
  public Data GetAt(Natural n) {
    if (n.compareTo(Size()) >= 0) throw new IndexOutOfBoundsException();
    MutableForwardIterator<Box<LLNode<Data>>> it = FRefIterator();
    it.Next(n);
    return it.GetCurrent().Get().Get();
  }

  @Override
  public Data GetFirst() {
    if (IsEmpty()) throw new IndexOutOfBoundsException();
    return headref.Get().Get();
  }

  @Override
  public Data GetLast() {
    if (IsEmpty()) throw new IndexOutOfBoundsException();
    return tailref.Get().Get();
  }

  @Override
  public boolean IsInBound(Natural n) {
    return n.compareTo(Size()) < 0;
  }

  @Override
  public Sequence<Data> SubSequence(Natural start, Natural end) {
    if (start.compareTo(end) > 0 || end.compareTo(Size()) > 0) throw new IndexOutOfBoundsException();
    long len = end.ToLong() - start.ToLong();
    
    // Creiamo i nodi per la nuova catena
    LLNode<Data> newHead = null;
    LLNode<Data> newTail = null;
    
    if (len > 0) {
        MutableForwardIterator<Box<LLNode<Data>>> it = FRefIterator();
        it.Next(start);
        
        for (long i = 0; i < len; i++) {
            LLNode<Data> newNode = new LLNode<>(it.DataNNext().Get().Get());
            if (newHead == null) {
                newHead = newNode;
            } else {
                newTail.SetNext(newNode);
            }
            newTail = newNode;
        }
    }
    
    // Usiamo il Factory Method astratto
    return NewChain(len, newHead, newTail);
  }

  /* ************************************************************************ */
  /* Override specific member functions from RemovableAtSequence              */
  /* ************************************************************************ */

  @Override
  public void RemoveAt(Natural n) {
    AtNRemove(n);
  }

  @Override
  public Data AtNRemove(Natural n) {
    if (n.compareTo(Size()) >= 0) throw new IndexOutOfBoundsException();

    // Navighiamo fino al box che punta al nodo target
    Box<LLNode<Data>> curBox = headref;
    LLNode<Data> prevNode = null;

    long idx = n.ToLong();
    for (long i = 0; i < idx; i++) {
      prevNode = curBox.Get();
      curBox = prevNode.GetNext();
    }

    LLNode<Data> nodeToRemove = curBox.Get();
    Data dat = nodeToRemove.Get();

    // Unlink
    curBox.Set(nodeToRemove.GetNext().Get());

    // Fix Tail
    if (tailref.Get() == nodeToRemove) {
      tailref.Set(prevNode);
    }

    size.Decrement();
    return dat;
  }

  @Override
  public void RemoveFirst() {
    AtNRemove(Natural.ZERO);
  }

  @Override
  public Data FirstNRemove() {
    return AtNRemove(Natural.ZERO);
  }

  @Override
  public void RemoveLast() {
    AtNRemove(Size().Decrement());
  }

  @Override
  public Data LastNRemove() {
    return AtNRemove(Size().Decrement());
  }

  /* ************************************************************************ */
  /* Override specific member functions from Collection                       */
  /* ************************************************************************ */

  @Override
  public boolean Filter(Predicate<Data> predicate) {
      boolean modified = false;
      Box<LLNode<Data>> curBox = headref;
      LLNode<Data> prevNode = null;

      while (curBox != null && !curBox.IsNull()) {
          LLNode<Data> node = curBox.Get();
          // Filter solitamente MANTIENE se true, RIMUOVE se false.
          // Se predicate.Apply(data) è false -> Rimuovi.
          
          if (!predicate.Apply(node.Get())) {
              // Remove
              curBox.Set(node.GetNext().Get());
              if (tailref.Get() == node) {
                  tailref.Set(prevNode);
              }
              size.Decrement();
              modified = true;
              // Non avanziamo curBox/prevNode, perché curBox ora punta già al prossimo
          } else {
              // Advance
              prevNode = node;
              curBox = node.GetNext();
          }
      }
      return modified;
  }

  @Override
  public boolean Exists(Data dat) {
      MutableForwardIterator<Box<LLNode<Data>>> it = FRefIterator();
      while(it.IsValid()) {
          Data d = it.DataNNext().Get().Get();
          if ((d == null && dat == null) || (d != null && d.equals(dat))) return true;
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
public boolean InsertIfAbsent(Data dat) {
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