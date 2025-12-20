package apsd.classes.containers.collections.abstractcollections.bases;

import apsd.interfaces.containers.collections.Set;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.iterators.BackwardIterator;
import apsd.interfaces.containers.iterators.ForwardIterator;
import apsd.interfaces.traits.Predicate;

/** Object: Abstract wrapper set base implementation via chain. */
public abstract class WSetBase<Data, Chn extends Chain<Data>> implements Set<Data> {

    // La struttura dati interna (Chain) che viene "wrappata"
    protected Chn chn;

    // Metodo astratto per allocare la catena specifica (es. LinearList, DoublyLinkedList)
    protected abstract Chn ChainAlloc();

    /**
     * Costruttore di default.
     * Inizializza la catena interna usando il metodo factory ChainAlloc.
     */
    public WSetBase() {
        this.chn = ChainAlloc();
    }

    /* ************************************************************************ */
    /* Override specific member functions from Container                        */
    /* ************************************************************************ */

    @Override
    public void Clear() {
        chn.Clear();
    }

    @Override
    public boolean IsEmpty() {
        return chn.IsEmpty();
    }

    /* ************************************************************************ */
    /* Override specific member functions from InsertableContainer              */
    /* ************************************************************************ */

    @Override
    public boolean Insert(Data dat) {
        // Logica Set: Inserisci solo se NON esiste già
        if (!chn.Exists(dat)) {
            return chn.Insert(dat);
        }
        return false;
    }

    @Override
    public boolean InsertAll(TraversableContainer<Data> container) {
        // Utilizziamo un array di boolean per tracciare se almeno un inserimento ha avuto successo
        // (necessario perché le lambda non possono modificare variabili locali primitive)
        boolean[] changed = {false};
        
        container.TraverseForward(data -> {
            if (this.Insert(data)) {
                changed[0] = true;
            }
            return true; // Continua l'attraversamento
        });
        
        return changed[0];
    }

    @Override
    public boolean InsertSome(TraversableContainer<Data> container) {
        return InsertAll(container); // Semantica identica per i Set in questo contesto
    }

    /* ************************************************************************ */
    /* Override specific member functions from RemovableContainer               */
    /* ************************************************************************ */

    @Override
    public boolean Remove(Data dat) {
        return chn.Remove(dat);
    }

    @Override
    public boolean RemoveAll(TraversableContainer<Data> container) {
        boolean[] changed = {false};
        
        container.TraverseForward(data -> {
            if (this.Remove(data)) {
                changed[0] = true;
            }
            return true;
        });
        
        return changed[0];
    }

    @Override
    public boolean RemoveSome(TraversableContainer<Data> container) {
        return RemoveAll(container);
    }

    /* ************************************************************************ */
    /* Override specific member functions from IterableContainer                */
    /* ************************************************************************ */

    @Override
    public ForwardIterator<Data> FIterator() {
        return chn.FIterator();
    }

    @Override
    public BackwardIterator<Data> BIterator() {
        return chn.BIterator();
    }

    /* ************************************************************************ */
    /* Override specific member functions from TraversableContainer             */
    /* ************************************************************************ */

    @Override
    public boolean TraverseForward(Predicate<Data> predicate) {
        return chn.TraverseForward(predicate);
    }

    @Override
    public boolean TraverseBackward(Predicate<Data> predicate) {
        return chn.TraverseBackward(predicate);
    }

    /* ************************************************************************ */
    /* Override specific member functions from Collection                       */
    /* ************************************************************************ */

    @Override
    public Natural Size() {
        return chn.Size();
    }

    @Override
    public boolean Exists(Data dat) {
        return chn.Exists(dat);
    }

    /* ************************************************************************ */
    /* Override specific member functions from Set                              */
    /* ************************************************************************ */

    @Override
    public void Union(Set<Data> set) {
        // Unione: Aggiungi tutti gli elementi dell'altro set a questo.
        // La logica di Insert(Data) gestirà i duplicati.
        this.InsertAll(set);
    }

    @Override
    public void Difference(Set<Data> set) {
        // Differenza: Rimuovi da questo set tutti gli elementi presenti nell'altro set.
        this.RemoveAll(set);
    }

    @Override
    public void Intersection(Set<Data> set) {
        // Intersezione: Mantieni solo gli elementi che sono presenti ANCHE nell'altro set.
        // Approccio: Costruiamo una lista temporanea di elementi da rimuovere.
        
        Chn toRemove = ChainAlloc();
        
        // Attraversiamo questo set
        this.TraverseForward(data -> {
            // Se l'elemento NON esiste nell'altro set, va rimosso
            if (!set.Exists(data)) {
                toRemove.Insert(data);
            }
            return true; // continua
        });
        
        // Rimuoviamo gli elementi identificati
        this.RemoveAll(toRemove);
    }

    @Override
    public boolean IsEqual(IterableContainer<Data> set) {
        ForwardIterator<Data> it = set.FIterator();
        while (it.IsValid()) { // Usa IsValid per verificare se ci sono elementi
            Data dataToCheck = it.DataNNext(); // Ottiene il dato e avanza
            
            if (!this.Exists(dataToCheck)) {
                return false; // Trovato un elemento esterno che non possiedo -> Diversi
            }
        }
        
        boolean foundMissing = this.TraverseForward(data -> {
            boolean existsInOther = false;

            // Verifichiamo se 'data' esiste in 'set'
            if (set instanceof Set) {
                existsInOther = ((Set<Data>) set).Exists(data);
            } else {
                // Fallback manuale se 'set' è solo Iterable
                ForwardIterator<Data> otherIt = set.FIterator();
                while (otherIt.IsValid()) {
                    if (otherIt.DataNNext().equals(data)) {
                        existsInOther = true;
                        break;
                    }
                }
            }

            // Se NON esiste nell'altro, ritorniamo true.
            // (TraverseForward si ferma e ritorna true appena trova questo caso)
            return !existsInOther; 
        });

        // Se abbiamo trovato un elemento mancante, i set non sono uguali
        if (foundMissing) return false;

        return true;
    }
}