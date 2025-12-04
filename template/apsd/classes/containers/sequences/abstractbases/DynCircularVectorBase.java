package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.sequences.DynVector;

/** Object: Abstract dynamic circular vector base implementation. */
abstract public class DynCircularVectorBase<Data> extends CircularVectorBase<Data> implements DynVector<Data> {

  protected long size = 0L;

  @Override
  public Natural Size() {
    return new Natural(size);
  }

  @Override
  public void Clear() {
    this.size = 0;
    this.start = 0;
    super.Clear(); // Pulisce il contenuto dell'array fisico
  }

  @Override
  public void Realloc(Natural size) {
    // Delega alla base che gestisce la linearizzazione e la nuova allocazione
    super.Realloc(size); 
  }

  @Override
  public void Expand(Natural n) {
    long reqSize = size + n.ToLong();
    if (reqSize > Capacity().ToLong()) {
      // Se la dimensione richiesta supera la capacità, riallochiamo.
      // Qui si potrebbe applicare il GROW_FACTOR, ma per ora soddisfiamo la richiesta minima.
      Realloc(Natural.Of(reqSize));
    }
  }

  @Override
  public void Reduce(Natural n) {
    // Logica di riduzione (Shrink) opzionale o da definire secondo policy specifiche
  }

  @Override
  public void ShiftLeft(Natural start, Natural end) {
     // Implementazione dello shift circolare a sinistra
  }

  @Override
  public void ShiftRight(Natural start, Natural end) {
     // Implementazione dello shift circolare a destra
  }

  @Override
  public void ArrayAlloc(Natural newsize) {
    super.ArrayAlloc(newsize);
  }
}