package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.sequences.DynVector;

public abstract class DynCircularVectorBase<Data> extends CircularVectorBase<Data> implements DynVector<Data> {

    protected long size = 0L;

    @Override
    public Natural Size() {
        return new Natural(size);
    }

    @Override
    public void Clear() {
        this.size = 0;
        this.start = 0;
        super.Clear();
    }

    @Override
    public void Realloc(Natural newsize) {
        super.Realloc(newsize);
    }

    @Override
    public void Expand(Natural n) {
        long reqSize = size + n.ToLong();
        if (reqSize > Capacity().ToLong()) {
            Realloc(Natural.Of(reqSize));
        }
    }

    @Override
    public void Reduce(Natural n) {
        long sz = size;
        long cap = Capacity().ToLong();
        long buffer = n.ToLong();

        if (cap > sz + buffer) {
            Realloc(Natural.Of(sz + buffer));
        }
    }

    @Override
    public void ShiftLeft(Natural start, Natural end) {
        long idx = start.ToLong();
        long delta = end.ToLong();
        long cap = Capacity().ToLong();

        if (idx < size / 2) {
            for (long i = idx - 1; i >= 0; i--) {
                long src = (this.start + i) % cap;
                long dst = (this.start + i + delta) % cap;
                arr[(int) dst] = arr[(int) src];
                arr[(int) src] = null;
            }
            this.start = (this.start + delta) % cap;
        } else {
            for (long i = idx + delta; i < size; i++) {
                long src = (this.start + i) % cap;
                long dst = (this.start + i - delta + cap) % cap;
                arr[(int) dst] = arr[(int) src];
                arr[(int) src] = null;
            }
        }
    }

    @Override
    public void ShiftRight(Natural start, Natural end) {
        long idx = start.ToLong();
        long delta = end.ToLong();
        long cap = Capacity().ToLong();

        if (idx < size / 2) {
            for (long i = 0; i < idx; i++) {
                long src = (this.start + i) % cap;
                long dst = (this.start + i - delta + cap) % cap;
                arr[(int) dst] = arr[(int) src];
            }
            this.start = (this.start - delta + cap) % cap;
        } else {
            for (long i = size - 1; i >= idx; i--) {
                long src = (this.start + i) % cap;
                long dst = (this.start + i + delta) % cap;
                arr[(int) dst] = arr[(int) src];
            }
        }
    }

    @Override
    public void ArrayAlloc(Natural newsize) {
        super.ArrayAlloc(newsize);
    }
}