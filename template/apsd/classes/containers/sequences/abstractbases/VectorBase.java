package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.iterators.MutableBackwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
import apsd.interfaces.containers.sequences.MutableSequence;
import apsd.interfaces.containers.sequences.Vector;
import apsd.interfaces.traits.Predicate;

/** Object: Abstract vector base implementation. */
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
      for (int i = 0; i < arr.length; i++) {
        arr[i] = null;
      }
    }
  }

  @Override
  public Natural Capacity() {
    return (arr == null) ? new Natural(0) : new Natural(arr.length);
  }

  @Override
  public MutableForwardIterator<Data> FIterator() {
    return new MutableForwardIterator<Data>() {
      protected Natural current = new Natural(0);

      @Override
      public boolean IsValid() {
        return current.compareTo(Size()) < 0;
      }

      @Override
      public Data GetCurrent() {
        if (!IsValid()) throw new IllegalStateException("Iterator out of bounds");
        return GetAt(current);
      }

      @Override
      public void SetCurrent(Data dat) {
        if (!IsValid()) throw new IllegalStateException("Iterator out of bounds");
        SetAt(dat, current);
      }

      @Override
      public void Next() {
        current = current.Increment();
      }

      @Override
      public void Next(Natural n) {
         current = Natural.Of(current.ToLong() + n.ToLong());
      }

      @Override
      public Data DataNNext() {
        Data d = GetCurrent();
        Next();
        return d;
      }

      @Override
      public void Reset() {
        current = new Natural(0);
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
        if (current >= 0) current--;
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
                if (predicate.Apply(DataNPrev())) { return true; }
            }
        }
        return false;
      }
    };
  }

  @Override
  public MutableSequence<Data> SubSequence(Natural start, Natural end) {
    if (start.compareTo(end) > 0 || end.compareTo(Size()) > 0) {
        throw new IndexOutOfBoundsException("Invalid SubSequence indices");
    }
    return null; 
  }
}