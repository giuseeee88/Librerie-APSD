package apsd.classes.containers.sequences;

import apsd.classes.containers.sequences.abstractbases.CircularVectorBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.traits.Predicate;

/** Object: Concrete (static) circular vector implementation. */
public class CircularVector<Data> extends CircularVectorBase<Data> {

  public CircularVector() {
    this(Natural.ZERO);
  }

  public CircularVector(Natural inisize) {
    ArrayAlloc(inisize);
    // In CircularVectorBase, ArrayAlloc imposta anche start = 0
  }

  public CircularVector(TraversableContainer<Data> con) {
    ArrayAlloc(con.Size());
    con.TraverseForward(new Predicate<Data>() {
      long idx = 0;
      @Override
      public boolean Apply(Data dat) {
        SetAt(dat, Natural.Of(idx++));
        return false; 
      }
    });
  }

  protected CircularVector(Data[] arr) {
    NewVector(arr);
    // Nota: NewVector eredita da VectorBase, imposta arr. 
    // CircularVectorBase assume start=0 di default (campo inizializzato a 0).
  }

  /**
   * Factory method statico (Wrap) per evitare Name Clash con NewVector di istanza.
   */
  public static <Data> CircularVector<Data> Wrap(Data[] arr) {
    return new CircularVector<>(arr);
  }

  /* ************************************************************************ */
  /* Implementazione Size (per vettori statici Size == Capacity)              */
  /* ************************************************************************ */

  @Override
  public Natural Size() {
    return Capacity();
  }

  /* ************************************************************************ */
  /* Implementazione metodi MutableSequence                                   */
  /* ************************************************************************ */

  @Override
  public Data GetNSetAt(Data dat, Natural n) {
    Data old = GetAt(n);
    SetAt(dat, n);
    return old;
  }

  @Override
  public void SetFirst(Data dat) {
    if (IsEmpty()) throw new IndexOutOfBoundsException("Vector is empty");
    SetAt(dat, Natural.ZERO);
  }

  @Override
  public Data GetNSetFirst(Data dat) {
    if (IsEmpty()) throw new IndexOutOfBoundsException("Vector is empty");
    return GetNSetAt(dat, Natural.ZERO);
  }

  @Override
  public void SetLast(Data dat) {
    if (IsEmpty()) throw new IndexOutOfBoundsException("Vector is empty");
    SetAt(dat, Size().Decrement());
  }

  @Override
  public Data GetNSetLast(Data dat) {
    if (IsEmpty()) throw new IndexOutOfBoundsException("Vector is empty");
    return GetNSetAt(dat, Size().Decrement());
  }

  @Override
  public void Swap(Natural n1, Natural n2) {
    Data temp = GetAt(n1);
    SetAt(GetAt(n2), n1);
    SetAt(temp, n2);
  }

  /* ************************************************************************ */
  /* Implementazione metodi Shift                                             */
  /* Utilizziamo GetAt/SetAt che gestiscono già la mappatura circolare        */
  /* ************************************************************************ */

  @Override
  public void ShiftLeft(Natural start, Natural n) {
    long s = start.ToLong();
    long d = n.ToLong();
    long limit = Size().ToLong();

    if (s + d >= limit) return;

    for (long i = s; i < limit - d; i++) {
        SetAt(GetAt(Natural.Of(i + d)), Natural.Of(i));
    }
    for (long i = limit - d; i < limit; i++) {
        SetAt(null, Natural.Of(i));
    }
  }

  @Override
  public void ShiftRight(Natural start, Natural n) {
    long s = start.ToLong();
    long d = n.ToLong();
    long limit = Size().ToLong();

    if (s + d >= limit) return;

    for (long i = limit - 1; i >= s + d; i--) {
        SetAt(GetAt(Natural.Of(i - d)), Natural.Of(i));
    }
    for (long i = s; i < s + d; i++) {
        SetAt(null, Natural.Of(i));
    }
  }

