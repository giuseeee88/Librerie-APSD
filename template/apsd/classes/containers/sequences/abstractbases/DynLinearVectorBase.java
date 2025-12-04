package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.sequences.DynVector;

/** Object: Abstract dynamic linear vector base implementation. */
abstract public class DynLinearVectorBase<Data> extends LinearVectorBase<Data> implements DynVector<Data> {

  protected long size = 0L;

  // DynLinearVectorBase

  @Override
  public Natural Size() {
    return new Natural(size);
  }

  @Override
  public void Clear() {
    size = 0;
    super.Clear(); // Pulisce l'array fisico in VectorBase
  }

  @Override
  public void Realloc(Natural size) {
    // Delega a LinearVectorBase che gestisce la copia lineare degli elementi
    super.Realloc(size);
  }

  @Override
  public void Expand(Natural n) {
    // Calcolo la nuova dimensione richiesta usando ToLong()
    long requiredSize = size + n.ToLong();
    
    // Se la dimensione richiesta supera la capacità attuale, rialloca
    if (requiredSize > Capacity().ToLong()) {
      Realloc(Natural.Of(requiredSize));
    }
  }

  @Override
  public void Reduce(Natural n) {
    // Implementazione vuota (hook per eventuale logica di riduzione, 
    // se non gestita diversamente da Shrink o policy specifiche)
  }

  @Override
  public void ArrayAlloc(Natural newsize) {
    super.ArrayAlloc(newsize);
  }
}