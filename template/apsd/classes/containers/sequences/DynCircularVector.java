package apsd.classes.containers.sequences;

import apsd.classes.containers.sequences.abstractbases.DynCircularVectorBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.traits.Predicate;

/** Object: Concrete dynamic circular vector implementation. */
public class DynCircularVector<Data> extends DynCircularVectorBase<Data> {

  public DynCircularVector() {
    this(Natural.ZERO);
  }

  public DynCircularVector(Natural inisize) {
    Realloc(inisize); 
    this.size = 0; 
  }

  public DynCircularVector(TraversableContainer<Data> con) {
    Realloc(con.Size());
    con.TraverseForward(dat -> { InsertLast(dat); return false; });
  }

  protected DynCircularVector(Data[] arr) {
    NewVector(arr);
    this.size = (arr != null) ? arr.length : 0;
  }

  public static <Data> DynCircularVector<Data> Wrap(Data[] arr) {
    return new DynCircularVector<>(arr);
  }

  /* ************************************************************************ */
  /* Metodi Core: Shift con gestione Size (Per soddisfare i Test)             */
  /* ************************************************************************ */

  @Override
  public void ShiftRight(Natural start, Natural n) {
      // 1. Expand (se necessario)
      Expand(n);
      
      // 2. Delega lo shift fisico alla base (che usa l'ottimizzazione testa/coda)
      // La base lavora sui dati raw e su 'start' fisico.
      super.ShiftRight(start, n);
      
      // 3. Incrementa Size
      size += n.ToLong();
  }

  @Override
  public void ShiftLeft(Natural start, Natural n) {
      // 1. Delega lo shift fisico alla base
      super.ShiftLeft(start, n);
      
      // 2. Decrementa Size
      size -= n.ToLong();
      
      // 3. Reduce (se necessario)
      Reduce();
  }

  /* ************************************************************************ */
  /* Metodi Strutturali: Insert/Remove semplificati                           */
  /* ************************************************************************ */

  @Override
  public void InsertAt(Data dat, Natural pos) {
    if (pos.compareTo(Size()) > 0) throw new IndexOutOfBoundsException("Insert index out of bounds: " + pos);

    // ShiftRight ora gestisce Expand e size++
    // Nota: Se pos == size (InsertLast), ShiftRight sposterà la "coda" (che è vuota) 
    // oppure la testa, creando semplicemente lo spazio logico.
    
    if (pos.compareTo(Size()) <= 0) {
        ShiftRight(pos, Natural.ONE);
    }
    
    // Scrivi il dato
    SetAt(dat, pos);
  }

  @Override
  public Data AtNRemove(Natural pos) {
    if (pos.compareTo(Size()) >= 0) throw new IndexOutOfBoundsException("Remove index out of bounds: " + pos);

    Data removed = GetAt(pos);

    // ShiftLeft ora gestisce size-- e Reduce
    ShiftLeft(pos, Natural.ONE);

    return removed;
  }

  /* ************************************************************************ */
  /* Alias Shift                                                              */
  /* ************************************************************************ */

  @Override public void ShiftLeft(Natural n) { ShiftLeft(Natural.ZERO, n); }
  @Override public void ShiftFirstLeft() { ShiftLeft(Natural.ONE); }
  @Override public void ShiftLastLeft() { ShiftLeft(Size().Decrement(), Natural.ONE); }
  @Override public void ShiftRight(Natural n) { ShiftRight(Natural.ZERO, n); }
  @Override public void ShiftFirstRight() { ShiftRight(Natural.ONE); }
  @Override public void ShiftLastRight() { ShiftRight(Size().Decrement(), Natural.ONE); }

  /* ************************************************************************ */
  /* Metodi Resize (Implementazione Completa)                                 */
  /* ************************************************************************ */

  @Override
  public void Expand() {
    long cap = Capacity().ToLong();
    long newCap = (cap == 0) ? 1 : (long)(cap * GROW_FACTOR);
    Realloc(Natural.Of(newCap));
  }

  @Override
  public void Expand(Natural n) {
    long reqSize = size + n.ToLong();
    if (reqSize > Capacity().ToLong()) {
       Realloc(Natural.Of(reqSize));
    }
  }

  @Override public void Grow() { Expand(); }
  @Override public void Grow(Natural n) { Expand(n); }

