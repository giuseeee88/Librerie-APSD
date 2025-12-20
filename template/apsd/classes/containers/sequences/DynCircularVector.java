package apsd.classes.containers.sequences;

import apsd.classes.containers.sequences.abstractbases.DynCircularVectorBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.traits.Predicate;

/**
 * Object: Concrete dynamic circular vector implementation.
 * Questa classe gestisce le operazioni di alto livello (Insert/Remove) delegando
 * la logica posizionale e di memoria alla classe base ottimizzata.
 */
public class DynCircularVector<Data> extends DynCircularVectorBase<Data> {

  /* ************************************************************************ */
  /* Costruttori                                                              */
  /* ************************************************************************ */

  public DynCircularVector() {
    this(Natural.ZERO);
  }

  public DynCircularVector(Natural inisize) {
    // Realloc in DynCircularVectorBase imposta capacity e linearizza start a 0
    Realloc(inisize); 
    // Inizialmente size è 0.
    this.size = 0; 
  }

  public DynCircularVector(TraversableContainer<Data> con) {
    // Pre-allochiamo la dimensione esatta per efficienza
    Realloc(con.Size());
    con.TraverseForward(new Predicate<Data>() {
      @Override
      public boolean Apply(Data dat) {
        InsertLast(dat);
        return false; // Continua l'attraversamento
      }
    });
  }

  protected DynCircularVector(Data[] arr) {
    NewVector(arr);
    this.size = (arr != null) ? arr.length : 0;
    // CircularVectorBase assume start=0 di default
  }

  public static <Data> DynCircularVector<Data> Wrap(Data[] arr) {
    return new DynCircularVector<>(arr);
  }

  /* ************************************************************************ */
  /* Metodi di Modifica Strutturale (Core Logic)                              */
  /* ************************************************************************ */

  @Override
  public void InsertAt(Data dat, Natural pos) {
    if (pos.compareTo(Size()) > 0) throw new IndexOutOfBoundsException("Insert index out of bounds: " + pos);

    // 1. Expand se necessario (se size ha raggiunto la capacity)
    if (size >= Capacity().ToLong()) {
      Expand();
    }

    // 2. Crea lo spazio solo se non stiamo inserendo in coda (Append)
    // ShiftRight della base gestisce l'ottimizzazione (sposta testa o coda)
    if (pos.compareTo(Size()) < 0) {
      ShiftRight(pos, Natural.ONE);
    }

    // 3. Scrivi il dato nella posizione (ora libera o nuova)
    SetAt(dat, pos);

    // 4. Aggiorna la dimensione logica
    size++;
  }

  @Override
  public Data AtNRemove(Natural pos) {
    if (pos.compareTo(Size()) >= 0) throw new IndexOutOfBoundsException("Remove index out of bounds: " + pos);

    // 1. Recupera il dato da rimuovere
    Data removed = GetAt(pos);

    // 2. Chiudi il buco (ShiftLeft della base gestisce l'ottimizzazione)
    ShiftLeft(pos, Natural.ONE);

    // 3. Aggiorna la dimensione logica
    size--;

    // 4. Shrink opzionale (riduce la memoria se troppo vuota)
    // Shrink(); 

    return removed;
  }

  /* ************************************************************************ */
  /* Metodi Accessori e Utility (Shortcuts)                                   */
  /* ************************************************************************ */

