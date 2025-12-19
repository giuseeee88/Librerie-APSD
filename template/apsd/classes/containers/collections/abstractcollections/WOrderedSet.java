package apsd.classes.containers.collections.abstractcollections;

import apsd.classes.containers.collections.abstractcollections.bases.WOrderedSetBase;
import apsd.classes.containers.collections.concretecollections.VSortedChain;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Set;
import apsd.interfaces.containers.collections.SortedChain;
import apsd.interfaces.containers.iterators.ForwardIterator;

/** Object: Wrapper Ordered Set implementation via Sorted Chain (VSortedChain). */
public class WOrderedSet<Data extends Comparable<? super Data>> extends WOrderedSetBase<Data, VSortedChain<Data>> {

    /* ************************************************************************ */
    /* Costruttori                                                              */
    /* ************************************************************************ */

    public WOrderedSet() {
        super();
    }

    public WOrderedSet(SortedChain<Data> chn) {
        super();
        if (chn != null) {
            ForwardIterator<Data> it = chn.FIterator();
            while (it.IsValid()) { // CORRETTO: era !IsValid()
                this.Insert(it.GetCurrent());
                it.Next();
            }
        }
    }

    public WOrderedSet(TraversableContainer<Data> con) {
        super();
        if (con != null) {
            this.InsertAll(con);
        }
    }

    public WOrderedSet(SortedChain<Data> chn, TraversableContainer<Data> con) {
        this(chn);
        if (con != null) {
            this.InsertAll(con);
        }
    }

    /* ************************************************************************ */
    /* Implementazione ChainAlloc                                               */
    /* ************************************************************************ */

    @Override
    protected VSortedChain<Data> ChainAlloc() {
        return new VSortedChain<Data>();
    }

    /* ************************************************************************ */
    /* Metodi Set / OrderedSet                                                  */
    /* ************************************************************************ */

    @Override
    public boolean Insert(Data dat) {
        // VSortedChain mantiene l'ordine, noi garantiamo l'unicità
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
        while (it.IsValid()) { // CORRETTO
            this.Insert(it.GetCurrent());
            it.Next();
        }
    }

    @Override
    public void Difference(Set<Data> set) {
        if (set == null) return;
        ForwardIterator<Data> it = set.FIterator();
        while (it.IsValid()) { // CORRETTO
            this.Remove(it.GetCurrent());
            it.Next();
        }
    }

    @Override
    public void Intersection(Set<Data> set) {
        if (set == null) return;

        // Usiamo VSortedChain per la lista temporanea (mantiene ordine, efficiente)
        VSortedChain<Data> toRemove = new VSortedChain<>();
        ForwardIterator<Data> it = this.FIterator();

        while (it.IsValid()) {
            if (!set.Exists(it.GetCurrent())) {
                toRemove.Insert(it.GetCurrent());
            }
            it.Next();
        }
        this.RemoveAll(toRemove);
    }

    @Override
    public boolean IsEqual(IterableContainer<Data> container) {
        if (container == null) return false;
        if (this.Size().compareTo(container.Size()) != 0) return false;

        // Poiché WOrderedSet è ordinato, se anche 'container' fosse ordinato,
        // potremmo fare un confronto lineare O(N).
        // Tuttavia, IterableContainer è generico, quindi usiamo il controllo standard.

        ForwardIterator<Data> it = this.FIterator();
        while (it.IsValid()) {
            Data myData = it.GetCurrent();
            boolean found = false;

            if (container instanceof Set) {
                 if (((Set<Data>)container).Exists(myData)) found = true;
            } else {
                ForwardIterator<Data> otherIt = container.FIterator();
                while (otherIt.IsValid()) {
                    if (myData.compareTo(otherIt.GetCurrent()) == 0) {
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