  @Override
  public void Shrink() {
    long cap = Capacity().ToLong();
    long sz = size;
    if (sz > 0 && cap >= (long)(sz * SHRINK_FACTOR * THRESHOLD_FACTOR)) {
      long newCap = (long)(cap / SHRINK_FACTOR);
      if (newCap < sz) newCap = sz;
      Realloc(Natural.Of(newCap));
    }
  }

  @Override public void Reduce() { Shrink(); }

  /* ************************************************************************ */
  /* Metodi Accessori e Utility                                               */
  /* ************************************************************************ */

  @Override public boolean IsEmpty() { return size == 0; }
  @Override public Data GetFirst() { if (IsEmpty()) throw new IndexOutOfBoundsException(); return GetAt(Natural.ZERO); }
  @Override public Data GetLast() { if (IsEmpty()) throw new IndexOutOfBoundsException(); return GetAt(Size().Decrement()); }
  @Override public void InsertFirst(Data dat) { InsertAt(dat, Natural.ZERO); }
  @Override public void InsertLast(Data dat) { InsertAt(dat, Size()); }
  @Override public void RemoveFirst() { AtNRemove(Natural.ZERO); }
  @Override public Data FirstNRemove() { return AtNRemove(Natural.ZERO); }
  @Override public void RemoveLast() { AtNRemove(Size().Decrement()); }
  @Override public Data LastNRemove() { return AtNRemove(Size().Decrement()); }
  @Override public void RemoveAt(Natural n) { AtNRemove(n); }
  @Override public Data GetNSetAt(Data dat, Natural n) { Data old = GetAt(n); SetAt(dat, n); return old; }
  @Override public void SetFirst(Data dat) { if (IsEmpty()) throw new IndexOutOfBoundsException(); SetAt(dat, Natural.ZERO); }
  @Override public Data GetNSetFirst(Data dat) { if (IsEmpty()) throw new IndexOutOfBoundsException(); return GetNSetAt(dat, Natural.ZERO); }
  @Override public void SetLast(Data dat) { if (IsEmpty()) throw new IndexOutOfBoundsException(); SetAt(dat, Size().Decrement()); }
  @Override public Data GetNSetLast(Data dat) { if (IsEmpty()) throw new IndexOutOfBoundsException(); return GetNSetAt(dat, Size().Decrement()); }
  @Override public void Swap(Natural pos1, Natural pos2) { Data tmp = GetAt(pos1); SetAt(GetAt(pos2), pos1); SetAt(tmp, pos2); }

  @Override
  public Natural Search(Data dat) {
    long sz = Size().ToLong();
    for (long i = 0; i < sz; i++) {
      Data val = GetAt(Natural.Of(i));
      if ((dat == null && val == null) || (dat != null && dat.equals(val))) return Natural.Of(i);
    }
    return null;
  }
  @Override public boolean Exists(Data dat) { return Search(dat) != null; }
  @Override public boolean IsInBound(Natural n) { return n.compareTo(Size()) < 0; }
  
  @Override
  public boolean TraverseForward(Predicate<Data> predicate) {
    long sz = Size().ToLong();
    for (long i = 0; i < sz; i++) if (predicate.Apply(GetAt(Natural.Of(i)))) return true;
    return false;
  }
  @Override
  public boolean TraverseBackward(Predicate<Data> predicate) {
    long sz = Size().ToLong();
    for (long i = sz - 1; i >= 0; i--) if (predicate.Apply(GetAt(Natural.Of(i)))) return true;
    return false;
  }
  @Override
  public boolean IsEqual(IterableContainer<Data> container) {
    if (container == null || Size().compareTo(container.Size()) != 0) return false;
    final long[] idx = {0};
    return !container.TraverseForward(other -> {
      Data mine = GetAt(Natural.Of(idx[0]++));
      return !((mine == null) ? other == null : mine.equals(other));
    });
  }
  
  @Override
  public apsd.interfaces.containers.sequences.DynVector<Data> SubVector(Natural start, Natural end) {
    long len = end.ToLong() - start.ToLong();
    if (len < 0) throw new IllegalArgumentException();
    DynCircularVector<Data> sub = new DynCircularVector<>(Natural.Of(len));
    for (long i = 0; i < len; i++) sub.InsertLast(GetAt(Natural.Of(start.ToLong() + i)));
    return sub;
  }
}