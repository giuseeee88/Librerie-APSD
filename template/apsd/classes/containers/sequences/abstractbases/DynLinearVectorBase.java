package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.sequences.DynVector;

abstract public class DynLinearVectorBase<Data> extends LinearVectorBase<Data> implements DynVector<Data> {

    protected long size = 0L;

    @Override
    public Natural Size() {
        return new Natural(size);
    }

    @Override
    public void Clear() {
        size = 0;
        super.Clear();
    }

    @Override
    public void Realloc(Natural newsize) {
        super.Realloc(newsize);
    }

    @Override
    public void Expand(Natural n) {
        long requiredSize = size + n.ToLong();

        if (requiredSize > Capacity().ToLong()) {
            Realloc(Natural.Of(requiredSize));
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
    public void ArrayAlloc(Natural newsize) {
        super.ArrayAlloc(newsize);
    }
}