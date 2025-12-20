package apsd.classes.containers.sequences;

import apsd.classes.containers.sequences.abstractbases.CircularVectorBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.sequences.Vector; 
import apsd.interfaces.traits.Predicate;

public class CircularVector<Data> extends CircularVectorBase<Data> implements Vector<Data> {

    public CircularVector() { 
        this(Natural.ZERO); 
    }

    public CircularVector(Natural inisize) { 
        ArrayAlloc(inisize); 
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

    public static <Data> CircularVector<Data> Wrap(Data[] arr) { 
        CircularVector<Data> vec = new CircularVector<>(Natural.Of(arr.length));
        for(int i=0; i<arr.length; i++) {
            vec.SetAt(arr[i], Natural.Of(i));
        }
        return vec;
    }

    @Override
    public void Clear() {
        ArrayAlloc(Natural.ZERO); 
    }

    @Override public Natural Size() { return Capacity(); } 
    @Override public boolean IsEmpty() { return Size().IsZero(); }

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
    public void SetFirst(Data dat) { 
        if(IsEmpty()) throw new IndexOutOfBoundsException(); 
        SetAt(dat, Natural.ZERO); 
    }

    @Override 
    public void SetLast(Data dat) { 
        if(IsEmpty()) throw new IndexOutOfBoundsException(); 
        SetAt(dat, Size().Decrement()); 
    }

    @Override 
    public Data GetNSetAt(Data dat, Natural n) { 
        Data old = GetAt(n); 
        SetAt(dat, n); 
        return old; 
    }
    
    @Override 
    public Data GetNSetFirst(Data dat) { 
        if(IsEmpty()) throw new IndexOutOfBoundsException(); 
        return GetNSetAt(dat, Natural.ZERO); 
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

    @Override public void ShiftFirstLeft() { ShiftLeft(Natural.ZERO, Natural.ONE); }
    @Override public void ShiftLastLeft() { ShiftLeft(Size().Decrement(), Natural.ONE); }
    @Override public void ShiftFirstRight() { ShiftRight(Natural.ZERO, Natural.ONE); }
    @Override public void ShiftLastRight() { ShiftRight(Size().Decrement(), Natural.ONE); }
    
    @Override public void ShiftLeft(Natural n) { ShiftLeft(Natural.ZERO, n); }
    @Override public void ShiftRight(Natural n) { ShiftRight(Natural.ZERO, n); }
    
    @Override 
    public void ShiftLeft(Natural start, Natural n) { 
        super.ShiftLeft(start, n);
    }

    @Override
    public void ShiftRight(Natural start, Natural n) {
        super.ShiftRight(start, n);
        // Pulizia: mettiamo a null le posizioni liberate dallo shift
        long idx = start.ToLong();
        long num = n.ToLong();
        for (long i = 0; i < num; i++) {
            SetAt(null, Natural.Of(idx + i));
        }
    }

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
    
    @Override public boolean IsEqual(IterableContainer<Data> container) {
        if (container == null || Size().compareTo(container.Size()) != 0) return false;
        final long[] idx = {0};
        return !container.TraverseForward(other -> {
            Data mine = GetAt(Natural.Of(idx[0]++));
            return !((mine == null) ? other == null : mine.equals(other));
        });
    }

    @Override
    public Vector<Data> SubVector(Natural start, Natural end) {
        long len = end.ToLong() - start.ToLong();
        if (len < 0) throw new IllegalArgumentException();
        CircularVector<Data> sub = new CircularVector<>(Natural.Of(len));
        for(long i=0; i<len; i++) {
            sub.SetAt(GetAt(Natural.Of(start.ToLong() + i)), Natural.Of(i));
        }
        return sub;
    }

    @Override
    public void Grow() {
        long currentCap = Capacity().ToLong();
        long newCap = (currentCap == 0) ? 1 : currentCap * 2;
        Realloc(Natural.Of(newCap));
    }

    @Override
    public void Grow(Natural n) {
        long currentCap = Capacity().ToLong();
        long increase = n.ToLong();
        Realloc(Natural.Of(currentCap + increase));
    }

    @Override
    public void Shrink() {
        Realloc(Size());
    }
}