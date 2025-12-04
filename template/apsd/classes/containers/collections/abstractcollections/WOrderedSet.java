package apsd.classes.containers.collections.abstractcollections;

import apsd.classes.containers.collections.abstractcollections.bases.WOrderedSetBase;
import apsd.classes.containers.collections.concretecollections.VSortedChain;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.collections.Set;
import apsd.interfaces.containers.collections.SortedChain;
import apsd.interfaces.containers.iterators.ForwardIterator;

/** Object: Wrapper ordered set implementation via ordered chain. */
public class WOrderedSet<Data extends Comparable<? super Data>> extends WOrderedSetBase<Data, VSortedChain<Data>> {

    /* --- Costruttori --- */

    public WOrderedSet() {
        super();
        // ChainAlloc viene chiamato implicitamente
    }

    public WOrderedSet(SortedChain<Data> chn) {
        super();
        // Copiamo gli elementi assicurando l'unicità
        ForwardIterator<Data> it = chn.FIterator();
        while (!it.IsValid()) {
            this.Insert(it.GetCurrent());
            it.Next();
        }
    }

    public WOrderedSet(TraversableContainer<Data> con) {
        super();
        this.InsertAll(con);
    }

    public WOrderedSet(SortedChain<Data> chn, TraversableContainer<Data> con) {
        this(chn);
        this.InsertAll(con);
    }

    /* --- Metodo di Allocazione --- */

    @Override
    protected VSortedChain<Data> ChainAlloc() {
        return new VSortedChain<Data>();
    }

    /* --- Metodi OrderedSet (Delegati alla catena ordinata) --- */

    @Override
    public Data Min() { // Corretto da min() a Min() per matchare Base/Interfaccia
        return chn.Min();
    }

    @Override
    public Data Max() { // Corretto da max() a Max() per matchare Base/Interfaccia
        return chn.Max();
    }

    @Override
    public void RemoveMin() {
        chn.RemoveMin();
    }

    @Override
    public void RemoveMax() {
        chn.RemoveMax();
    }

    @Override
    public Data MinNRemove() {
        return chn.MinNRemove();
    }

    @Override
    public Data MaxNRemove() {
        return chn.MaxNRemove();
    }

    @Override
    public Data Predecessor(Data dat) {
        return chn.Predecessor(dat);
    }

    @Override
    public Data Successor(Data dat) {
        return chn.Successor(dat);
    }

    @Override
    public void RemovePredecessor(Data dat) {
        chn.RemovePredecessor(dat);
    }

    @Override
    public void RemoveSuccessor(Data dat) {
        chn.RemoveSuccessor(dat);
    }

    @Override
    public Data PredecessorNRemove(Data dat) {
        return chn.PredecessorNRemove(dat);
    }

    @Override
    public Data SuccessorNRemove(Data dat) {
        return chn.SuccessorNRemove(dat);
    }

    /* --- Metodi Set (Logica Insiemistica) --- */

    @Override
    public boolean Exists(Data dat) {
        return chn.Exists(dat);
    }

    @Override
    public boolean Insert(Data dat) {
        // Un Set non ammette duplicati. 
        // VSortedChain gestisce l'ordinamento, noi gestiamo l'unicità.
        if (!chn.Exists(dat)) {
            chn.Insert(dat);
            return true;
        }
        return false;
    }

    @Override
    public boolean Remove(Data dat) {
        return chn.Remove(dat);
    }

    @Override
    public void Union(Set<Data> set) {
        // Unione: Aggiungi tutti gli elementi dell'altro set.
        // Insert gestisce già i duplicati.
        ForwardIterator<Data> it = set.FIterator();
        while (!it.IsValid()) {
            this.Insert(it.GetCurrent());
            it.Next();
        }
    }

    @Override
    public void Difference(Set<Data> set) {
        // Differenza: Rimuovi gli elementi presenti nell'altro set.
        ForwardIterator<Data> it = set.FIterator();
        while (!it.IsValid()) {
            this.Remove(it.GetCurrent());
            it.Next();
        }
    }

    @Override
    public void Intersection(Set<Data> set) {
        // Intersezione: Mantieni solo gli elementi comuni.
        // Identifichiamo gli elementi da rimuovere (quelli in THIS ma non in SET).
        VSortedChain<Data> toRemove = new VSortedChain<>();
        ForwardIterator<Data> it = this.FIterator();
        
        while (!it.IsValid()) {
            if (!set.Exists(it.GetCurrent())) {
                toRemove.Insert(it.GetCurrent());
            }
            it.Next();
        }
        
        // Rimuoviamo in blocco per sicurezza
        this.RemoveAll(toRemove);
    }

    @Override
    public boolean IsEqual(IterableContainer<Data> set) {
        // 1. Check Dimensione
        if (this.Size().compareTo(set.Size()) != 0) {
            return false;
        }

        // 2. Check Elementi
        // Poiché è un insieme ordinato, se l'altro container è ordinato potremmo ottimizzare,
        // ma per sicurezza controlliamo l'esistenza generica.
        ForwardIterator<Data> it = this.FIterator();
        while (!it.IsValid()) {
            // Verifica manuale se 'set' non ha un metodo Exists efficiente esposto dall'interfaccia base
            boolean found = false;
            ForwardIterator<Data> otherIt = set.FIterator();
            while(!otherIt.IsValid()){
                if(otherIt.GetCurrent().compareTo(it.GetCurrent()) == 0){
                    found = true;
                    break;
                }
                otherIt.Next();
            }
            
            if(!found) return false;
            it.Next();
        }
        return true;
    }

    @Override
    public void Clear() {
        chn.Clear();
    }
}