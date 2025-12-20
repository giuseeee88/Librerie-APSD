package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.sequences.DynVector;

/** Object: Abstract dynamic linear vector base implementation. */
abstract public class DynLinearVectorBase<Data> extends LinearVectorBase<Data> implements DynVector<Data> {

  /** The number of elements currently stored in the vector. */
  protected long size = 0L;

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
  public void Realloc(Natural newsize) {
    // Delega a LinearVectorBase che gestisce la copia lineare degli elementi
    super.Realloc(newsize);
  }

  @Override
  public void Expand(Natural n) {
    // Calcolo la nuova dimensione richiesta
    long requiredSize = size + n.ToLong();
    
    // Se la dimensione richiesta supera la capacità attuale, rialloca
    if (requiredSize > Capacity().ToLong()) {
      Realloc(Natural.Of(requiredSize));
    }
  }

  @Override
  public void Reduce(Natural n) {
    long sz = size;
    long cap = Capacity().ToLong();
    long buffer = n.ToLong();

    // Riduce la capacità solo se c'è un eccesso significativo di memoria allocata.
    // Se la capacità attuale è maggiore della size + buffer, riduciamo.
    if (cap > sz + buffer) {
       Realloc(Natural.Of(sz + buffer));
    }
  }

  @Override
  public void ArrayAlloc(Natural newsize) {
    super.ArrayAlloc(newsize);
  }
  
  // Nota: ShiftLeft e ShiftRight non sono sovrascritti qui.
  // Verrà utilizzata l'implementazione di default dell'interfaccia Vector (o VectorBase)
  // che itera sugli elementi usando GetAt/SetAt, il che è efficiente per vettori lineari.
}