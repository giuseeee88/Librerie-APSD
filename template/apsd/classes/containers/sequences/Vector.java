package apsd.classes.containers.sequences;

import apsd.classes.containers.sequences.abstractbases.LinearVectorBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.traits.Predicate;

/** Object: Concrete (static linear) vector implementation. */
public class Vector<Data> extends LinearVectorBase<Data> {

  public Vector() {
    this(Natural.ZERO);
  }

  public Vector(Natural inisize) {
    ArrayAlloc(inisize);
  }

  public Vector(TraversableContainer<Data> con) {
    ArrayAlloc(con.Size());
    con.TraverseForward(new Predicate<Data>() {
      long idx = 0;
      @Override
      public boolean Apply(Data dat) {
        SetAt(dat, Natural.Of(idx++));
        return false; // Continua l'attraversamento
      }
    });
  }

  protected Vector(Data[] arr) {
    NewVector(arr);
  }

  public static <Data> Vector<Data> Wrap(Data[] arr) {
    return new Vector<>(arr);
  }

  /* ************************************************************************ */
  /* Implementazione metodi MutableSequence e Size                            */
  /* ************************************************************************ */

  @Override
  public Natural Size() {
    return Capacity();
  }

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
  /* Implementazione metodi Shift (Spostamento elementi)                      */
  /* ************************************************************************ */

  @Override
  public void ShiftLeft(Natural n) {
    ShiftLeft(Natural.ZERO, n);
  }

  @Override
  public void ShiftLeft(Natural start, Natural n) {
    // Sposta gli elementi a sinistra di 'n' posizioni a partire da 'start'
    // Gli elementi spostati sovrascrivono i precedenti.
    long s = start.ToLong();
    long d = n.ToLong();
    long size = Size().ToLong();
    
    if (s + d >= size) return; // Nulla da spostare o out of bound gestito logicamente

    for (long i = s; i < size - d; i++) {
        SetAt(GetAt(Natural.Of(i + d)), Natural.Of(i));
    }
    // Opzionale: annullare gli elementi in coda rimasti "doppi"
    for (long i = size - d; i < size; i++) {
        SetAt(null, Natural.Of(i));
    }
  }

  @Override
  public void ShiftFirstLeft() {
    ShiftLeft(Natural.ONE);
  }

  @Override
  public void ShiftLastLeft() {
      // ShiftLastLeft su un vettore significa tecnicamente spostare l'ultimo elemento...
      // Ma spesso è inteso come uno shift che coinvolge solo la parte finale o è un no-op se inteso come rotazione
      // Assumendo sia uno shift a sinistra di 1 posizione partendo dalla fine (poco utile) o simile.
      // Basandoci sul diagramma standard, spesso è un alias o un caso particolare.
      // Se non specificato diversamente, shiftiamo tutto di 1.
      ShiftLeft(Natural.ONE); 
  }

  @Override
  public void ShiftRight(Natural n) {
      ShiftRight(Natural.ZERO, n);
  }

  @Override
  public void ShiftRight(Natural start, Natural n) {
    long s = start.ToLong();
    long d = n.ToLong();
    long size = Size().ToLong();

    if (s + d >= size) return;

    // Itera all'indietro per evitare sovrascritture
    for (long i = size - 1; i >= s + d; i--) {
        SetAt(GetAt(Natural.Of(i - d)), Natural.Of(i));
    }
    // Opzionale: annullare gli elementi in testa (range start...start+n)
    for (long i = s; i < s + d; i++) {
        SetAt(null, Natural.Of(i));
    }
  }

  @Override
  public void ShiftFirstRight() {
      ShiftRight(Natural.ONE);
  }

  @Override
  public void ShiftLastRight() {
      ShiftRight(Natural.ONE);
  }

  /* ************************************************************************ */
  /* Altri metodi concreti                                                    */
  /* ************************************************************************ */

  @Override
  public apsd.interfaces.containers.sequences.Vector<Data> SubVector(Natural start, Natural end) {
    long len = end.ToLong() - start.ToLong();
    if (len < 0) throw new IllegalArgumentException("End index must be greater or equal to start index");
    
    Vector<Data> sub = new Vector<>(Natural.Of(len));
    for (long i = 0; i < len; i++) {
        sub.SetAt(GetAt(Natural.Of(start.ToLong() + i)), Natural.Of(i));
    }
    return sub;
  }

  @Override
  public void Grow() {
    // Vettore statico: non supporta ridimensionamento dinamico automatico
    // Lasciare vuoto o lanciare UnsupportedOperationException
  }

  @Override
  public void Grow(Natural n) {
    // Vettore statico: non supporta ridimensionamento dinamico automatico
  }

  @Override
  public void Shrink() {
    // Vettore statico: non supporta ridimensionamento dinamico automatico
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
      // Scansione lineare
      long size = Size().ToLong();
      for (long i = 0; i < size; i++) {
          Data val = GetAt(Natural.Of(i));
          if ((dat == null && val == null) || (dat != null && dat.equals(val))) {
              return Natural.Of(i);
          }
      }
      return null; // O indicatore di non trovato
  }

  @Override
  public boolean IsInBound(Natural n) {
    return n.compareTo(Size()) < 0;
  }

  @Override
  public boolean IsEqual(IterableContainer<Data> container) {
    if (container == null) return false;
    if (Size().compareTo(container.Size()) != 0) return false;

    // Confronto elemento per elemento usando l'iteratore dell'altro container
    final long[] idx = {0};
    return !container.TraverseForward(new Predicate<Data>() {
        @Override
        public boolean Apply(Data otherDat) {
            Data myDat = GetAt(Natural.Of(idx[0]++));
            // Se diversi, interrompiamo ritornando true (trovato un mismatch)
            boolean areEqual = (myDat == null) ? otherDat == null : myDat.equals(otherDat);
            return !areEqual; 
        }
    }); 
    // Nota: TraverseForward ritorna true se il predicato ritorna true (cioè se ha trovato diversità)
    // Quindi IsEqual è !TraverseForward
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
}