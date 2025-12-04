package apsd.classes.containers.sequences;

import apsd.classes.containers.sequences.abstractbases.DynLinearVectorBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.traits.Predicate;

/** Object: Concrete dynamic (linear) vector implementation. */
public class DynVector<Data> extends DynLinearVectorBase<Data> {

    // Costruttore vuoto
    public DynVector() {
        this(Natural.ZERO);
    }

    // Costruttore con dimensione iniziale
    public DynVector(Natural inisize) {
        Realloc(inisize);
        // Size rimane 0 (inizializzata in base), Capacity diventa inisize
    }

    // Costruttore da container
    public DynVector(TraversableContainer<Data> con) {
        Realloc(con.Size());
        con.TraverseForward(new Predicate<Data>() {
            @Override
            public boolean Apply(Data dat) {
                InsertLast(dat);
                return false; // Continua l'attraversamento
            }
        });
    }

    // Costruttore protetto (wrap array)
    protected DynVector(Data[] arr) {
        NewVector(arr);
        this.size = (arr != null) ? arr.length : 0;
    }

    // Factory statico (rinominato Wrap per evitare name clash)
    public static <Data> DynVector<Data> Wrap(Data[] arr) {
        return new DynVector<>(arr);
    }

    /* ************************************************************************ */
    /* Metodi di Modifica Strutturale (Insert/Remove/Shift)                     */
    /* ************************************************************************ */

    @Override
    public void InsertAt(Data dat, Natural pos) {
        if (pos.compareTo(Size()) > 0) throw new IndexOutOfBoundsException("Insert index out of bounds");

        // Se siamo pieni, espandiamo
        if (Size().compareTo(Capacity()) >= 0) {
            Expand();
        }

        // Spostiamo a destra per fare spazio, partendo dalla fine fino a pos
        if (pos.compareTo(Size()) < 0) {
            ShiftRight(pos, Natural.ONE);
        }

        // Inseriamo e incrementiamo size
        SetAt(dat, pos);
        size++;
    }

    @Override
    public Data AtNRemove(Natural pos) {
        if (pos.compareTo(Size()) >= 0) throw new IndexOutOfBoundsException("Remove index out of bounds");

        Data removed = GetAt(pos);

        // Spostiamo a sinistra per coprire il buco
        if (pos.compareTo(Size().Decrement()) < 0) {
            ShiftLeft(pos.Increment(), Natural.ONE);
        }

        // Decrementiamo size e annulliamo l'ultimo elemento (ora duplicato)
        size--;
        SetAt(null, Size()); // Size() ritorna la nuova dimensione, che ora punta alla vecchia "fine"

        // Riduciamo se necessario (opzionale, ma buona pratica per DynVector)
        // Shrink(); 

        return removed;
    }

    @Override
    public void ShiftLeft(Natural start, Natural n) {
        // Shift logico degli elementi a sinistra
        // Nota: Questo metodo sposta i dati ma NON cambia la 'size' del vettore 
        // a meno che non sia usato internamente da Remove.
        // Se usato come operazione pubblica di MutableSequence, sovrascrive.
        
        long s = start.ToLong();
        long d = n.ToLong();
        long limit = Size().ToLong();

        if (s + d >= limit) return;

        for (long i = s; i < limit - d; i++) {
            SetAt(GetAt(Natural.Of(i + d)), Natural.Of(i));
        }
        // Pulizia coda
        for (long i = limit - d; i < limit; i++) {
            SetAt(null, Natural.Of(i));
        }
    }

    @Override
    public void ShiftRight(Natural start, Natural n) {
        long s = start.ToLong();
        long d = n.ToLong();
        long limit = Size().ToLong();

        // Controllo capacità: shift right potrebbe spingere elementi fuori o richiedere resize?
        // In MutableSequence solitamente shift perde elementi o è confinato.
        // Qui assumiamo shift interno senza resize automatico (a meno di Insert).
        
        if (s + d >= limit) return; // O throw, dipende dalla specifica

        for (long i = limit - 1; i >= s + d; i--) {
            SetAt(GetAt(Natural.Of(i - d)), Natural.Of(i));
        }
        // Pulizia testa
        for (long i = s; i < s + d; i++) {
            SetAt(null, Natural.Of(i));
        }
    }
    
