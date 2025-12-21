package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;

public abstract class CircularVectorBase<Data> extends VectorBase<Data> {

    protected long start = 0L;

    @SuppressWarnings("unchecked")
    @Override
    public void Realloc(Natural n) {
        long sz = Size().ToLong();
        long newCap = n.ToLong();
        Data[] newArr = (Data[]) new Object[(int) newCap];

        long limit = (sz < newCap) ? sz : newCap;

        if (Capacity().ToLong() > 0 && limit > 0) {
            for (long i = 0; i < limit; i++) {
                newArr[(int) i] = GetAt(new Natural(i));
            }
        }

        arr = newArr;
        start = 0;
    }

    @Override
    public Data GetAt(Natural n) {
        long cap = Capacity().ToLong();
        if (cap == 0 || n.compareTo(Size()) >= 0) {
            throw new IndexOutOfBoundsException("Index " + n + " out of bounds (Size: " + Size() + ")");
        }

        long idx = (start + n.ToLong()) % cap;
        return arr[(int) idx];
    }

    @Override
    public void SetAt(Data dat, Natural n) {
        long cap = Capacity().ToLong();
        if (cap == 0 || n.compareTo(Size()) >= 0) {
            throw new IndexOutOfBoundsException("Index " + n + " out of bounds (Size: " + Size() + ")");
        }

        long idx = (start + n.ToLong()) % cap;
        arr[(int) idx] = dat;
    }

    @Override
    public void ShiftLeft(Natural startIdx, Natural count) {
        long idx = startIdx.ToLong();
        long delta = count.ToLong();
        long sz = Size().ToLong();
        long cap = Capacity().ToLong();

        if (idx < sz / 2) {
            for (long i = idx - 1; i >= 0; i--) {
                long src = (this.start + i) % cap;
                long dst = (this.start + i + delta) % cap;
                arr[(int) dst] = arr[(int) src];
                arr[(int) src] = null;
            }
            this.start = (this.start + delta) % cap;
        } else {
            for (long i = idx + delta; i < sz; i++) {
                long src = (this.start + i) % cap;
                long dst = (this.start + i - delta + cap) % cap;
                arr[(int) dst] = arr[(int) src];
                arr[(int) src] = null;
            }
        }
    }

    @Override
    public void ShiftRight(Natural startIdx, Natural count) {
        long idx = startIdx.ToLong();
        long delta = count.ToLong();
        long sz = Size().ToLong();
        long cap = Capacity().ToLong();

        if (idx < sz / 2) {
            for (long i = 0; i < idx; i++) {
                long src = (this.start + i) % cap;
                long dst = (this.start + i - delta + cap) % cap;
                arr[(int) dst] = arr[(int) src];
            }
            this.start = (this.start - delta + cap) % cap;
        } else {
            for (long i = sz - 1; i >= idx; i--) {
                long src = (this.start + i) % cap;
                long dst = (this.start + i + delta) % cap;
                arr[(int) dst] = arr[(int) src];
            }
        }
    }

    @Override
    public void ArrayAlloc(Natural n) {
        super.ArrayAlloc(n);
        start = 0;
    }
}