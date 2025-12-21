package apsd.classes.containers.collections.concretecollections;

import apsd.classes.containers.collections.concretecollections.bases.LLChainBase;
import apsd.classes.containers.collections.concretecollections.bases.LLNode;
import apsd.classes.containers.sequences.Vector;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.collections.List;
import apsd.interfaces.containers.iterators.MutableBackwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
import apsd.interfaces.containers.sequences.MutableSequence;

public class LLList<Data> extends LLChainBase<Data> implements List<Data> {

    public LLList() { super(null); }
    public LLList(TraversableContainer<Data> con) { super(con); }
    
    // Costruttore interno per SubList
    protected LLList(long size, LLNode<Data> head, LLNode<Data> tail) {
        super(null);
        this.size.Assign(new Natural((int) size));
        this.headref.Set(head);
        this.tailref.Set(tail);
    }

    @Override public LLChainBase<Data> NewChain(long capacity, LLNode<Data> head, LLNode<Data> tail) { 
        return new LLList<>(capacity, head, tail); 
    }

    @Override public MutableForwardIterator<Data> FIterator() {
        return new MutableForwardIterator<Data>() {
            LLNode<Data> current = headref.Get();
            @Override public boolean IsValid() { return current != null; }
            @Override public Data GetCurrent() { return current.Get(); }
            @Override public void SetCurrent(Data dat) { if(current!=null) current.Set(dat); }
            @Override public void Next() { if(current!=null) current = current.GetNext().Get(); }
            @Override public void Next(Natural n) { for(long i=0; i<n.ToLong() && current!=null; i++) Next(); }
            @Override public Data DataNNext() { Data d = GetCurrent(); Next(); return d; }
            @Override public void Reset() { current = headref.Get(); }
        };
    }

    @Override public MutableBackwardIterator<Data> BIterator() { return null; }

    @Override public void SetAt(Data dat, Natural n) { 
        LLNode<Data> node = getNodeAt(n.ToLong());
        if(node == null) throw new IndexOutOfBoundsException();
        node.Set(dat);
    }

    @Override public Data GetNSetAt(Data dat, Natural n) { 
        LLNode<Data> node = getNodeAt(n.ToLong());
        if(node == null) throw new IndexOutOfBoundsException();
        return node.GetNSet(dat);
    }

    @Override public void SetFirst(Data dat) { 
        if(IsEmpty()) throw new IndexOutOfBoundsException(); 
        headref.Get().Set(dat); 
    }
    @Override public Data GetNSetFirst(Data dat) { 
        if(IsEmpty()) throw new IndexOutOfBoundsException(); 
        return headref.Get().GetNSet(dat); 
    }
    @Override public void SetLast(Data dat) { 
        if(IsEmpty()) throw new IndexOutOfBoundsException(); 
        tailref.Get().Set(dat); 
    }
    @Override public Data GetNSetLast(Data dat) { 
        if(IsEmpty()) throw new IndexOutOfBoundsException(); 
        return tailref.Get().GetNSet(dat); 
    }

    @Override public void Swap(Natural pos1, Natural pos2) { 
        LLNode<Data> n1 = getNodeAt(pos1.ToLong());
        LLNode<Data> n2 = getNodeAt(pos2.ToLong());
        if(n1==null || n2==null) throw new IndexOutOfBoundsException();
        Data tmp = n1.Get(); n1.Set(n2.Get()); n2.Set(tmp);
    }

    @Override public MutableSequence<Data> SubSequence(Natural start, Natural end) { return SubList(start, end); }

    @Override public void InsertAt(Data dat, Natural n) {
        long idx = n.ToLong();
        if(idx < 0 || idx > size.ToLong()) throw new IndexOutOfBoundsException();
        if(idx == 0) InsertFirst(dat);
        else if(idx == size.ToLong()) InsertLast(dat);
        else {
            LLNode<Data> prev = getNodeAt(idx-1);
            LLNode<Data> newNode = new LLNode<>(dat);
            newNode.SetNext(prev.GetNext().Get());
            prev.SetNext(newNode);
            size.Increment();
        }
    }

    @Override public List<Data> SubList(Natural start, Natural end) {
        // Implementazione semplificata: crea nuova lista e copia
        LLList<Data> sub = new LLList<>();
        long s = start.ToLong();
        long e = end.ToLong();
        if(s<0 || e>size.ToLong() || s>e) throw new IndexOutOfBoundsException();
        LLNode<Data> curr = getNodeAt(s);
        for(long i=0; i < (e-s); i++) {
            sub.InsertLast(curr.Get());
            curr = curr.GetNext().Get();
        }
        return sub;
    }

    @Override public boolean Insert(Data dat) { InsertLast(dat); return true; }
    
    @Override public boolean InsertIfAbsent(Data dat) { 
        if(!Exists(dat)) { InsertLast(dat); return true; } 
        return false; 
    }
    
    @Override public void RemoveOccurrences(Data dat) { 
        // Implementato in LLChainBase via Remove
        while(Remove(dat));
    }
    
    @Override public Chain<Data> SubChain(Natural s, Natural e) { return SubList(s, e); }
}