  // Alias richiesti dall'interfaccia
  @Override public void ShiftLeft(Natural n) { ShiftLeft(Natural.ZERO, n); }
  @Override public void ShiftFirstLeft() { ShiftLeft(Natural.ONE); }
  @Override public void ShiftLastLeft() { ShiftLeft(Size().Decrement(), Natural.ONE); } // Spesso un no-op pratico per shift(N-1, 1)

  @Override public void ShiftRight(Natural n) { ShiftRight(Natural.ZERO, n); }
  @Override public void ShiftFirstRight() { ShiftRight(Natural.ONE); }
  @Override public void ShiftLastRight() { ShiftRight(Size().Decrement(), Natural.ONE); }

  /* ************************************************************************ */
  /* Altri metodi (Grow, Shrink, Search, Traverse...)                         */
  /* ************************************************************************ */

  @Override
  public apsd.interfaces.containers.sequences.Vector<Data> SubVector(Natural start, Natural end) {
      // Nota: Ritorna un 'Vector' (lineare) o un 'CircularVector'? 
      // L'interfaccia Vector impone di tornare 'Vector' nel diagramma, ma potrebbe essere un tipo covariante.
      // Qui ritorniamo un CircularVector per coerenza di tipo, ma castato se necessario o adattato.
      // Se la firma richiede 'apsd.interfaces...Vector', CircularVector lo implementa.
      long len = end.ToLong() - start.ToLong();
      if (len < 0) throw new IllegalArgumentException("Invalid range");

      CircularVector<Data> sub = new CircularVector<>(Natural.Of(len));
      for(long i=0; i<len; i++) {
          sub.SetAt(GetAt(Natural.Of(start.ToLong() + i)), Natural.Of(i));
      }
      return sub; // CircularVector implements Vector interface via inheritance
  }
  
  @Override
  public boolean IsEmpty() {
    return Size().IsZero();
  }

  @Override
  public Data GetFirst() {
    if (IsEmpty()) throw new IndexOutOfBoundsException("Vector is empty");
    return GetAt(Natural.ZERO);
  }

  @Override
  public Data GetLast() {
    if (IsEmpty()) throw new IndexOutOfBoundsException("Vector is empty");
    return GetAt(Size().Decrement());
  }

  @Override
  public Natural Search(Data dat) {
      long sz = Size().ToLong();
      for (long i = 0; i < sz; i++) {
          Data val = GetAt(Natural.Of(i));
          if ((dat == null && val == null) || (dat != null && dat.equals(val))) {
              return Natural.Of(i);
          }
      }
      return null;
  }

  @Override
  public boolean IsInBound(Natural n) {
    return n.compareTo(Size()) < 0;
  }

  @Override
  public boolean IsEqual(IterableContainer<Data> container) {
    if (container == null) return false;
    if (Size().compareTo(container.Size()) != 0) return false;

    final long[] idx = {0};
    return !container.TraverseForward(new Predicate<Data>() {
        @Override
        public boolean Apply(Data otherDat) {
            Data myDat = GetAt(Natural.Of(idx[0]++));
            boolean areEqual = (myDat == null) ? otherDat == null : myDat.equals(otherDat);
            return !areEqual; 
        }
    }); 
  }

  @Override
  public boolean TraverseForward(Predicate<Data> predicate) {
      long size = Size().ToLong();
      for (long i = 0; i < size; i++) {
          if (predicate.Apply(GetAt(Natural.Of(i)))) return true;
      }
      return false;
  }

  @Override
  public boolean TraverseBackward(Predicate<Data> predicate) {
      long size = Size().ToLong();
      for (long i = size - 1; i >= 0; i--) {
          if (predicate.Apply(GetAt(Natural.Of(i)))) return true;
      }
      return false;
  }

  @Override
  public boolean Exists(Data dat) {
      return Search(dat) != null;
  }

  @Override
  public void Grow() {
	  
  }
	
  @Override
  public void Grow(Natural n) {
	  
  }
	
  @Override
  public void Shrink() {
	  
  }
}