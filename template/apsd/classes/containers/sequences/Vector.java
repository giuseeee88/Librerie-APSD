package apsd.classes.containers.sequences;

import apsd.classes.containers.sequences.abstractbases.LinearVectorBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.traits.Predicate;

public class Vector<Data> extends LinearVectorBase<Data> implements apsd.interfaces.containers.sequences.Vector<Data> {

    /* ************************************************************************ */
    /* Costruttori                                                              */
    /* ************************************************************************ */

    public Vector() { this(Natural.ZERO); }

    public Vector(Natural inisize) { ArrayAlloc(inisize); }

    public Vector(TraversableContainer<Data> con) {
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

    protected Vector(Data[] arr) { NewVector(arr); }

    public static <Data> Vector<Data> Wrap(Data[] arr) { return new Vector<>(arr); }

    /* ************************************************************************ */
    /* Metodi Core                                                              */
    /* ************************************************************************ */

    @Override
    public void Clear() {
        ArrayAlloc(Natural.ZERO);
    }

    @Override public Natural Size() { return Capacity(); }
    @Override public boolean IsEmpty() { return Size().IsZero(); }

    /* ************************************************************************ */
    /* Metodi di ACCESSO (Get)                                                  */
    /* ************************************************************************ */

    @Override 
    public Data GetFirst() { 
        if (IsEmpty()) throw new IndexOutOfBoundsException(); 
        return GetAt(Natural.ZERO); 
    }

    @Override 
    public Data GetLast() { 
        if (IsEmpty()) throw new IndexOutOfBoundsException(); 
        return GetAt(Size().Decrement()); 
    }

    /* ************************************************************************ */
    /* Metodi di MODIFICA VALORE (Set)                                          */
    /* ************************************************************************ */

    @Override 
    public void SetFirst(Data dat) { 
        if(IsEmpty()) throw new IndexOutOfBoundsException(); 
        SetAt(dat, Natural.ZERO); 
    }

    @Override 
    public void SetLast(Data dat) { 
        if(IsEmpty()) throw new IndexOutOfBoundsException(); 
        SetAt(dat, Size().Decrement()); 
    }

    @Override public Data GetNSetAt(Data dat, Natural n) { Data old = GetAt(n); SetAt(dat, n); return old; }
    @Override public Data GetNSetFirst(Data dat) { if(IsEmpty()) throw new IndexOutOfBoundsException(); return GetNSetAt(dat, Natural.ZERO); }
    @Override public Data GetNSetLast(Data dat) { if(IsEmpty()) throw new IndexOutOfBoundsException(); return GetNSetAt(dat, Size().Decrement()); }
    @Override public void Swap(Natural pos1, Natural pos2) { Data tmp = GetAt(pos1); SetAt(GetAt(pos2), pos1); SetAt(tmp, pos2); }

    /* ************************************************************************ */
    /* Metodi Shift                                                             */
    /* ************************************************************************ */

    /* * NOTA: Ho rimosso l'override di ShiftLeft(start, n). 
     * Ora la classe userà automaticamente l'implementazione 'default' definita 
     * nell'interfaccia apsd.interfaces.containers.sequences.Vector.
     */

    @Override 
    public void ShiftRight(Natural start, Natural n) { 
        long idx = start.ToLong();
        long shift = n.ToLong();
        long size = Size().ToLong();

        // Se lo shift è 0 o parte fuori dal vettore, non fare nulla
        if (shift == 0 || idx >= size) return;

        // 1. Spostamento (Loop all'indietro per non sovrascrivere i dati)
        // Spostiamo l'elemento che sta a (i - shift) nella posizione (i)
        // Partiamo dalla fine dell'array
        for (long i = size - 1; i >= idx + shift; i--) {
            Natural dest = Natural.Of(i);
            Natural src = Natural.Of(i - shift);
            SetAt(GetAt(src), dest);
        }

        // 2. Riempimento con null del buco creato all'inizio
        long limit = (idx + shift < size) ? (idx + shift) : size;
        for (long i = idx; i < limit; i++) {
            SetAt(null, Natural.Of(i));
        }
    }
    
    // Wrapper methods
    @Override public void ShiftFirstLeft() { ShiftLeft(Natural.ONE); }
    @Override public void ShiftLastLeft() { ShiftLeft(Size().Decrement(), Natural.ONE); }
    @Override public void ShiftFirstRight() { ShiftRight(Natural.ONE); }
    @Override public void ShiftLastRight() { ShiftRight(Size().Decrement(), Natural.ONE); }
    @Override public void ShiftLeft(Natural n) { ShiftLeft(Natural.ZERO, n); }
    @Override public void ShiftRight(Natural n) { ShiftRight(Natural.ZERO, n); }

    /* ************************************************************************ */
    /* Metodi Membership & Traversal                                            */
    /* ************************************************************************ */

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

    @Override
    public apsd.interfaces.containers.sequences.Vector<Data> SubVector(Natural start, Natural end) {
        long len = end.ToLong() - start.ToLong();
        if (len < 0) throw new IllegalArgumentException();
        Vector<Data> sub = new Vector<>(Natural.Of(len));
        for(long i=0; i<len; i++) sub.SetAt(GetAt(Natural.Of(start.ToLong() + i)), Natural.Of(i));
        return sub;
    }

    // TODO: Implementare la logica di ridimensionamento se richiesto
    @Override
    public void Grow() {
        // Logica per aumentare capacity (es. raddoppio)
    }

    @Override
    public void Grow(Natural n) {
        // Logica per aumentare capacity di n
    }

    @Override
    public void Shrink() {
        // Logica per ridurre capacity se troppo vuoto
    }
}