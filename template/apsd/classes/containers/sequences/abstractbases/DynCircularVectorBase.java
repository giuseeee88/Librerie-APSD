package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.sequences.DynVector;

/** Object: Abstract dynamic circular vector base implementation. */
abstract public class DynCircularVectorBase<Data> extends CircularVectorBase<Data> implements DynVector<Data> {

  /** The number of elements currently stored in the vector. */
  protected long size = 0L;

  @Override
  public Natural Size() {
    return new Natural(size);
  }

  @Override
  public void Clear() {
    this.size = 0;
    this.start = 0;
    super.Clear(); // Pulisce il contenuto dell'array fisico e resetta size nel padre se necessario
  }

  @Override
  public void Realloc(Natural newsize) {
    // Delega alla base che gestisce la linearizzazione e la nuova allocazione
    // Questo resetterà 'start' a 0 e linearizzerà gli elementi.
    super.Realloc(newsize); 
  }

  @Override
  public void Expand(Natural n) {
    long reqSize = size + n.ToLong();
    // Se la dimensione richiesta supera la capacità, riallochiamo.
    if (reqSize > Capacity().ToLong()) {
      Realloc(Natural.Of(reqSize));
    }
  }

  @Override
  public void Reduce(Natural n) {
    // Logica di riduzione (Shrink): se la capacità inutilizzata supera 'n' (margine),
    // ridimensiona il vettore per adattarlo esattamente a Size() + n.
    long sz = size;
    long cap = Capacity().ToLong();
    long buffer = n.ToLong();

    // Se la capacità attuale è maggiore di quella necessaria (Size + buffer)
    if (cap > sz + buffer) {
      Realloc(Natural.Of(sz + buffer));
    }
  }

  @Override
  public void ShiftLeft(Natural start, Natural end) {
    // start: indice logico da cui iniziare la rimozione (o shift)
    // end: numero di posizioni da shiftare (delta/quantità da rimuovere)
    long idx = start.ToLong();
    long delta = end.ToLong();
    long cap = Capacity().ToLong();

    // Ottimizzazione Circolare:
    // Se il punto di modifica è nella prima metà del vettore, conviene spostare
    // gli elementi precedenti (la testa) verso destra (incrementando 'this.start').
    // Altrimenti, si usa lo shift standard della coda verso sinistra.
    
    if (idx < size / 2) {
      // Caso 1: Sposto la testa [0...idx-1] "in avanti" (verso destra) di 'delta' posizioni.
      // Esempio logico: [A, B, X, D, E] -> rimuovo X (pos 2) -> [A, B] vanno in pos 1 e 2.
      // Fisicamente: incrementiamo start.
      
      // Copiamo all'indietro per evitare sovrascritture durante lo spostamento a destra
      for (long i = idx - 1; i >= 0; i--) {
        long src = (this.start + i) % cap;
        long dst = (this.start + i + delta) % cap;
        arr[(int) dst] = arr[(int) src];
        arr[(int) src] = null; // Pulizia per evitare memory leak
      }
      // Aggiorniamo l'indice di start fisico
      this.start = (this.start + delta) % cap;
      
    } else {
      // Caso 2: Sposto la coda [idx + delta ... size-1] verso sinistra (indietro) di 'delta' posizioni.
      // Questo è il comportamento standard dei vettori lineari.
      
      for (long i = idx + delta; i < size; i++) {
        long src = (this.start + i) % cap;
        // dst = src - delta (gestendo il wrap-around negativo aggiungendo cap)
        long dst = (this.start + i - delta + cap) % cap;
        arr[(int) dst] = arr[(int) src];
        arr[(int) src] = null; // Pulizia vecchia posizione
      }
    }
    // Nota: La gestione del decremento di 'size' è demandata al chiamante (es. RemoveAt)
    // o alla logica specifica di DynVector, ma qui operiamo sui dati grezzi.
  }

  @Override
  public void ShiftRight(Natural start, Natural end) {
    // start: indice logico dove creare lo spazio (punto di inserimento)
    // end: numero di posizioni da liberare (delta/quantità spazio)
    long idx = start.ToLong();
    long delta = end.ToLong();
    long cap = Capacity().ToLong();
    // Nota: in ShiftRight assumiamo che Expand sia già stato chiamato e size non sia ancora incrementato ufficialmente,
    // oppure gestiamo i dati esistenti. Usiamo 'size' corrente.

    if (idx < size / 2) {
      // Caso 1: Sposto la testa [0...idx-1] verso sinistra (indietro) di 'delta' posizioni.
      // Decrementiamo start.
      
      // Copiamo in avanti (da 0 a idx-1)
      for (long i = 0; i < idx; i++) {
        long src = (this.start + i) % cap;
        // dst = src - delta
        long dst = (this.start + i - delta + cap) % cap;
        arr[(int) dst] = arr[(int) src];
        // Non serve nullificare qui perché i dati verranno sovrascritti o spostati in zone valide
      }
      // Aggiorniamo start fisico all'indietro
      this.start = (this.start - delta + cap) % cap;
      
    } else {
      // Caso 2: Sposto la coda [idx...size-1] verso destra (avanti) di 'delta' posizioni.
      
      // Copiamo all'indietro (da size-1 a idx) per evitare sovrascritture
      for (long i = size - 1; i >= idx; i--) {
        long src = (this.start + i) % cap;
        long dst = (this.start + i + delta) % cap;
        arr[(int) dst] = arr[(int) src];
      }
    }
  }

  @Override
  public void ArrayAlloc(Natural newsize) {
    super.ArrayAlloc(newsize);
  }
}