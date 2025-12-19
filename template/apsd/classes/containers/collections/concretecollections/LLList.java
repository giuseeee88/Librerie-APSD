package apsd.classes.containers.collections.concretecollections;

import apsd.classes.containers.collections.concretecollections.bases.LLChainBase;
import apsd.classes.containers.collections.concretecollections.bases.LLNode;
import apsd.classes.containers.sequences.Vector;
import apsd.classes.utilities.Box;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.collections.List;
import apsd.interfaces.containers.iterators.MutableBackwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
import apsd.interfaces.containers.sequences.MutableSequence;

public class LLList<Data> extends LLChainBase<Data> implements List<Data> {

    public LLList() { super(new Vector<Data>()); }
    public LLList(TraversableContainer<Data> con) { super(con); }
    protected LLList(long size, LLNode<Data> head, LLNode<Data> tail) {
        super(new Vector<Data>());
        this.size.Assign(new Natural((int) size));
        this.headref.Set(head);
        this.tailref.Set(tail);
    }

    @Override public LLChainBase<Data> NewChain(long capacity, LLNode<Data> head, LLNode<Data> tail) { return new LLList<>(capacity, head, tail); }

    private LLNode<Data> getNodeAt(long index) {
        if (index < 0 || index >= size.ToLong()) throw new IndexOutOfBoundsException("Index: " + index);
        Box<LLNode<Data>> cursor = headref;
        for (long i = 0; i < index; i++) cursor = cursor.Get().GetNext();
        return cursor.Get();
    }

    public MutableForwardIterator<Data> NewMutableForwardIterator() {
        return new MutableForwardIterator<Data>() {
            private final MutableForwardIterator<Box<LLNode<Data>>> refIter = FRefIterator();
            @Override public boolean IsValid() { return refIter.IsValid(); }
            @Override public Data GetCurrent() { return refIter.GetCurrent().Get().Get(); }
            @Override public void SetCurrent(Data dat) { refIter.GetCurrent().Get().Set(dat); }
            @Override public void Next() { refIter.Next(); }
            @Override public void Next(Natural n) { refIter.Next(n); }
            @Override public Data DataNNext() { Data d = GetCurrent(); Next(); return d; }
            @Override public void Reset() { refIter.Reset(); }
        };
    }

    @Override public MutableForwardIterator<Data> FIterator() { return NewMutableForwardIterator(); }
    @Override public MutableBackwardIterator<Data> BIterator() { return null; }

    @Override public void SetAt(Data dat, Natural n) { getNodeAt(n.ToLong()).Set(dat); }
    @Override public Data GetNSetAt(Data dat, Natural n) { return getNodeAt(n.ToLong()).GetNSet(dat); }

    @Override
    public void SetFirst(Data dat) {
        if (IsEmpty()) throw new IndexOutOfBoundsException();
        headref.Get().Set(dat);
    }

    @Override
    public Data GetNSetFirst(Data dat) {
        if (IsEmpty()) throw new IndexOutOfBoundsException();
        return headref.Get().GetNSet(dat);
    }

    @Override
    public void SetLast(Data dat) {
        if (IsEmpty()) throw new IndexOutOfBoundsException();
        tailref.Get().Set(dat);
    }

    @Override
    public Data GetNSetLast(Data dat) {
        if (IsEmpty()) throw new IndexOutOfBoundsException();
        return tailref.Get().GetNSet(dat);
    }

    @Override
    public void Swap(Natural pos1, Natural pos2) {
        long p1 = pos1.ToLong(); long p2 = pos2.ToLong();
        if (p1 == p2) return;
        LLNode<Data> n1 = getNodeAt(p1);
        LLNode<Data> n2 = getNodeAt(p2);
        Data temp = n1.Get();
        n1.Set(n2.Get());
        n2.Set(temp);
    }

    @Override
    public MutableSequence<Data> SubSequence(Natural start, Natural end) {
        return (MutableSequence<Data>) super.SubSequence(start, end);
    }

    @Override
    public void InsertAt(Data dat, Natural n) {
        long index = n.ToLong();
        long currentSize = size.ToLong();
        if (index < 0 || index > currentSize) throw new IndexOutOfBoundsException();
        if (index == 0) InsertFirst(dat);
        else if (index == currentSize) InsertLast(dat);
        else {
            LLNode<Data> prev = getNodeAt(index - 1);
            LLNode<Data> newNode = new LLNode<>(dat);
            newNode.SetNext(prev.GetNext().Get());
            prev.SetNext(newNode);
            size.Increment();
        }
    }

    @Override
    public void InsertFirst(Data dat) {
        boolean wasEmpty = IsEmpty();
        LLNode<Data> newNode = new LLNode<>(dat);
        newNode.SetNext(headref.Get());
        headref.Set(newNode);
        if (wasEmpty) tailref.Set(newNode);
        size.Increment();
    }

    @Override
    public void InsertLast(Data dat) {
        LLNode<Data> newNode = new LLNode<>(dat);
        if (IsEmpty()) { headref.Set(newNode); tailref.Set(newNode); }
        else { tailref.Get().SetNext(newNode); tailref.Set(newNode); }
        size.Increment();
    }

    @Override public boolean Insert(Data dat) { InsertLast(dat); return true; }

    @Override
    public Natural Search(Data dat) {
        long index = 0;
        MutableForwardIterator<Data> it = FIterator();
        while (it.IsValid()) {
            Data current = it.GetCurrent();
            if ((dat == null && current == null) || (dat != null && dat.equals(current))) return new Natural((int) index);
            it.Next(); index++;
        }
        return null;
    }

    @Override public boolean InsertIfAbsent(Data dat) { if (!Exists(dat)) { InsertLast(dat); return true; } return false; }
    @Override public void RemoveOccurrences(Data dat) { Filter(element -> (dat == null) ? (element != null) : !dat.equals(element)); }
    @SuppressWarnings("unchecked")
    @Override public Chain<Data> SubChain(Natural start, Natural end) { return (Chain<Data>) SubSequence(start, end); }
    @Override public List<Data> SubList(Natural start, Natural end) { return (List<Data>) SubSequence(start, end); }
    @Override public boolean InsertAll(TraversableContainer<Data> container) {
        if (container == null || container.IsEmpty()) return false;
        container.TraverseForward(dat -> { InsertLast(dat); return false; });
        return true;
    }
    @Override public boolean InsertSome(TraversableContainer<Data> container) { return InsertAll(container); }
    @Override public boolean RemoveAll(TraversableContainer<Data> container) { if (container == null) return false; return Filter(dat -> !container.Exists(dat)); }
    @Override public boolean RemoveSome(TraversableContainer<Data> container) { return RemoveAll(container); }
}