package apsd.classes.containers.sequences;

import apsd.classes.containers.sequences.abstractbases.DynLinearVectorBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.traits.Predicate;

/** Object: Concrete dynamic (linear) vector implementation. */
public class DynVector<Data> extends DynLinearVectorBase<Data> {

    // Costruttori
    public DynVector() { this(Natural.ZERO); }

    public DynVector(Natural inisize) {
        Realloc(inisize);
        this.size = 0; // Inizia vuoto
    }

    public DynVector(TraversableContainer<Data> con) {
        Realloc(con.Size());
        con.TraverseForward(dat -> { InsertLast(dat); return false; });
    }

    protected DynVector(Data[] arr) {
        NewVector(arr);
        this.size = (arr != null) ? arr.length : 0;
    }

    public static <Data> DynVector<Data> Wrap(Data[] arr) {
        return new DynVector<>(arr);
    }

    /* ************************************************************************ */
    /* Metodi di Modifica Strutturale (Adattati ai Test)                        */
    /* ************************************************************************ */

    /**
     * ShiftRight ora deve:
     * 1. Espandere il vettore se necessario.
     * 2. Spostare gli elementi.
     * 3. Incrementare la size.
     */
    @Override
    public void ShiftRight(Natural start, Natural n) {
        // 1. Expand preventivo
        Expand(n);
        
        // 2. Shift fisico (implementazione base/default)
        // Nota: chiamiamo super.ShiftRight o usiamo logica manuale se la base non lo supporta.
        // Poiché VectorBase::ShiftRight usa GetAt/SetAt basati su size, e noi stiamo aumentando size,
        // dobbiamo stare attenti. VectorBase::ShiftRight usa Size(). 
        // Ma qui la size logica non è ancora aumentata. 
        // Implementiamo manualmente lo shift fisico per sicurezza sui raw data.
        
        long s = start.ToLong();
        long d = n.ToLong();
        long limit = size; // Vecchia size

        // Spostiamo da (limit-1) a (s) verso destra di d posizioni
        for (long i = limit - 1; i >= s; i--) {
            SetAt(GetAt(Natural.Of(i)), Natural.Of(i + d));
        }
        // Puliamo il buco creato
        for (long i = s; i < s + d; i++) {
            SetAt(null, Natural.Of(i));
        }
        
        // 3. Incremento Size
        size += d;
    }

    /**
     * ShiftLeft ora deve:
     * 1. Spostare gli elementi.
     * 2. Decrementare la size.
     * 3. Ridurre la memoria (Reduce) se necessario.
     */
    @Override
    public void ShiftLeft(Natural start, Natural n) {
        long s = start.ToLong();
        long d = n.ToLong();
        long limit = size;

        if (s + d > limit) return; // O throw

        // 1. Shift fisico
        for (long i = s; i < limit - d; i++) {
            SetAt(GetAt(Natural.Of(i + d)), Natural.Of(i));
        }
        // Puliamo la coda (che verrà tagliata dalla size, ma per pulizia memoria)
        for (long i = limit - d; i < limit; i++) {
            SetAt(null, Natural.Of(i));
        }

        // 2. Decremento Size
        size -= d;

        // 3. Reduce automatico (richiesto dai test)
        Reduce();
    }

    @Override
    public void InsertAt(Data dat, Natural pos) {
        if (pos.compareTo(Size()) > 0) throw new IndexOutOfBoundsException();

        // Delega tutto a ShiftRight (che fa Expand e size++)
        ShiftRight(pos, Natural.ONE);
        
        // Scrive il dato
        SetAt(dat, pos);
    }

    @Override
    public Data AtNRemove(Natural pos) {
        if (pos.compareTo(Size()) >= 0) throw new IndexOutOfBoundsException();

        Data removed = GetAt(pos);

        // Delega tutto a ShiftLeft (che fa size-- e Reduce)
        // ShiftLeft a partire da pos, di 1 posizione. 
        // Questo sovrascrive 'pos' con 'pos+1' e accorcia la lista.
        ShiftLeft(pos, Natural.ONE);

        return removed;
    }

    // Alias per compatibilità interfaccia
    @Override public void ShiftLeft(Natural n) { ShiftLeft(Natural.ZERO, n); }
    @Override public void ShiftFirstLeft() { ShiftLeft(Natural.ONE); }
    @Override public void ShiftLastLeft() { ShiftLeft(Size().Decrement(), Natural.ONE); }
    @Override public void ShiftRight(Natural n) { ShiftRight(Natural.ZERO, n); }
    @Override public void ShiftFirstRight() { ShiftRight(Natural.ONE); }
    @Override public void ShiftLastRight() { ShiftRight(Size().Decrement(), Natural.ONE); }

