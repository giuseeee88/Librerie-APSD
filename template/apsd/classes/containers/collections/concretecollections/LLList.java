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

/** Object: Concrete list implementation on linked-list. */
public class LLList<Data> extends LLChainBase<Data> implements List<Data> {

    /* ************************************************************************ */
    /* Costruttori                                                              */
    /* ************************************************************************ */

    public LLList() {
        super(new Vector<Data>());
    }

    public LLList(TraversableContainer<Data> con) {
        super(con);
    }

    protected LLList(long size, LLNode<Data> head, LLNode<Data> tail) {
        super(new Vector<Data>());
        this.size.Assign(new Natural((int) size));
        this.headref.Set(head);
        this.tailref.Set(tail);
    }

    /* ************************************************************************ */
    /* Metodi Factory / Helper                                                  */
    /* ************************************************************************ */

    @Override
    public LLChainBase<Data> NewChain(long capacity, LLNode<Data> head, LLNode<Data> tail) {
        return new LLList<>(capacity, head, tail);
    }

    private LLNode<Data> getNodeAt(long index) {
        if (index < 0 || index >= size.ToLong())
            throw new IndexOutOfBoundsException("Index: " + index);

        Box<LLNode<Data>> cursor = headref;
        for (long i = 0; i < index; i++) {
            cursor = cursor.Get().GetNext();
        }
        return cursor.Get();
    }

    public MutableForwardIterator<Data> NewMutableForwardIterator() {
        return new MutableForwardIterator<Data>() {
            private final MutableForwardIterator<Box<LLNode<Data>>> refIter = FRefIterator();

            @Override
            public boolean IsValid() { return refIter.IsValid(); }

            @Override
            public Data GetCurrent() { return refIter.GetCurrent().Get().Get(); }

            @Override
            public void SetCurrent(Data dat) { refIter.GetCurrent().Get().Set(dat); }

            @Override
            public void Next() { refIter.Next(); }

            @Override
            public void Next(Natural n) { refIter.Next(n); }

            @Override
            public Data DataNNext() {
                Data d = GetCurrent();
                Next();
                return d;
            }

            @Override
            public void Reset() { refIter.Reset(); }
        };
    }

    @Override
    public MutableForwardIterator<Data> FIterator() {
        return NewMutableForwardIterator();
    }

    @Override
    public MutableBackwardIterator<Data> BIterator() {
    	System.out.println("LLList BIterator not supported.");
    	return null;
    }

    /* ************************************************************************ */
    /* Metodi Posizionali (MutableSequence)                                     */
    /* ************************************************************************ */

    @Override
    public void SetAt(Data dat, Natural n) {
        getNodeAt(n.ToLong()).Set(dat);
    }

    @Override
    public Data GetNSetAt(Data dat, Natural n) {
        LLNode<Data> node = getNodeAt(n.ToLong());
        Data old = node.Get();
        node.Set(dat);
        return old;
    }

    @Override
    public void SetFirst(Data dat) {
        if (IsEmpty()) InsertFirst(dat);
        else headref.Get().Set(dat);
    }

    @Override
    public Data GetNSetFirst(Data dat) {
        if (IsEmpty()) throw new IndexOutOfBoundsException();
        Data old = headref.Get().Get();
        headref.Get().Set(dat);
        return old;
    }

    @Override
    public void SetLast(Data dat) {
        if (IsEmpty()) InsertLast(dat);
        else tailref.Get().Set(dat);
    }

    @Override
    public Data GetNSetLast(Data dat) {
        if (IsEmpty()) throw new IndexOutOfBoundsException();
        Data old = tailref.Get().Get();
        tailref.Get().Set(dat);
        return old;
    }

