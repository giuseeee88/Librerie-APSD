package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;

abstract public class CircularVectorBase<Data> extends VectorBase<Data> {

  protected long start = 0L;

  @SuppressWarnings("unchecked")
  @Override
  public void Realloc(Natural n) {
    long sz = Size().ToLong();
    long newCap = n.ToLong();
    Data[] newArr = (Data[]) new Object[(int) newCap];

    // Linearizzazione: Copia elementi nel nuovo array.
    // IMPORTANTE: Copiamo solo finché c'è spazio nel nuovo array e ci sono elementi nel vecchio.
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
  public void ShiftLeft(Natural start, Natural end) {
  }

  @Override
  public void ShiftRight(Natural start, Natural end) {
  }

  @Override
  public void ArrayAlloc(Natural n) {
    super.ArrayAlloc(n);
    start = 0;
  }
}