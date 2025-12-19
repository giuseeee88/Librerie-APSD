package apsd.classes.containers.collections.abstractcollections;

import apsd.classes.containers.collections.abstractcollections.bases.WSetBase;
import apsd.classes.containers.collections.concretecollections.VList;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.collections.Set;
import apsd.interfaces.containers.iterators.ForwardIterator;

/** Object: Concrete Wrapper Set implementation via Chain (VList). */
public class WSet<Data> extends WSetBase<Data, VList<Data>> {

    /* ************************************************************************ */
    /* Costruttori                                                              */
    /* ************************************************************************ */

    public WSet() {
        super();
        // ChainAlloc chiamato implicitamente da WSetBase
    }

    public WSet(Chain<Data> chn) {
        super();
        // Copia elementi assicurando unicità
        if (chn != null) {
            ForwardIterator<Data> it = chn.FIterator();
            while (it.IsValid()) { // CORRETTO: era !IsValid()
                this.Insert(it.GetCurrent());
                it.Next();
            }
        }
    }

    public WSet(TraversableContainer<Data> con) {
        super();
        if (con != null) {
            this.InsertAll(con);
        }
    }

    public WSet(Chain<Data> chn, TraversableContainer<Data> con) {
        this(chn);
        if (con != null) {
            this.InsertAll(con);
        }
    }

    /* ************************************************************************ */
    /* Implementazione ChainAlloc                                               */
    /* ************************************************************************ */

    @Override
    protected VList<Data> ChainAlloc() {
        return new VList<Data>();
    }

    /* ************************************************************************ */
    /* Metodi Set (Override per chiarezza o ottimizzazione)                     */
    /* ************************************************************************ */

    @Override
    public boolean Insert(Data dat) {
        // Inseriamo solo se non esiste già
        if (!chn.Exists(dat)) {
            chn.Insert(dat);
            return true;
        }
        return false;
    }

    @Override
    public void Union(Set<Data> set) {
        if (set == null) return;
        ForwardIterator<Data> it = set.FIterator();
        while (it.IsValid()) {
            this.Insert(it.GetCurrent());
            it.Next();
        }
    }

    @Override
    public void Intersection(Set<Data> set) {
        if (set == null) return;
        
        // Costruiamo una lista di elementi da rimuovere (quelli che NON sono in set)
        VList<Data> toRemove = new VList<>();
        ForwardIterator<Data> it = this.FIterator();
        
        while (it.IsValid()) {
            if (!set.Exists(it.GetCurrent())) {
                toRemove.Insert(it.GetCurrent());
            }
            it.Next();
        }
        
        // Rimuoviamo in blocco
        this.RemoveAll(toRemove);
    }

    @Override
    public void Difference(Set<Data> set) {
        if (set == null) return;
        ForwardIterator<Data> it = set.FIterator();
        while (it.IsValid()) {
            this.Remove(it.GetCurrent());
            it.Next();
        }
    }

    @Override
    public boolean IsEqual(IterableContainer<Data> container) {
        if (container == null) return false;
        
        // Ottimizzazione dimensione
        if (this.Size().compareTo(container.Size()) != 0) return false;

        // Verifica contenuto: ogni mio elemento deve esistere nell'altro
        ForwardIterator<Data> it = this.FIterator();
        while (it.IsValid()) {
            Data myData = it.GetCurrent();
            boolean found = false;

            // Se l'altro container è un Set, usiamo Exists (più veloce se ottimizzato)
            if (container instanceof apsd.interfaces.containers.collections.Set) {
                 if (((apsd.interfaces.containers.collections.Set<Data>)container).Exists(myData)) {
                     found = true;
                 }
            } else {
                // Fallback: scansione manuale
                ForwardIterator<Data> otherIt = container.FIterator();
                while (otherIt.IsValid()) {
                    if (myData.equals(otherIt.GetCurrent())) {
                        found = true;
                        break;
                    }
                    otherIt.Next();
                }
            }
            
            if (!found) return false;
            it.Next();
        }
        return true;
    }
}