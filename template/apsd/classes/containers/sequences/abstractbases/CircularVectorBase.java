package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;

abstract public class CircularVectorBase<Data> extends VectorBase<Data> {

  protected long start = 0L;

  @SuppressWarnings("unchecked")
  @Override
  public void Realloc(Natural n) {
    long sz = Size().ToLong();
    Data[] newArr = (Data[]) new Object[(int) n.ToLong()];
    for (long i = 0; i < sz; i++) {
      newArr[(int) i] = GetAt(new Natural(i));
    }
    arr = newArr;
    start = 0;
  }

  @Override
  public Data GetAt(Natural n) {
    long idx = (start + n.ToLong()) % Capacity().ToLong();
    return arr[(int) idx];
  }

  @Override
  public void SetAt(Data dat, Natural n) {
    long idx = (start + n.ToLong()) % Capacity().ToLong();
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