package apsd.classes.containers.collections.concretecollections;

import apsd.classes.containers.collections.concretecollections.bases.VChainBase;
import apsd.classes.containers.sequences.DynCircularVector;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Set;
import apsd.interfaces.containers.collections.SortedChain;
import apsd.interfaces.containers.sequences.DynVector;

/** Object: Concrete sorted chain implementation via (dynamic circular) vector. */
public class VSortedChain<Data extends Comparable<? super Data>> extends VChainBase<Data> implements SortedChain<Data> {

    /* ************************************************************************ */
    /* Costruttori                                                              */
    /* ************************************************************************ */

    public VSortedChain() {
        // Inizializza con un vettore dinamico circolare vuoto
        NewChain(new DynCircularVector<>());
    }

    public VSortedChain(TraversableContainer<Data> con) {
        NewChain(new DynCircularVector<>());
        if (con != null) {
            // Inseriamo gli elementi uno ad uno mantenendo l'ordinamento
            con.TraverseForward(dat -> {
                Insert(dat);
                return false;
            });
        }
    }

    // Costruttore protetto per operazioni interne (es. SubChain)
    protected VSortedChain(DynVector<Data> vec) {
        NewChain(vec);
    }

    /* ************************************************************************ */
    /* Helper Privato: Binary Search                                            */
    /* ************************************************************************ */

