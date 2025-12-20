package apsd.classes.containers.sequences;

import apsd.classes.containers.sequences.abstractbases.DynCircularVectorBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.traits.Predicate;

public class DynCircularVector<Data> extends DynCircularVectorBase<Data> {

  public DynCircularVector() { this(Natural.ZERO); }
  public DynCircularVector(Natural inisize) { Realloc(inisize); this.size = 0; }
  public DynCircularVector(TraversableContainer<Data> con) {
    Realloc(con.Size());
    con.TraverseForward(dat -> { InsertLast(dat); return false; });
  }
  protected DynCircularVector(Data[] arr) { NewVector(arr); this.size = (arr != null) ? arr.length : 0; }
  public static <Data> DynCircularVector<Data> Wrap(Data[] arr) { return new DynCircularVector<>(arr); }

  @Override
  public void Realloc(Natural newsize) {
      super.Realloc(newsize);
      long cap = newsize.ToLong();
      if (this.size > cap) this.size = cap;
  }

  @Override
  public void ShiftRight(Natural start, Natural n) {
      Expand(n);
      super.ShiftRight(start, n);
      // CLEANUP CRUCIALE
      long s = start.ToLong();
      long d = n.ToLong();
      for (long i = 0; i < d; i++) SetAt(null, Natural.Of(s + i));
  }

  @Override
  public void ShiftLeft(Natural start, Natural n) {
      super.ShiftLeft(start, n);
      // CLEANUP CRUCIALE
      long d = n.ToLong();
      long currentSize = size;
      for (long i = 0; i < d; i++) SetAt(null, Natural.Of(currentSize - 1 - i));
      Reduce(n);
  }

  @Override public void Expand() { Expand(Natural.ONE); }
  @Override public void Expand(Natural n) {
    long reqSize = size + n.ToLong();
    if (reqSize > Capacity().ToLong()) Realloc(Natural.Of(reqSize));
    size = reqSize;
  }
  @Override public void Grow() { Expand(); }
  @Override public void Grow(Natural n) { Expand(n); }
  @Override public void Reduce() { Reduce(Natural.ONE); }
  @Override public void Reduce(Natural n) {
    long dec = n.ToLong();
    if (dec > size) throw new IllegalArgumentException();
    size -= dec;
    Shrink();
  }
  @Override public void Shrink() {
    long cap = Capacity().ToLong();
    long sz = size;
    if (sz > 0 && cap >= (long)(sz * SHRINK_FACTOR * THRESHOLD_FACTOR)) {
      long newCap = (long)(cap / SHRINK_FACTOR);
      if (newCap < sz) newCap = sz;
      Realloc(Natural.Of(newCap));
    }
  }

  @Override public void InsertAt(Data dat, Natural pos) {
    if (pos.compareTo(Size()) > 0) throw new IndexOutOfBoundsException();
    if (pos.compareTo(Size()) <= 0) ShiftRight(pos, Natural.ONE);
    SetAt(dat, pos);
  }
  @Override public Data AtNRemove(Natural pos) {
    if (pos.compareTo(Size()) >= 0) throw new IndexOutOfBoundsException();
    Data removed = GetAt(pos);
    ShiftLeft(pos, Natural.ONE);
    return removed;
  }

  @Override public void ShiftLeft(Natural n) { ShiftLeft(Natural.ZERO, n); }
  @Override public void ShiftFirstLeft() { ShiftLeft(Natural.ONE); }
  @Override public void ShiftLastLeft() { ShiftLeft(Size().Decrement(), Natural.ONE); }
  @Override public void ShiftRight(Natural n) { ShiftRight(Natural.ZERO, n); }
  @Override public void ShiftFirstRight() { ShiftRight(Natural.ONE); }
  @Override public void ShiftLastRight() { ShiftRight(Size().Decrement(), Natural.ONE); }

  @Override public boolean IsEmpty() { return size == 0; }
  @Override public Data GetFirst() { if (IsEmpty()) throw new IndexOutOfBoundsException(); return GetAt(Natural.ZERO); }
  @Override public Data GetLast() { if (IsEmpty()) throw new IndexOutOfBoundsException(); return GetAt(Size().Decrement()); }
  @Override public void InsertFirst(Data dat) { InsertAt(dat, Natural.ZERO); }
  @Override public void InsertLast(Data dat) { InsertAt(dat, Size()); }
  @Override public void RemoveFirst() { if(!IsEmpty()) AtNRemove(Natural.ZERO); } // SAFE CHECK
  @Override public Data FirstNRemove() { return (!IsEmpty()) ? AtNRemove(Natural.ZERO) : null; }
  @Override public void RemoveLast() { if(!IsEmpty()) AtNRemove(Size().Decrement()); } // SAFE CHECK
  @Override public Data LastNRemove() { return (!IsEmpty()) ? AtNRemove(Size().Decrement()) : null; }
  @Override public void RemoveAt(Natural n) { AtNRemove(n); }
  @Override public Data GetNSetAt(Data dat, Natural n) { Data old = GetAt(n); SetAt(dat, n); return old; }
  @Override public void SetFirst(Data dat) { if (IsEmpty()) throw new IndexOutOfBoundsException(); SetAt(dat, Natural.ZERO); }
  @Override public Data GetNSetFirst(Data dat) { if (IsEmpty()) throw new IndexOutOfBoundsException(); return GetNSetAt(dat, Natural.ZERO); }
  @Override public void SetLast(Data dat) { if (IsEmpty()) throw new IndexOutOfBoundsException(); SetAt(dat, Size().Decrement()); }
  @Override public Data GetNSetLast(Data dat) { if (IsEmpty()) throw new IndexOutOfBoundsException(); return GetNSetAt(dat, Size().Decrement()); }
  @Override public void Swap(Natural pos1, Natural pos2) { Data tmp = GetAt(pos1); SetAt(GetAt(pos2), pos1); SetAt(tmp, pos2); }

  @Override public Natural Search(Data dat) {
    long sz = Size().ToLong();
    for (long i = 0; i < sz; i++) {
      Data val = GetAt(Natural.Of(i));
      if ((dat == null && val == null) || (dat != null && dat.equals(val))) return Natural.Of(i);
    }
    return null;
  }
  @Override public boolean Exists(Data dat) { return Search(dat) != null; }
  @Override public boolean IsInBound(Natural n) { return n.compareTo(Size()) < 0; }
  @Override public boolean TraverseForward(Predicate<Data> predicate) {
    long sz = Size().ToLong();
    for (long i = 0; i < sz; i++) if (predicate.Apply(GetAt(Natural.Of(i)))) return true;
    return false;
  }
  @Override public boolean TraverseBackward(Predicate<Data> predicate) {
    long sz = Size().ToLong();
    for (long i = sz - 1; i >= 0; i--) if (predicate.Apply(GetAt(Natural.Of(i)))) return true;
    return false;
  }
  @Override public boolean IsEqual(IterableContainer<Data> container) {
    if (container == null || Size().compareTo(container.Size()) != 0) return false;
    final long[] idx = {0};
    return !container.TraverseForward(other -> {
      Data mine = GetAt(Natural.Of(idx[0]++));
      return !((mine == null) ? other == null : mine.equals(other));
    });
  }
  @Override public DynVector<Data> SubVector(Natural start, Natural end) {
    long len = end.ToLong() - start.ToLong();
    if (len < 0) throw new IllegalArgumentException();
    DynVector<Data> sub = new DynVector<>(Natural.Of(len));
    for (long i = 0; i < len; i++) sub.InsertLast(GetAt(Natural.Of(start.ToLong() + i)));
    return sub;
  }
}