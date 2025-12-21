package apsd.classes.containers.collections.concretecollections;

import apsd.classes.containers.collections.concretecollections.bases.LLChainBase;
import apsd.classes.containers.collections.concretecollections.bases.LLNode;
import apsd.classes.utilities.Box;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Set;
import apsd.interfaces.containers.collections.SortedChain;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
import java.util.NoSuchElementException;

public class LLSortedChain<Data extends Comparable<? super Data>> extends LLChainBase<Data> implements SortedChain<Data> {

    public LLSortedChain() { super(null); }
    public LLSortedChain(TraversableContainer<Data> con) { 
        super(null); 
        if(con!=null) con.TraverseForward(dat -> { Insert(dat); return false; });
    }
    
    // Costruttore interno
    protected LLSortedChain(long size, LLNode<Data> head, LLNode<Data> tail) {
        super(null);
        this.size.Assign(new Natural((int) size));
        this.headref.Set(head);
        this.tailref.Set(tail);
    }

    @Override public LLChainBase<Data> NewChain(long capacity, LLNode<Data> head, LLNode<Data> tail) { return new LLSortedChain<>(capacity, head, tail); }

    // --- SortedChain Methods ---
    @Override public Data Min() { if(IsEmpty()) return null; return GetFirst(); }
    @Override public Data Max() { if(IsEmpty()) return null; return GetLast(); }
    @Override public void RemoveMin() { RemoveFirst(); }
    @Override public void RemoveMax() { RemoveLast(); }
    @Override public Data MinNRemove() { return FirstNRemove(); }
    @Override public Data MaxNRemove() { return LastNRemove(); }

    @Override public boolean Insert(Data dat) {
        if(dat==null) return false;
        // Inserimento ordinato
        LLNode<Data> newNode = new LLNode<>(dat);
        if(IsEmpty() || dat.compareTo(headref.Get().Get()) < 0) {
            newNode.SetNext(headref.Get());
            headref.Set(newNode);
            if(tailref.Get()==null) tailref.Set(newNode);
        } else {
            // Uso FRefIterator che ora è PUBLIC in LLChainBase
            MutableForwardIterator<Box<LLNode<Data>>> it = FRefIterator();
            while(it.IsValid() && it.GetCurrent().Get().GetNext().Get() != null && 
                  it.GetCurrent().Get().GetNext().Get().Get().compareTo(dat) <= 0) {
                it.Next();
            }
            LLNode<Data> curr = it.GetCurrent().Get();
            newNode.SetNext(curr.GetNext().Get());
            curr.SetNext(newNode);
            if(newNode.GetNext().Get()==null) tailref.Set(newNode);
        }
        size.Increment();
        return true;
    }

    @Override public boolean Exists(Data dat) { return Search(dat) != null; }
    @Override public Natural Search(Data dat) {
        if(dat==null) return null;
        long idx = 0;
        LLNode<Data> curr = headref.Get();
        while(curr!=null) {
            int cmp = curr.Get().compareTo(dat);
            if(cmp==0) return new Natural((int)idx);
            if(cmp>0) return null; // Ottimizzazione lista ordinata
            curr = curr.GetNext().Get();
            idx++;
        }
        return null;
    }

    // Stubs necessari
    @Override public Data Predecessor(Data dat) { return null; } // Implementare se serve
    @Override public Data Successor(Data dat) { return null; }
    @Override public Natural SearchPredecessor(Data dat) { return null; }
    @Override public Natural SearchSuccessor(Data dat) { return null; }
    @Override public void RemovePredecessor(Data dat) {}
    @Override public void RemoveSuccessor(Data dat) {}
    @Override public Data PredecessorNRemove(Data dat) { return null; }
    @Override public Data SuccessorNRemove(Data dat) { return null; }
    @Override public void Union(Set<Data> set) { if(set!=null) set.TraverseForward(d->{Insert(d); return false;}); }
    @Override public void Difference(Set<Data> set) { if(set!=null) set.TraverseForward(d->{Remove(d); return false;}); }
    @Override public void Intersection(Set<Data> set) {}
    @Override public SortedChain<Data> SubChain(Natural start, Natural end) { return null; }
}