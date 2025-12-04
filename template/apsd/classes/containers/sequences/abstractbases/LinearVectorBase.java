package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;

/** Object: Abstract (static) linear vector base implementation. */
abstract public class LinearVectorBase<Data> extends VectorBase<Data> {

  @SuppressWarnings("unchecked")
  @Override
  public void Realloc(Natural size) {
    long newCap = size.ToLong();
    // Creiamo il nuovo array manualmente qui per copiare i dati prima di riassegnare 'arr'
    Data[] newArr = (Data[]) new Object[(int) newCap];
    
    if (arr != null) {
      long oldCap = arr.length;
      // Se stiamo espandendo, copiamo tutto il vecchio array (limit = oldCap).
      // Se stiamo riducendo, copiamo solo finché c'è spazio nel nuovo (limit = newCap).
      long limit = (oldCap < newCap) ? oldCap : newCap;
      
      for (int i = 0; i < limit; i++) {
        newArr[i] = arr[i];
      }
    }
    // Aggiorniamo il riferimento nell'oggetto padre VectorBase
    this.arr = newArr;
  }

  @Override
  public Data GetAt(Natural n) {
    // Mappatura lineare diretta: indice N corrisponde alla posizione N nell'array
    return arr[(int) n.ToLong()];
  }

  @Override
  public void SetAt(Data dat, Natural n) {
    // Mappatura lineare diretta
    arr[(int) n.ToLong()] = dat;
  }
}