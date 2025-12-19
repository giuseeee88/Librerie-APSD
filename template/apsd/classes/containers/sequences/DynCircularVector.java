package apsd.classes.containers.sequences;

import apsd.classes.containers.sequences.abstractbases.DynCircularVectorBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.traits.Predicate;

public class DynCircularVector<Data> extends DynCircularVectorBase<Data> {

    public DynCircularVector() {
        this(Natural.ZERO);
    }

    public DynCircularVector(Natural inisize) {
        Realloc(inisize);
        this.size = inisize.ToLong();
    }

    public DynCircularVector(TraversableContainer<Data> con) {
        this(con.Size());
        con.TraverseForward(new Predicate<Data>() {
            long idx = 0;
            @Override
            public boolean Apply(Data dat) {
                // Scrittura diretta per inizializzazione
                rawSet(dat, idx++);
                return false;
            }
        });
    }

    protected DynCircularVector(Data[] arr) {
        NewVector(arr);
        this.size = (arr != null) ? arr.length : 0;
    }

    public static <Data> DynCircularVector<Data> Wrap(Data[] arr) {
        return new DynCircularVector<>(arr);
    }

    // Helper: Accesso fisico diretto (ignora Size, rispetta Capacity)
    private void rawSet(Data dat, long logicalIdx) {
        long cap = Capacity().ToLong();
        if (cap == 0) return;
        long physicalIdx = (start + logicalIdx) % cap;
        arr[(int) physicalIdx] = dat;
    }

    private Data rawGet(long logicalIdx) {
        long cap = Capacity().ToLong();
        if (cap == 0) throw new IndexOutOfBoundsException("Capacity 0");
        long physicalIdx = (start + logicalIdx) % cap;
        return arr[(int) physicalIdx];
    }

    @Override
    public void InsertAt(Data dat, Natural pos) {
        long idx = pos.ToLong();
        // idx == size è valido per append
        if (idx < 0 || idx > size) throw new IndexOutOfBoundsException("Index: " + idx + ", Size: " + size);

        if (size >= Capacity().ToLong()) {
            Grow();
        }

        // Shift a destra usando rawSet per poter scrivere in posizioni >= size
        for (long i = size; i > idx; i--) {
            rawSet(rawGet(i - 1), i);
        }

        rawSet(dat, idx);
        size++;
    }

    @Override
    public Data AtNRemove(Natural pos) {
        long idx = pos.ToLong();
        if (idx < 0 || idx >= size) throw new IndexOutOfBoundsException("Index: " + idx + ", Size: " + size);

        Data removed = rawGet(idx);

        // Shift a sinistra
        for (long i = idx; i < size - 1; i++) {
            rawSet(rawGet(i + 1), i);
        }

        size--;
        rawSet(null, size); // Clean up
        return removed;
    }

    @Override
    public void Expand(Natural n) {
        long newSize = size + n.ToLong();
        if (newSize > Capacity().ToLong()) {
            Realloc(Natural.Of(newSize));
        }
        size = newSize;
    }

    @Override
    public void Grow() {
        long cap = Capacity().ToLong();
        long newCap = (cap == 0) ? 1 : (long)(cap * GROW_FACTOR);
        Realloc(Natural.Of(newCap));
    }

    @Override
    public void Grow(Natural n) {
        long reqCap = Capacity().ToLong() + n.ToLong();
        Realloc(Natural.Of(reqCap));
    }

    @Override
    public void Shrink() {
        long cap = Capacity().ToLong();
        if (size > 0 && cap > size * THRESHOLD_FACTOR * SHRINK_FACTOR) {
             long newCap = (long)(cap / SHRINK_FACTOR);
             if (newCap < size) newCap = size;
             Realloc(Natural.Of(newCap));
        }
    }

    @Override
    public void Reduce(Natural n) {
        long dec = n.ToLong();
        if (dec > size) throw new IllegalArgumentException("Reduce > size");
        for (long i = size - dec; i < size; i++) {
            rawSet(null, i);
        }
        size -= dec;
    }

    // Alias
    @Override public void Expand() { Expand(Natural.ONE); }
    @Override public void Reduce() { Reduce(Natural.ONE); }
    
    @Override public boolean IsEmpty() { return size == 0; }
    
    @Override public Data GetFirst() { 
        if(size == 0) throw new IndexOutOfBoundsException("Vector is empty"); 
        return GetAt(Natural.ZERO); 
    }
    @Override public Data GetLast() { 
        if(size == 0) throw new IndexOutOfBoundsException("Vector is empty"); 
        return GetAt(Natural.Of(size - 1)); 
    }

