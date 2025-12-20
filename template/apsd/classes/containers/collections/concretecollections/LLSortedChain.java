package apsd.classes.containers.collections.concretecollections;

import apsd.classes.containers.collections.concretecollections.bases.LLChainBase;
import apsd.classes.containers.collections.concretecollections.bases.LLNode;
import apsd.classes.containers.sequences.Vector;
import apsd.classes.utilities.Box;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Set;
import apsd.interfaces.containers.collections.SortedChain;
import apsd.interfaces.containers.iterators.ForwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;

public class LLSortedChain<Data extends Comparable<? super Data>> extends LLChainBase<Data> implements SortedChain<Data> {

    public LLSortedChain() { super(new Vector<Data>()); }
    public LLSortedChain(TraversableContainer<Data> con) {
        super(new Vector<Data>());
        if (con != null) { con.TraverseForward(dat -> { Insert(dat); return false; }); }
    }
    protected LLSortedChain(long size, LLNode<Data> head, LLNode<Data> tail) {
        super(new Vector<Data>());
        this.size.Assign(new Natural((int) size));
        this.headref.Set(head);
        this.tailref.Set(tail);
    }

    @Override public LLChainBase<Data> NewChain(long capacity, LLNode<Data> head, LLNode<Data> tail) { return new LLSortedChain<>(capacity, head, tail); }

    // FIX: Safe empty checks
    @Override public Data Min() { if (IsEmpty()) return null; return headref.Get().Get(); }
    @Override public Data Max() { if (IsEmpty()) return null; return tailref.Get().Get(); }

    @Override public void RemoveMin() { if (!IsEmpty()) RemoveFirst(); }
    @Override public void RemoveMax() { if (!IsEmpty()) RemoveLast(); }
    @Override public Data MinNRemove() { return (!IsEmpty()) ? FirstNRemove() : null; }
    @Override public Data MaxNRemove() { return (!IsEmpty()) ? LastNRemove() : null; }

    @Override
    public Natural Search(Data dat) {
        long index = 0;
        MutableForwardIterator<Box<LLNode<Data>>> it = FRefIterator();
        while (it.IsValid()) {
            Data curr = it.GetCurrent().Get().Get();
            int cmp = curr.compareTo(dat);
            if (cmp == 0) return new Natural((int) index);
            if (cmp > 0) return null;
            it.Next(); index++;
        }
        return null;
    }
    @Override public boolean Exists(Data dat) { return Search(dat) != null; }

    @Override
    public boolean Insert(Data dat) {
        LLNode<Data> newNode = new LLNode<>(dat);
        if (IsEmpty() || dat.compareTo(headref.Get().Get()) < 0) {
            newNode.SetNext(headref.Get());
            headref.Set(newNode);
            if (IsEmpty()) tailref.Set(newNode);
            size.Increment();
            return true;
        }
        Box<LLNode<Data>> curBox = headref;
        while (curBox.Get() != null) {
            LLNode<Data> current = curBox.Get();
            LLNode<Data> next = current.GetNext().Get();
            if (next == null || dat.compareTo(next.Get()) < 0) {
                newNode.SetNext(next);
                current.SetNext(newNode);
                if (next == null) tailref.Set(newNode);
                size.Increment();
                return true;
            }
            curBox = current.GetNext();
        }
        return false;
    }

    @Override public boolean InsertIfAbsent(Data dat) { if (!Exists(dat)) return Insert(dat); return false; }
    @Override public Data Predecessor(Data dat) {
        Data prev = null;
        ForwardIterator<Data> it = FIterator();
        while(it.IsValid()) {
            Data curr = it.GetCurrent();
            if (curr.compareTo(dat) >= 0) return prev;
            prev = curr;
            it.Next();
        }
        return prev;
    }
    @Override public Data Successor(Data dat) {
        ForwardIterator<Data> it = FIterator();
        while(it.IsValid()) {
            Data curr = it.GetCurrent();
            if (curr.compareTo(dat) > 0) return curr;
            it.Next();
        }
        return null;
    }
    @Override public void RemovePredecessor(Data dat) { Data p = Predecessor(dat); if (p != null) Remove(p); }
    @Override public void RemoveSuccessor(Data dat) { Data s = Successor(dat); if (s != null) Remove(s); }
    @Override public Data PredecessorNRemove(Data dat) { Data p = Predecessor(dat); if (p != null) { Remove(p); return p; } return null; }
    @Override public Data SuccessorNRemove(Data dat) { Data s = Successor(dat); if (s != null) { Remove(s); return s; } return null; }
    @Override public Natural SearchPredecessor(Data dat) { Data p = Predecessor(dat); return (p != null) ? Search(p) : null; }
    @Override public Natural SearchSuccessor(Data dat) { Data s = Successor(dat); return (s != null) ? Search(s) : null; }
    @Override public void Union(Set<Data> set) { set.TraverseForward(dat -> { InsertIfAbsent(dat); return false; }); }
    @Override public void Difference(Set<Data> set) { set.TraverseForward(dat -> { Remove(dat); return false; }); }
    @Override public void Intersection(Set<Data> set) { Filter(dat -> !set.Exists(dat)); }
    @Override public void RemoveOccurrences(Data dat) { while (Remove(dat)) {}; }
    @Override public SortedChain<Data> SubChain(Natural start, Natural end) { return (SortedChain<Data>) SubSequence(start, end); }
}