package apsd.classes.containers.sequences.abstractbases;

import java.util.Arrays;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.iterators.MutableBackwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
import apsd.interfaces.containers.sequences.MutableSequence;
import apsd.interfaces.containers.sequences.Vector;
import apsd.interfaces.traits.Predicate;

abstract public class VectorBase<Data> implements Vector<Data> {

    protected Data[] arr;

    protected void NewVector(Data[] data) {
        arr = data;
    }

    @SuppressWarnings("unchecked")
    protected void ArrayAlloc(Natural newsize) {
        long size = newsize.ToLong();
        
        if (size > Integer.MAX_VALUE) {
            throw new ArithmeticException("Size too large for array allocation");
        }
        arr = (Data[]) new Object[(int) size];
    }

    @Override
    public void Clear() {
        if (arr != null) {
            Arrays.fill(arr, null);
        }
    }

    @Override
    public Natural Capacity() {
        return (arr == null) ? new Natural(0) : new Natural(arr.length);
    }

    @Override
    public MutableForwardIterator<Data> FIterator() {
        return new MutableForwardIterator<Data>() {
            protected long current = 0;

            @Override
            public boolean IsValid() {
                return current < Size().ToLong();
            }

            @Override
            public Data GetCurrent() {
                if (!IsValid()) throw new IllegalStateException("Iterator out of bounds");
                return GetAt(new Natural(current));
            }

            @Override
            public void SetCurrent(Data dat) {
                if (!IsValid()) throw new IllegalStateException("Iterator out of bounds");
                SetAt(dat, new Natural(current));
            }

            @Override
            public void Next() {
                current++;
            }

            @Override
            public void Next(Natural n) {
                current += n.ToLong();
            }

            @Override
            public Data DataNNext() {
                Data d = GetCurrent();
                Next();
                return d;
            }

            @Override
            public void Reset() {
                current = 0;
            }
        };
    }

    @Override
    public MutableBackwardIterator<Data> BIterator() {
        return new MutableBackwardIterator<Data>() {
            protected long current = Size().ToLong() - 1;

            @Override
            public boolean IsValid() {
                return current >= 0 && current < Size().ToLong();
            }

            @Override
            public Data GetCurrent() {
                if (!IsValid()) throw new IllegalStateException("Iterator out of bounds");
                return GetAt(new Natural(current));
            }

            @Override
            public void SetCurrent(Data dat) {
                if (!IsValid()) throw new IllegalStateException("Iterator out of bounds");
                SetAt(dat, new Natural(current));
            }

            @Override
            public void Prev() {
                current--;
            }

            @Override
            public void Prev(Natural n) {
                current -= n.ToLong();
            }

            @Override
            public Data DataNPrev() {
                Data d = GetCurrent();
                Prev();
                return d;
            }

            @Override
            public void Reset() {
                current = Size().ToLong() - 1;
            }

            @Override
            public boolean ForEachBackward(Predicate<Data> predicate) {
                if (predicate != null) {
                    while (IsValid()) {
                        if (predicate.Apply(DataNPrev())) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }

    @Override
    public MutableSequence<Data> SubSequence(Natural start, Natural end) {
        return null; 
    }
}