    // Alias per Shift
    @Override public void ShiftLeft(Natural n) { ShiftLeft(Natural.ZERO, n); }
    @Override public void ShiftFirstLeft() { ShiftLeft(Natural.ONE); }
    @Override public void ShiftLastLeft() { ShiftLeft(Size().Decrement(), Natural.ONE); } // Poco utile, sposta solo l'ultimo su se stesso o fuori? Spesso inteso shift(N-1, 1)

    @Override public void ShiftRight(Natural n) { ShiftRight(Natural.ZERO, n); }
    @Override public void ShiftFirstRight() { ShiftRight(Natural.ONE); }
    @Override public void ShiftLastRight() { ShiftRight(Size().Decrement(), Natural.ONE); }


    /* ************************************************************************ */
    /* Metodi di Gestione Memoria (Reallocable/Resizable)                       */
    /* ************************************************************************ */

    @Override
    public void Clear() {
        super.Clear(); // Chiama DynLinearVectorBase.Clear() che azzera size e VectorBase.Clear() che pulisce array
    }

    @Override
    public void Expand() {
        // Raddoppia la capacità (GROW_FACTOR = 2.0)
        long newCap = (Capacity().IsZero()) ? 1 : (long)(Capacity().ToLong() * GROW_FACTOR);
        Expand(Natural.Of(newCap - Capacity().ToLong()));
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
        // Riduci se size < capacity / (SHRINK_FACTOR * THRESHOLD)
        // Logica semplificata: se size è 1/4 della capacity, dimezza.
        long cap = Capacity().ToLong();
        long sz = Size().ToLong();
        if (sz > 0 && cap >= (long)(sz * SHRINK_FACTOR * THRESHOLD_FACTOR)) {
             long newCap = (long)(cap / SHRINK_FACTOR);
             if (newCap < sz) newCap = sz; // Safety
             Realloc(Natural.Of(newCap));
        }
    }
    
    @Override
    public void Reduce() {
        Shrink();
    }


    /* ************************************************************************ */
    /* Metodi Accessori e Utility                                               */
    /* ************************************************************************ */

    @Override
    public boolean IsEmpty() {
        return Size().IsZero();
    }

    @Override
    public Data GetFirst() {
        if (IsEmpty()) throw new IndexOutOfBoundsException("Empty vector");
        return GetAt(Natural.ZERO);
    }

    @Override
    public Data GetLast() {
        if (IsEmpty()) throw new IndexOutOfBoundsException("Empty vector");
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
        return null; // Non trovato
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
    public Data GetNSetAt(Data dat, Natural n) {
        Data old = GetAt(n);
        SetAt(dat, n);
        return old;
    }

    @Override
    public void SetFirst(Data dat) {
        if(IsEmpty()) throw new IndexOutOfBoundsException();
        SetAt(dat, Natural.ZERO);
    }

    @Override
    public Data GetNSetFirst(Data dat) {
        if(IsEmpty()) throw new IndexOutOfBoundsException();
        return GetNSetAt(dat, Natural.ZERO);
    }

    @Override
    public void SetLast(Data dat) {
        if(IsEmpty()) throw new IndexOutOfBoundsException();
        SetAt(dat, Size().Decrement());
    }

    @Override
    public Data GetNSetLast(Data dat) {
        if(IsEmpty()) throw new IndexOutOfBoundsException();
        return GetNSetAt(dat, Size().Decrement());
    }

    @Override
    public void Swap(Natural pos1, Natural pos2) {
        Data tmp = GetAt(pos1);
        SetAt(GetAt(pos2), pos1);
        SetAt(tmp, pos2);
    }

    @Override
    public apsd.interfaces.containers.sequences.DynVector<Data> SubVector(Natural start, Natural end) {
        long len = end.ToLong() - start.ToLong();
        if (len < 0) throw new IllegalArgumentException("Invalid range");
        
        DynVector<Data> sub = new DynVector<>(Natural.Of(len));
        for(long i=0; i<len; i++) {
            sub.InsertLast(GetAt(Natural.Of(start.ToLong() + i)));
        }
        return sub;
    }
}