    @Override
    public Natural Search(Data dat) {
        for(long i=0; i<size; i++) {
            Data val = rawGet(i);
            if ((dat==null && val==null) || (dat!=null && dat.equals(val))) return Natural.Of(i);
        }
        return null;
    }
    
    @Override public boolean Exists(Data dat) { return Search(dat) != null; }
    @Override public boolean IsInBound(Natural n) { return n.ToLong() < size; }

    @Override
    public boolean IsEqual(IterableContainer<Data> container) {
        if(container == null || Size().compareTo(container.Size())!=0) return false;
        final long[] i = {0};
        return !container.TraverseForward(dat -> {
            Data my = rawGet(i[0]++);
            return !((my==null)?dat==null : my.equals(dat));
        });
    }

    @Override
    public boolean TraverseForward(Predicate<Data> predicate) {
        for(long i=0; i<size; i++) {
            if(predicate.Apply(rawGet(i))) return true;
        }
        return false;
    }

    @Override
    public boolean TraverseBackward(Predicate<Data> predicate) {
        for(long i=size-1; i>=0; i--) {
            if(predicate.Apply(rawGet(i))) return true;
        }
        return false;
    }

    @Override public Data GetNSetAt(Data dat, Natural n) { Data old = GetAt(n); SetAt(dat, n); return old; }
    @Override public void SetFirst(Data dat) { if(size==0) throw new IndexOutOfBoundsException(); SetAt(dat, Natural.ZERO); }
    @Override public Data GetNSetFirst(Data dat) { if(size==0) throw new IndexOutOfBoundsException(); return GetNSetAt(dat, Natural.ZERO); }
    @Override public void SetLast(Data dat) { if(size==0) throw new IndexOutOfBoundsException(); SetAt(dat, Natural.Of(size-1)); }
    @Override public Data GetNSetLast(Data dat) { if(size==0) throw new IndexOutOfBoundsException(); return GetNSetAt(dat, Natural.Of(size-1)); }
    @Override public void Swap(Natural pos1, Natural pos2) { Data tmp = GetAt(pos1); SetAt(GetAt(pos2), pos1); SetAt(tmp, pos2); }

    @Override public void Clear() {
        super.Clear(); 
        size = 0;
        start = 0;
    }

    @Override public void InsertFirst(Data dat) { InsertAt(dat, Natural.ZERO); }
    @Override public void InsertLast(Data dat) { InsertAt(dat, Natural.Of(size)); }
    @Override public void RemoveFirst() { AtNRemove(Natural.ZERO); }
    @Override public void RemoveLast() { AtNRemove(Natural.Of(size - 1)); }
    @Override public Data FirstNRemove() { return AtNRemove(Natural.ZERO); }
    @Override public Data LastNRemove() { return AtNRemove(Natural.Of(size - 1)); }
    @Override public void RemoveAt(Natural n) { AtNRemove(n); }

    @Override public void ShiftLeft(Natural start, Natural n) {
        long s = start.ToLong(); long d = n.ToLong();
        if(s+d >= size) return;
        for(long i=s; i < size-d; i++) rawSet(rawGet(i+d), i);
        for(long i=size-d; i < size; i++) rawSet(null, i);
    }
    
    @Override public void ShiftRight(Natural start, Natural n) {
        long s = start.ToLong(); long d = n.ToLong();
        if(s+d >= size) return;
        for(long i=size-1; i >= s+d; i--) rawSet(rawGet(i-d), i);
        for(long i=s; i < s+d; i++) rawSet(null, i);
    }
    
    @Override public void ShiftLeft(Natural n) { ShiftLeft(Natural.ZERO, n); }
    @Override public void ShiftFirstLeft() { ShiftLeft(Natural.ONE); }
    @Override public void ShiftLastLeft() { ShiftLeft(Natural.Of(size-1), Natural.ONE); }
    @Override public void ShiftRight(Natural n) { ShiftRight(Natural.ZERO, n); }
    @Override public void ShiftFirstRight() { ShiftRight(Natural.ONE); }
    @Override public void ShiftLastRight() { ShiftRight(Natural.Of(size-1), Natural.ONE); }

    @Override
    public apsd.interfaces.containers.sequences.DynVector<Data> SubVector(Natural start, Natural end) {
        long len = end.ToLong() - start.ToLong();
        if (len < 0) throw new IllegalArgumentException("Invalid range");
        DynCircularVector<Data> sub = new DynCircularVector<>(Natural.Of(len));
        for(long i=0; i<len; i++) sub.rawSet(rawGet(start.ToLong()+i), i);
        return sub;
    }
}