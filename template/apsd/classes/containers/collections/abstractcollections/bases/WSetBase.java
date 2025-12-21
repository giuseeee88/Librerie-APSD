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

    protected Chn chn;

    protected abstract Chn ChainAlloc();

    public WSetBase() {
        this.chn = ChainAlloc();
    }

    @Override
    public void Clear() {
        chn.Clear();
    }

    @Override
    public boolean IsEmpty() {
        return chn.IsEmpty();
    }

    @Override
    public boolean Insert(Data dat) {
        if (!chn.Exists(dat)) {
            return chn.Insert(dat);
        }
        return false;
    }

    @Override
    public boolean InsertAll(TraversableContainer<Data> container) {
        boolean[] changed = {false};
        
        container.TraverseForward(data -> {
            if (this.Insert(data)) {
                changed[0] = true;
            }
            return true;
        });
        
        return changed[0];
    }

    @Override
    public boolean InsertSome(TraversableContainer<Data> container) {
        return InsertAll(container);
    }

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

    @Override
    public ForwardIterator<Data> FIterator() {
        return chn.FIterator();
    }

    @Override
    public BackwardIterator<Data> BIterator() {
        return chn.BIterator();
    }

    @Override
    public boolean TraverseForward(Predicate<Data> predicate) {
        return chn.TraverseForward(predicate);
    }

    @Override
    public boolean TraverseBackward(Predicate<Data> predicate) {
        return chn.TraverseBackward(predicate);
    }

    @Override
    public Natural Size() {
        return chn.Size();
    }

    @Override
    public boolean Exists(Data dat) {
        return chn.Exists(dat);
    }

    @Override
    public void Union(Set<Data> set) {
        this.InsertAll(set);
    }

    @Override
    public void Difference(Set<Data> set) {
        this.RemoveAll(set);
    }

    @Override
    public void Intersection(Set<Data> set) {
        Chn toRemove = ChainAlloc();
        
        this.TraverseForward(data -> {
            if (!set.Exists(data)) {
                toRemove.Insert(data);
            }
            return true;
        });
        
        this.RemoveAll(toRemove);
    }

    @Override
    public boolean IsEqual(IterableContainer<Data> set) {
        ForwardIterator<Data> it = set.FIterator();
        while (it.IsValid()) {
            Data dataToCheck = it.DataNNext();
            
            if (!this.Exists(dataToCheck)) {
                return false;
            }
        }
        
        boolean foundMissing = this.TraverseForward(data -> {
            boolean existsInOther = false;

            if (set instanceof Set) {
                existsInOther = ((Set<Data>) set).Exists(data);
            } else {
                ForwardIterator<Data> otherIt = set.FIterator();
                while (otherIt.IsValid()) {
                    if (otherIt.DataNNext().equals(data)) {
                        existsInOther = true;
                        break;
                    }
                }
            }

            return !existsInOther; 
        });

        if (foundMissing) return false;

        return true;
    }
}