  @Override
  public boolean IsEmpty() {
    return size == 0;
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
  public void InsertFirst(Data dat) {
    InsertAt(dat, Natural.ZERO);
  }

  @Override
  public void InsertLast(Data dat) {
    InsertAt(dat, Size());
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

  @Override
  public void RemoveAt(Natural n) {
    AtNRemove(n);
  }

  /* ************************************************************************ */
  /* Metodi MutableSequence (GetNSet, Swap)                                   */
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
  public void Swap(Natural pos1, Natural pos2) {
    Data tmp = GetAt(pos1);
    SetAt(GetAt(pos2), pos1);
    SetAt(tmp, pos2);
  }

  /* ************************************************************************ */
  /* Metodi Shift (Alias ai metodi Base)                                      */
  /* ************************************************************************ */

  @Override
  public void ShiftLeft(Natural n) {
    ShiftLeft(Natural.ZERO, n);
  }

  @Override
  public void ShiftFirstLeft() {
    ShiftLeft(Natural.ONE);
  }

  @Override
  public void ShiftLastLeft() {
    ShiftLeft(Size().Decrement(), Natural.ONE);
  }

  @Override
  public void ShiftRight(Natural n) {
    ShiftRight(Natural.ZERO, n);
  }

  @Override
  public void ShiftFirstRight() {
    ShiftRight(Natural.ONE);
  }

  @Override
  public void ShiftLastRight() {
    ShiftRight(Size().Decrement(), Natural.ONE);
  }

  /* ************************************************************************ */
  /* Implementazione Container / Traversable / Search                         */
  /* ************************************************************************ */

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
  public boolean Exists(Data dat) {
    return Search(dat) != null;
  }

  @Override
  public boolean IsInBound(Natural n) {
    return n.compareTo(Size()) < 0;
  }

  @Override
  public boolean TraverseForward(Predicate<Data> predicate) {
    long sz = Size().ToLong();
    for (long i = 0; i < sz; i++) {
      if (predicate.Apply(GetAt(Natural.Of(i)))) return true;
    }
    return false;
  }

  @Override
  public boolean TraverseBackward(Predicate<Data> predicate) {
    long sz = Size().ToLong();
    for (long i = sz - 1; i >= 0; i--) {
      if (predicate.Apply(GetAt(Natural.Of(i)))) return true;
    }
    return false;
  }

  @Override
  public boolean IsEqual(IterableContainer<Data> container) {
    if (container == null) return false;
    if (Size().compareTo(container.Size()) != 0) return false;

    final long[] idx = {0};
    return !container.TraverseForward(new Predicate<Data>() {
      @Override
      public boolean Apply(Data other) {
        Data mine = GetAt(Natural.Of(idx[0]++));
        boolean eq = (mine == null) ? other == null : mine.equals(other);
        return !eq;
      }
    });
  }

  @Override
  public apsd.interfaces.containers.sequences.DynVector<Data> SubVector(Natural start, Natural end) {
    long len = end.ToLong() - start.ToLong();
    if (len < 0) throw new IllegalArgumentException("Invalid range: end < start");

    DynCircularVector<Data> sub = new DynCircularVector<>(Natural.Of(len));
    for (long i = 0; i < len; i++) {
      sub.InsertLast(GetAt(Natural.Of(start.ToLong() + i)));
    }
    return sub;
  }

  /* ************************************************************************ */
  /* Metodi Resize (Expand/Grow/Shrink) - Implementati Completi               */
  /* ************************************************************************ */

  /**
   * Espande il vettore secondo il GROW_FACTOR (default 2.0).
   * Viene chiamato automaticamente quando InsertAt rileva che il vettore è pieno.
   */
  @Override
  public void Expand() {
    long cap = Capacity().ToLong();
    // Se capacity è 0, inizializza a 1, altrimenti raddoppia.
    long newCap = (cap == 0) ? 1 : (long)(cap * GROW_FACTOR);
    Realloc(Natural.Of(newCap));
  }

  /**
   * Espande il vettore per garantire spazio per 'n' elementi AGGIUNTIVI.
   * Override per assicurarsi che 'size' sia considerato correttamente.
   */
  @Override
  public void Expand(Natural n) {
    long reqSize = size + n.ToLong();
    if (reqSize > Capacity().ToLong()) {
       // Se la dimensione richiesta supera la capacità, rialloca
       Realloc(Natural.Of(reqSize));
    }
  }

  @Override
  public void Grow() {
    Expand();
  }

  @Override
  public void Grow(Natural n) {
    Expand(n);
  }

  @Override
  public void Shrink() {
    long cap = Capacity().ToLong();
    long sz = size;
    // Se la size è inferiore a Capacity / (SHRINK * THRESHOLD), riduciamo
    // Esempio: size=10, cap=100. Riduce a cap=50 (o 25 a seconda del fattore).
    if (sz > 0 && cap >= (long)(sz * SHRINK_FACTOR * THRESHOLD_FACTOR)) {
      long newCap = (long)(cap / SHRINK_FACTOR);
      if (newCap < sz) newCap = sz; // Safety: non scendere sotto la size attuale
      Realloc(Natural.Of(newCap));
    }
  }

  @Override
  public void Reduce() {
    Shrink();
  }
}