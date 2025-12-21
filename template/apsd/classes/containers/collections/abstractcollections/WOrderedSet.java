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

    @Override
    protected VSortedChain<Data> ChainAlloc() {
        return new VSortedChain<Data>();
    }

    @Override
    public boolean Insert(Data dat) {
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
    public void Difference(Set<Data> set) {
        if (set == null) return;
        ForwardIterator<Data> it = set.FIterator();
        while (it.IsValid()) {
            this.Remove(it.GetCurrent());
            it.Next();
        }
    }

    @Override
    public void Intersection(Set<Data> set) {
        if (set == null) return;
        
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