    /**
     * Esegue una ricerca binaria sul vettore ordinato.
     * @return l'indice dell'elemento se trovato, altrimenti (-(insertionPoint) - 1)
     */
    private long binarySearch(Data key) {
        long low = 0;
        long high = Size().ToLong() - 1;

        while (low <= high) {
            long mid = (low + high) >>> 1;
            Data midVal = vec.GetAt(Natural.Of(mid));
            int cmp = midVal.compareTo(key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // Trovato
        }
        return -(low + 1);  // Non trovato
    }

    /* ************************************************************************ */
    /* Implementazione Min/Max (O(1) grazie all'ordinamento)                    */
    /* ************************************************************************ */

    @Override
    public Data Min() {
        if (IsEmpty()) throw new IllegalStateException("Chain is empty");
        return vec.GetFirst(); // Il minimo è sempre in testa [cite: 8]
    }

    @Override
    public Data Max() {
        if (IsEmpty()) throw new IllegalStateException("Chain is empty");
        return vec.GetLast(); // Il massimo è sempre in coda [cite: 8]
    }

    @Override
    public void RemoveMin() {
        if (IsEmpty()) throw new IllegalStateException("Chain is empty");
        vec.RemoveFirst();
    }

    @Override
    public void RemoveMax() {
        if (IsEmpty()) throw new IllegalStateException("Chain is empty");
        vec.RemoveLast();
    }

    @Override
    public Data MinNRemove() {
        if (IsEmpty()) throw new IllegalStateException("Chain is empty");
        return vec.FirstNRemove();
    }

    @Override
    public Data MaxNRemove() {
        if (IsEmpty()) throw new IllegalStateException("Chain is empty");
        return vec.LastNRemove();
    }

    /* ************************************************************************ */
    /* Implementazione Predecessore/Successore                                  */
    /* ************************************************************************ */

    @Override
    public Data Predecessor(Data dat) {
        Natural idx = SearchPredecessor(dat);
        return (idx != null) ? vec.GetAt(idx) : null;
    }

    @Override
    public Data Successor(Data dat) {
        Natural idx = SearchSuccessor(dat);
        return (idx != null) ? vec.GetAt(idx) : null;
    }

    @Override
    public Natural SearchPredecessor(Data dat) {
        // Cerchiamo dove si trova dat
        long res = binarySearch(dat);
        long idx;

        if (res >= 0) {
            // Trovato: il predecessore è all'indice precedente
            idx = res - 1;
        } else {
            // Non trovato: res = -(insertionPoint) - 1 -> insertionPoint = -(res + 1)
            // L'insertionPoint è l'indice del primo elemento MAGGIORE di dat.
            // Quindi il predecessore (il maggiore tra i minori) è insertionPoint - 1.
            long insertionPoint = -(res + 1);
            idx = insertionPoint - 1;
        }

        if (idx >= 0 && idx < Size().ToLong()) {
            return Natural.Of(idx);
        }
        return null;
    }

    @Override
    public Natural SearchSuccessor(Data dat) {
        long res = binarySearch(dat);
        long idx;

        if (res >= 0) {
            // Trovato: il successore è all'indice successivo
            idx = res + 1;
        } else {
            // Non trovato: insertionPoint è già l'indice del primo elemento MAGGIORE di dat
            long insertionPoint = -(res + 1);
            idx = insertionPoint;
        }

        if (idx >= 0 && idx < Size().ToLong()) {
            return Natural.Of(idx);
        }
        return null;
    }

    @Override
    public void RemovePredecessor(Data dat) {
        Natural idx = SearchPredecessor(dat);
        if (idx != null) vec.RemoveAt(idx);
    }

    @Override
    public void RemoveSuccessor(Data dat) {
        Natural idx = SearchSuccessor(dat);
        if (idx != null) vec.RemoveAt(idx);
    }

    @Override
    public Data PredecessorNRemove(Data dat) {
        Natural idx = SearchPredecessor(dat);
        return (idx != null) ? vec.AtNRemove(idx) : null;
    }

    @Override
    public Data SuccessorNRemove(Data dat) {
        Natural idx = SearchSuccessor(dat);
        return (idx != null) ? vec.AtNRemove(idx) : null;
    }

    /* ************************************************************************ */
    /* Implementazione Search / Insert / Remove                                 */
    /* ************************************************************************ */

    @Override
    public boolean Exists(Data dat) {
        return Search(dat) != null;
    }

    @Override
    public Natural Search(Data dat) {
        long res = binarySearch(dat);
        return (res >= 0) ? Natural.Of(res) : null;
    }

    @Override
    public boolean Insert(Data dat) {
        // Troviamo la posizione corretta per mantenere l'ordinamento
        long res = binarySearch(dat);
        
        // Se res >= 0, l'elemento esiste già. Poiché è una Chain (non un Set stretto),
        // permettiamo duplicati e li inseriamo accanto all'esistente.
        // Se res < 0, insertPoint = -(res + 1).
        
        long insertIndex = (res >= 0) ? res : -(res + 1);
        
        vec.InsertAt(dat, Natural.Of(insertIndex));
        return true;
    }

    @Override
    public boolean Remove(Data dat) {
        // Sfruttiamo la binary search per trovare l'elemento velocemente
        long res = binarySearch(dat);
        if (res >= 0) {
            vec.RemoveAt(Natural.Of(res));
            return true;
        }
        return false;
    }

    /* ************************************************************************ */
    /* Operazioni Insiemistiche                                                 */
    /* ************************************************************************ */

    @Override
    public void Union(Set<Data> set) {
        // Aggiungiamo tutti gli elementi del set esterno (l'insert gestisce l'ordinamento)
        set.TraverseForward(dat -> {
            Insert(dat); 
            // Nota: Se vogliamo evitare duplicati assoluti (comportamento Set), useremmo InsertIfAbsent
            // Ma VSortedChain estende Chain, quindi tipicamente accetta duplicati.
            // Se deve comportarsi da Set puro: InsertIfAbsent(dat);
            return false;
        });
    }

    @Override
    public void Difference(Set<Data> set) {
        // Rimuoviamo gli elementi presenti nel set dato
        set.TraverseForward(dat -> {
            Remove(dat); 
            return false;
        });
    }

    @Override
    public void Intersection(Set<Data> set) {
        // Manteniamo solo gli elementi che esistono anche nel set dato
        Filter(dat -> !set.Exists(dat)); 
        // Filter in VChainBase rimuove se il predicato è true.
        // Quindi: se !Exists(dat) è true (cioè dat NON è nel set), lo rimuoviamo.
    }

    @Override
    public boolean IsEqual(IterableContainer<Data> container) {
        // Utilizza l'implementazione di base che confronta elemento per elemento in ordine.
        // Poiché entrambe sono sequenze, se sono ordinate uguali, sono uguali.
        return super.IsEqual(container);
    }

    @Override
    public void Clear() {
        vec.Clear();
    }
    
    /* ************************************************************************ */
    /* Metodi Chain                                                             */
    /* ************************************************************************ */

    @Override
    public boolean InsertIfAbsent(Data dat) {
        long res = binarySearch(dat);
        if (res < 0) { // Non trovato
            long insertIndex = -(res + 1);
            vec.InsertAt(dat, Natural.Of(insertIndex));
            return true;
        }
        return false;
    }

    @Override
    public void RemoveOccurrences(Data dat) {
        // Rimuove tutte le copie di dat. Dato che sono ordinate, sono adiacenti.
        // Possiamo ottimizzare trovando la prima occorrenza e rimuovendo finché uguale.
        long res = binarySearch(dat);
        if (res >= 0) {
            // Trovato un elemento. Potrebbero essercene prima o dopo.
            // Strategia semplice: usa Filter, efficiente per rimozioni multiple in VChainBase.
            Filter(elem -> elem.compareTo(dat) != 0); // Rimuovi se == dat
        }
    }

    @Override
    public SortedChain<Data> SubChain(Natural start, Natural end) {
        // SubVector ritorna un DynVector. Lo wrappiamo in un nuovo VSortedChain.
        // È sicuro perché un sotto-vettore di un vettore ordinato è ordinato.
        return new VSortedChain<>(vec.SubVector(start, end));
    }
}