    /* ************************************************************************ */
    /* Metodi Resize                                                            */
    /* ************************************************************************ */
    
    @Override
    public void Expand() {
        long newCap = (Capacity().IsZero()) ? 1 : (long)(Capacity().ToLong() * GROW_FACTOR);
        Expand(Natural.Of(newCap - Capacity().ToLong()));
    }

    @Override
    public void Grow() { Expand(); }
    @Override
    public void Grow(Natural n) { Expand(n); }
    
    @Override
    public void Shrink() {
        long cap = Capacity().ToLong();
        long sz = Size().ToLong();
        if (sz > 0 && cap >= (long)(sz * SHRINK_FACTOR * THRESHOLD_FACTOR)) {
             long newCap = (long)(cap / SHRINK_FACTOR);
             if (newCap < sz) newCap = sz;
             Realloc(Natural.Of(newCap));
        }
    }
    @Override
    public void Reduce() { Shrink(); }

    /* ************************************************************************ */
    /* Metodi Accessori e Utility                                               */
    /* ************************************************************************ */

    @Override public boolean IsEmpty() { return Size().IsZero(); }
    @Override public Data GetFirst() { if (IsEmpty()) throw new IndexOutOfBoundsException(); return GetAt(Natural.ZERO); }
    @Override public Data GetLast() { if (IsEmpty()) throw new IndexOutOfBoundsException(); return GetAt(Size().Decrement()); }
    @Override public void InsertFirst(Data dat) { InsertAt(dat, Natural.ZERO); }
    @Override public void InsertLast(Data dat) { InsertAt(dat, Size()); } // InsertAt gestisce Expand
    @Override public void RemoveAt(Natural n) { AtNRemove(n); }
    @Override public void RemoveFirst() { RemoveAt(Natural.ZERO); }
    @Override public Data FirstNRemove() { return AtNRemove(Natural.ZERO); }
    @Override public void RemoveLast() { RemoveAt(Size().Decrement()); }
    @Override public Data LastNRemove() { return AtNRemove(Size().Decrement()); }

    @Override
    public Natural Search(Data dat) {
        long sz = Size().ToLong();
        for(long i=0; i<sz; i++) {
            Data curr = GetAt(Natural.Of(i));
            if ((curr==null && dat==null) || (curr!=null && curr.equals(dat))) return Natural.Of(i);
        }
        return null;
    }
    @Override public boolean IsInBound(Natural n) { return n.compareTo(Size()) < 0; }
    @Override public boolean Exists(Data dat) { return Search(dat) != null; }
    @Override
    public boolean TraverseForward(Predicate<Data> predicate) {
        long sz = Size().ToLong();
        for(long i=0; i<sz; i++) if (predicate.Apply(GetAt(Natural.Of(i)))) return true;
        return false;
    }
    @Override
    public boolean TraverseBackward(Predicate<Data> predicate) {
        long sz = Size().ToLong();
        for(long i=sz-1; i>=0; i--) if (predicate.Apply(GetAt(Natural.Of(i)))) return true;
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
    @Override public Data GetNSetAt(Data dat, Natural n) { Data old = GetAt(n); SetAt(dat, n); return old; }
    @Override public void SetFirst(Data dat) { if(IsEmpty()) throw new IndexOutOfBoundsException(); SetAt(dat, Natural.ZERO); }
    @Override public Data GetNSetFirst(Data dat) { if(IsEmpty()) throw new IndexOutOfBoundsException(); return GetNSetAt(dat, Natural.ZERO); }
    @Override public void SetLast(Data dat) { if(IsEmpty()) throw new IndexOutOfBoundsException(); SetAt(dat, Size().Decrement()); }
    @Override public Data GetNSetLast(Data dat) { if(IsEmpty()) throw new IndexOutOfBoundsException(); return GetNSetAt(dat, Size().Decrement()); }
    @Override public void Swap(Natural pos1, Natural pos2) { Data tmp = GetAt(pos1); SetAt(GetAt(pos2), pos1); SetAt(tmp, pos2); }
    
    @Override
    public DynVector<Data> SubVector(Natural start, Natural end) {
        long len = end.ToLong() - start.ToLong();
        if (len < 0) throw new IllegalArgumentException();
        DynVector<Data> sub = new DynVector<>(Natural.Of(len));
        for(long i=0; i<len; i++) sub.InsertLast(GetAt(Natural.Of(start.ToLong() + i)));
        return sub;
    }
}