    @Override
    public void Swap(Natural pos1, Natural pos2) {
        long p1 = pos1.ToLong();
        long p2 = pos2.ToLong();
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

    /* ************************************************************************ */
    /* Inserimenti Posizionali (InsertableAtSequence)                           */
    /* ************************************************************************ */

    @Override
    public void InsertAt(Data dat, Natural n) {
        long index = n.ToLong();
        long currentSize = size.ToLong();

        if (index < 0 || index > currentSize) throw new IndexOutOfBoundsException();

        if (index == 0) {
            InsertFirst(dat);
        } else if (index == currentSize) {
            InsertLast(dat);
        } else {
            LLNode<Data> prev = getNodeAt(index - 1);
            LLNode<Data> nextNode = prev.GetNext().Get(); // obtain LLNode<Data> from Box
            LLNode<Data> newNode = new LLNode<>(dat);
            newNode.SetNext(nextNode);
            prev.SetNext(newNode);
            size.Increment();
        }
    }

    @Override
    public void InsertFirst(Data dat) {
        boolean wasEmpty = IsEmpty();
        LLNode<Data> newNode = new LLNode<>(dat);
        // set next to current head (may be null)
        newNode.SetNext(headref.Get());
        headref.Set(newNode);
        if (wasEmpty) {
            tailref.Set(newNode);
        }
        size.Increment();
    }

    @Override
    public void InsertLast(Data dat) {
        LLNode<Data> newNode = new LLNode<>(dat);
        newNode.SetNext(null);
        if (IsEmpty()) {
            headref.Set(newNode);
            tailref.Set(newNode);
        } else {
            tailref.Get().SetNext(newNode);
            tailref.Set(newNode);
        }
        size.Increment();
    }

    /* ************************************************************************ */
    /* Implementazione metodi Chain / List mancanti                             */
    /* ************************************************************************ */

    @Override
    public boolean Insert(Data dat) {
        // Nelle liste, l'inserimento generico avviene in coda
        InsertLast(dat);
        return true;
    }

    @Override
    public Natural Search(Data dat) {
        // Scansione lineare O(N)
        long index = 0;
        MutableForwardIterator<Data> it = FIterator();
        while (it.IsValid()) {
            Data current = it.GetCurrent();
            if ((dat == null && current == null) || (dat != null && dat.equals(current))) {
                return new Natural((int) index);
            }
            it.Next();
            index++;
        }
        return null; // Elemento non trovato
    }

    @Override
    public boolean InsertIfAbsent(Data dat) {
        if (!Exists(dat)) {
            InsertLast(dat);
            return true;
        }
        return false;
    }

    @Override
    public void RemoveOccurrences(Data dat) {
        Filter(element -> (dat == null) ? (element != null) : !dat.equals(element));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Chain<Data> SubChain(Natural start, Natural end) {
        // SubChain è sinonimo di SubSequence in questo contesto
        return (Chain<Data>) SubSequence(start, end);
    }

    @Override
    public List<Data> SubList(Natural start, Natural end) {
        return (List<Data>) SubSequence(start, end);
    }

    /* ************************************************************************ */
    /* Metodi Bulk (InsertAll / RemoveAll)                                      */
    /* ************************************************************************ */

    @Override
    public boolean InsertAll(TraversableContainer<Data> container) {
        if (container == null || container.IsEmpty()) return false;
        // Traversiamo il container e inseriamo tutto in coda
        container.TraverseForward(dat -> {
            InsertLast(dat);
            return false; // Continua traversata
        });
        return true;
    }

    @Override
    public boolean InsertSome(TraversableContainer<Data> container) {
        return InsertAll(container);
    }

    @Override
    public boolean RemoveAll(TraversableContainer<Data> container) {
        if (container == null || container.IsEmpty()) return false;
        // Remove elements that exist in the provided container
        return Filter(dat -> !container.Exists(dat));
    }

    @Override
    public boolean RemoveSome(TraversableContainer<Data> container) {
        // Sinonimo di RemoveAll nel contesto insiemistico standard
        return RemoveAll(container);
    }
}
