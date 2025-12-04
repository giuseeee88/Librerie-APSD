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
    }

    public DynCircularVector(TraversableContainer<Data> con) {
        Realloc(con.Size());
        con.TraverseForward(new Predicate<Data>() {
            @Override
            public boolean Apply(Data dat) {
                InsertLast(dat);
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

    @Override
    public void Clear() {
        super.Clear();
    }

    @Override
    public void Realloc(Natural newsize) {
        super.Realloc(newsize);
    }

    /* ************************************************************************ */
    /* Structural Modification Methods (Insert/Remove/Shift)                    */
    /* ************************************************************************ */

    @Override
    public void InsertAt(Data dat, Natural pos) {
        if (pos.compareTo(Size()) > 0) throw new IndexOutOfBoundsException("Insert index out of bounds");

        if (Size().compareTo(Capacity()) >= 0) {
            Expand();
        }

        if (pos.compareTo(Size()) < 0) {
            ShiftRight(pos, Natural.ONE);
        }

        SetAt(dat, pos);
        size++;
    }

    @Override
    public Data AtNRemove(Natural pos) {
        if (pos.compareTo(Size()) >= 0) throw new IndexOutOfBoundsException("Remove index out of bounds");

        Data removed = GetAt(pos);

        if (pos.compareTo(Size().Decrement()) < 0) {
            ShiftLeft(pos.Increment(), Natural.ONE);
        }

        size--;
        // In un vettore circolare, riduciamo la size e potenzialmente 'puliamo' l'elemento logicamente in coda
        // Nota: SetAt usa la logica circolare, quindi SetAt(..., Size()) punta al vecchio ultimo elemento
        SetAt(null, Size()); 
        
        // Shrink(); // Opzionale: ridimensionamento automatico

        return removed;
    }

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

    @Override public void ShiftLeft(Natural n) { ShiftLeft(Natural.ZERO, n); }
    @Override public void ShiftFirstLeft() { ShiftLeft(Natural.ONE); }
    @Override public void ShiftLastLeft() { ShiftLeft(Size().Decrement(), Natural.ONE); }

    @Override public void ShiftRight(Natural n) { ShiftRight(Natural.ZERO, n); }
    @Override public void ShiftFirstRight() { ShiftRight(Natural.ONE); }
    @Override public void ShiftLastRight() { ShiftRight(Size().Decrement(), Natural.ONE); }

    /* ************************************************************************ */
    /* Accessors and Mutators (MutableSequence)                                 */
    /* ************************************************************************ */

    @Override
    public Data GetNSetAt(Data dat, Natural n) {
        Data old = GetAt(n);
        SetAt(dat, n);
        return old;
    }

    @Override
    public void SetFirst(Data dat) {
        if (IsEmpty()) throw new IndexOutOfBoundsException();
        SetAt(dat, Natural.ZERO);
    }

    @Override
    public Data GetNSetFirst(Data dat) {
        if (IsEmpty()) throw new IndexOutOfBoundsException();
        return GetNSetAt(dat, Natural.ZERO);
    }

    @Override
    public void SetLast(Data dat) {
        if (IsEmpty()) throw new IndexOutOfBoundsException();
        SetAt(dat, Size().Decrement());
    }

    @Override
    public Data GetNSetLast(Data dat) {
        if (IsEmpty()) throw new IndexOutOfBoundsException();
        return GetNSetAt(dat, Size().Decrement());
    }

    @Override
    public void Swap(Natural n1, Natural n2) {
        Data tmp = GetAt(n1);
        SetAt(GetAt(n2), n1);
        SetAt(tmp, n2);
    }

    /* ************************************************************************ */
    /* Utility Methods (Vector/Container)                                       */
    /* ************************************************************************ */

    @Override
    public boolean IsEmpty() {
        return Size().IsZero();
    }

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

    @Override
    public void InsertFirst(Data dat) {
        InsertAt(dat, Natural.ZERO);
    }

    @Override
    public void InsertLast(Data dat) {
        InsertAt(dat, Size());
    }

    @Override
    public void RemoveAt(Natural n) {
        AtNRemove(n);
    }

    @Override
    public void RemoveFirst() {
        RemoveAt(Natural.ZERO);
    }

    @Override
    public Data FirstNRemove() {
        return AtNRemove(Natural.ZERO);
    }

    @Override
    public void RemoveLast() {
        RemoveAt(Size().Decrement());
    }

    @Override
    public Data LastNRemove() {
        return AtNRemove(Size().Decrement());
    }

    @Override
    public Natural Search(Data dat) {
        long sz = Size().ToLong();
        for(long i=0; i<sz; i++) {
            Data curr = GetAt(Natural.Of(i));
            if ((curr==null && dat==null) || (curr!=null && curr.equals(dat))) {
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
    public boolean Exists(Data dat) {
        return Search(dat) != null;
    }

    @Override
    public boolean TraverseForward(Predicate<Data> predicate) {
        long sz = Size().ToLong();
        for(long i=0; i<sz; i++) {
            if (predicate.Apply(GetAt(Natural.Of(i)))) return true;
        }
        return false;
    }

    @Override
    public boolean TraverseBackward(Predicate<Data> predicate) {
        long sz = Size().ToLong();
        for(long i=sz-1; i>=0; i--) {
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
        if (len < 0) throw new IllegalArgumentException("Invalid range");
        
        DynCircularVector<Data> sub = new DynCircularVector<>(Natural.Of(len));
        for(long i=0; i<len; i++) {
            sub.InsertLast(GetAt(Natural.Of(start.ToLong() + i)));
        }
        return sub;
    }

    // Expand/Shrink/Grow/Reduce implementation handled by Base or defaults
    @Override
    public void Expand() {
        // Calcola la nuova capacità basandosi sul fattore di crescita
        long currentCap = Capacity().ToLong();
        // Se è vuoto diventa 1, altrimenti moltiplica per il fattore (es. raddoppia)
        long newCap = (currentCap == 0) ? 1 : (long)(currentCap * GROW_FACTOR);
        
        // Calcola l'incremento necessario
        long increment = newCap - currentCap;
        
        // Chiama Expand(Natural n) che è implementato nella superclasse DynCircularVectorBase
        Expand(Natural.Of(increment));
    }

    @Override
    public void Grow() {
        // Grow è un alias per Expand
        Expand();
    }

    @Override
    public void Grow(Natural n) {
        // Chiama l'implementazione della base DynCircularVectorBase
        Expand(n);
    }

    @Override
    public void Shrink() {
        // Logica di riduzione: riduciamo solo se la capacità è eccessiva rispetto alla size
        long cap = Capacity().ToLong();
        long sz = Size().ToLong();
        
        // Verifica la condizione: Capacity > Size * THRESHOLD * SHRINK_FACTOR
        // Esempio: Se size=10, threshold=2, shrink=2 -> riduci se cap > 40
        if (sz > 0 && cap > (long)(sz * THRESHOLD_FACTOR * SHRINK_FACTOR)) {
             long newCap = (long)(cap / SHRINK_FACTOR);
             
             // Sicurezza: mai scendere sotto la size attuale
             if (newCap < sz) newCap = sz; 
             
             Realloc(Natural.Of(newCap));
        }
    }
    
    @Override
    public void Reduce() {
        // Reduce standard chiama Shrink
        Shrink();
    }
    
    @Override
    public void Reduce(Natural n) {
        // Riduzione esplicita di n posizioni (se supportata)
        // Calcoliamo la nuova capacità target
        long cap = Capacity().ToLong();
        long decrement = n.ToLong();
        if (decrement < cap) {
             long newCap = cap - decrement;
             // Non possiamo scendere sotto la Size attuale
             if (newCap < Size().ToLong()) newCap = Size().ToLong();
             Realloc(Natural.Of(newCap));
        }
    }
}