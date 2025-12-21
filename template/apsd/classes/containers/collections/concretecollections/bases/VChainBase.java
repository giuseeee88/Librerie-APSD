package apsd.classes.containers.collections.concretecollections.bases;

// *** FIX IMPORTANTE: Importare l'INTERFACCIA ***
import apsd.interfaces.containers.sequences.DynVector; 

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.iterators.BackwardIterator;
import apsd.interfaces.containers.iterators.ForwardIterator;
import apsd.interfaces.containers.sequences.Sequence;
import apsd.interfaces.traits.Predicate;

public class VChainBase<Data> implements Chain<Data> {

    protected DynVector<Data> vec;

    public void NewChain(DynVector<Data> vec) { this.vec = vec; }

    @Override public Natural Size() { return vec.Size(); }
    @Override public boolean IsEmpty() { return vec.IsEmpty(); }
    @Override public void Clear() { vec.Clear(); }
    
    @Override public boolean Remove(Data dat) { 
        Natural idx = vec.Search(dat); 
        if (idx != null) { vec.RemoveAt(idx); return true; } 
        return false; 
    }
    
    @Override public ForwardIterator<Data> FIterator() { return vec.FIterator(); }
    @Override public BackwardIterator<Data> BIterator() { return vec.BIterator(); }
    @Override public boolean IsEqual(IterableContainer<Data> c) { return vec.IsEqual(c); }
    @Override public boolean TraverseForward(Predicate<Data> p) { return vec.TraverseForward(p); }
    @Override public boolean TraverseBackward(Predicate<Data> p) { return vec.TraverseBackward(p); }
    @Override public Data GetAt(Natural n) { return vec.GetAt(n); }
    @Override public Data GetFirst() { return vec.GetFirst(); }
    @Override public Data GetLast() { return vec.GetLast(); }
    @Override public boolean IsInBound(Natural n) { return vec.IsInBound(n); }
    @Override public Sequence<Data> SubSequence(Natural s, Natural e) { return vec.SubVector(s, e); }
    @Override public void RemoveAt(Natural n) { vec.RemoveAt(n); }
    @Override public Data AtNRemove(Natural n) { return vec.AtNRemove(n); }
    @Override public void RemoveFirst() { if (!IsEmpty()) vec.RemoveFirst(); }
    @Override public Data FirstNRemove() { return (!IsEmpty()) ? vec.FirstNRemove() : null; }
    @Override public void RemoveLast() { if (!IsEmpty()) vec.RemoveLast(); }
    @Override public Data LastNRemove() { return (!IsEmpty()) ? vec.LastNRemove() : null; }

    @Override public boolean Filter(Predicate<Data> p) {
        boolean mod = false;
        long sz = Size().ToLong();
        for (long i = sz - 1; i >= 0; i--) {
            Natural idx = new Natural((int)i);
            if (!p.Apply(vec.GetAt(idx))) { vec.RemoveAt(idx); mod = true; }
        }
        return mod;
    }
    
    @Override public boolean Exists(Data dat) { return vec.Exists(dat); }
    
    @Override public boolean InsertIfAbsent(Data dat) { 
        if (!Exists(dat)) { Insert(dat); return true; }
        return false; 
    }
    
    @Override public boolean Insert(Data dat) { return false; }
    @Override public boolean InsertAll(TraversableContainer<Data> c) { 
        if (c == null || c.IsEmpty()) return false; 
        c.TraverseForward(dat -> { Insert(dat); return false; }); 
        return true; 
    }
    @Override public boolean InsertSome(TraversableContainer<Data> c) { return InsertAll(c); }
    @Override public boolean RemoveAll(TraversableContainer<Data> c) { 
        if (c == null || c.IsEmpty()) return false; 
        return Filter(dat -> !c.Exists(dat)); 
    }
    @Override public boolean RemoveSome(TraversableContainer<Data> c) { return RemoveAll(c); }
    @Override public void RemoveOccurrences(Data dat) { Filter(elem -> (dat == null) ? (elem != null) : !dat.equals(elem)); }
    @Override public Chain<Data> SubChain(Natural s, Natural e) { return null; }
    @Override public Natural Search(Data dat) { return vec.Search(dat); }
}