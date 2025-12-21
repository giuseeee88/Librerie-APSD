package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;

abstract public class LinearVectorBase<Data> extends VectorBase<Data> {

    @SuppressWarnings("unchecked")
    @Override
    public void Realloc(Natural size) {
        long newCap = size.ToLong();
        Data[] newArr = (Data[]) new Object[(int) newCap];

        if (arr != null) {
            long oldCap = arr.length;
            long limit = (oldCap < newCap) ? oldCap : newCap;

            for (int i = 0; i < limit; i++) {
                newArr[i] = arr[i];
            }
        }
        this.arr = newArr;
    }

    @Override
    public Data GetAt(Natural n) {
        return arr[(int) n.ToLong()];
    }

    @Override
    public void SetAt(Data dat, Natural n) {
        arr[(int) n.ToLong()] = dat;
    }
}