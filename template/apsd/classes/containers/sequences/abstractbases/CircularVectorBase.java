package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;

/**
 * Classe astratta che gestisce la logica di indicizzazione circolare.
 * Implementa i metodi di accesso e manipolazione (Shift) ottimizzati per buffer circolari.
 */
abstract public class CircularVectorBase<Data> extends VectorBase<Data> {

  /** Indice fisico del primo elemento logico del vettore. */
  protected long start = 0L;

  @SuppressWarnings("unchecked")
  @Override
  public void Realloc(Natural n) {
    long sz = Size().ToLong();
    long newCap = n.ToLong();
    Data[] newArr = (Data[]) new Object[(int) newCap];

    // Linearizzazione: Copia elementi nel nuovo array partendo dall'indice 0.
    // Usiamo limit = min(sz, newCap) per supportare sia Grow che Shrink.
    long limit = (sz < newCap) ? sz : newCap;

    if (Capacity().ToLong() > 0 && limit > 0) {
        for (long i = 0; i < limit; i++) {
          // GetAt usa la logica circolare corrente per recuperare l'elemento corretto
          newArr[(int) i] = GetAt(new Natural(i));
        }
    }

    arr = newArr;
    start = 0; // Dopo la riallocazione, il vettore è linearizzato
  }

  @Override
  public Data GetAt(Natural n) {
    long cap = Capacity().ToLong();
    // Prevenzione ArithmeticException e Bounds Check
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

  /**
   * Sposta gli elementi per chiudere un "buco" (rimozione) o compattare.
   * @param startIdx Indice logico da cui inizia la rimozione/sovrascrittura.
   * @param count Numero di posizioni da shiftare (delta).
   */
  @Override
  public void ShiftLeft(Natural startIdx, Natural count) {
    long idx = startIdx.ToLong();
    long delta = count.ToLong();
    long sz = Size().ToLong();
    long cap = Capacity().ToLong();

    // Ottimizzazione Circolare:
    // Se l'indice è nella prima metà, spostiamo la TESTA verso destra.
    // Altrimenti, spostiamo la CODA verso sinistra.
    
    if (idx < sz / 2) {
      // Caso 1: Sposto la testa [0...idx-1] verso DESTRA (fisicamente avanti)
      // Esempio: [A, B, X, D, E] remove X -> A, B avanzano -> [_, A, B, D, E]
      
      // Iteriamo all'indietro per evitare sovrascritture durante lo spostamento a destra
      for (long i = idx - 1; i >= 0; i--) {
        long src = (this.start + i) % cap;
        long dst = (this.start + i + delta) % cap;
        arr[(int) dst] = arr[(int) src];
        arr[(int) src] = null; // Evita memory leaks
      }
      // Aggiorniamo start: la testa è avanzata
      this.start = (this.start + delta) % cap;
      
    } else {
      // Caso 2: Sposto la coda [idx + delta ... size-1] verso SINISTRA (fisicamente indietro)
      // Comportamento standard lineare.
      
      for (long i = idx + delta; i < sz; i++) {
        long src = (this.start + i) % cap;
        // Calcolo dst gestendo il modulo negativo: (src - delta)
        long dst = (this.start + i - delta + cap) % cap;
        arr[(int) dst] = arr[(int) src];
        arr[(int) src] = null; // Pulizia vecchia cella
      }
    }
  }

  /**
   * Sposta gli elementi per creare uno spazio vuoto (inserimento).
   * @param startIdx Indice logico dove creare lo spazio.
   * @param count Numero di posizioni da liberare (delta).
   */
  @Override
  public void ShiftRight(Natural startIdx, Natural count) {
    long idx = startIdx.ToLong();
    long delta = count.ToLong();
    long sz = Size().ToLong();
    long cap = Capacity().ToLong();

    // Ottimizzazione Circolare:
    // Se l'indice è nella prima metà, spostiamo la TESTA verso sinistra.
    // Altrimenti, spostiamo la CODA verso destra.

    if (idx < sz / 2) {
      // Caso 1: Sposto la testa [0...idx-1] verso SINISTRA (fisicamente indietro)
      // Esempio: Insert a pos 1. [A, B, C] -> A va indietro -> [A, _, B, C]
      
      // Copiamo in ordine crescente (0 -> idx-1)
      for (long i = 0; i < idx; i++) {
        long src = (this.start + i) % cap;
        // dst = src - delta
        long dst = (this.start + i - delta + cap) % cap;
        arr[(int) dst] = arr[(int) src];
        // Non serve nullificare src qui, verrà sovrascritto o resterà valido
      }
      // Aggiorniamo start: la testa è arretrata
      this.start = (this.start - delta + cap) % cap;
      
    } else {
      // Caso 2: Sposto la coda [idx ... size-1] verso DESTRA (fisicamente avanti)
      
      // Copiamo all'indietro per evitare sovrascritture
      for (long i = sz - 1; i >= idx; i--) {
        long src = (this.start + i) % cap;
        long dst = (this.start + i + delta) % cap;
        arr[(int) dst] = arr[(int) src];
      }
    }
  }

  @Override
  public void ArrayAlloc(Natural n) {
    super.ArrayAlloc(n);
    start = 0;
  }
}