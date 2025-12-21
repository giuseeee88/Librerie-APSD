package apsd.classes.containers.collections.concretecollections.bases;

import apsd.classes.containers.sequences.Vector;
import apsd.classes.utilities.Box;
import apsd.classes.utilities.MutableNatural;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.iterators.BackwardIterator;
import apsd.interfaces.containers.iterators.ForwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
import apsd.interfaces.containers.sequences.Sequence;
import apsd.interfaces.traits.Predicate;

abstract public class LLChainBase<Data> implements Chain<Data> {

    protected final MutableNatural size = new MutableNatural();
    protected final Box<LLNode<Data>> headref = new Box<>();
    protected final Box<LLNode<Data>> tailref = new Box<>();

    public LLChainBase(TraversableContainer<Data> con) {
        if (con != null) {
            con.TraverseForward(dat -> {
                InsertLast(dat);
                return false;
            });
        }
    }

    abstract public LLChainBase<Data> NewChain(long capacity, LLNode<Data> head, LLNode<Data> tail);

    public MutableForwardIterator<Box<LLNode<Data>>> FRefIterator() {
        return new MutableForwardIterator<Box<LLNode<Data>>>() {
            Box<LLNode<Data>> current = headref;

            @Override
            public boolean IsValid() {
                return current != null && !current.IsNull();
            }

            @Override
            public Box<LLNode<Data>> GetCurrent() {
                if (!IsValid()) {
                    throw new IllegalStateException();
                }
                return current;
            }

            @Override
            public void SetCurrent(Box<LLNode<Data>> dat) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void Next() {
                if (IsValid()) {
                    current = current.Get().GetNext();
                }
            }

            @Override
            public void Next(Natural n) {
                for (long i = 0; i < n.ToLong() && IsValid(); i++) {
                    Next();
                }
            }

            @Override
            public Box<LLNode<Data>> DataNNext() {
                Box<LLNode<Data>> d = GetCurrent();
                Next();
                return d;
            }

            @Override
            public void Reset() {
                current = headref;
            }
        };
    }

    public void InsertLast(Data dat) {
        LLNode<Data> newNode = new LLNode<>(dat);
        if (IsEmpty()) {
            headref.Set(newNode);
            tailref.Set(newNode);
        } else {
            tailref.Get().SetNext(newNode);
            tailref.Set(newNode);
        }
        size.Increment();
    }

    public void InsertFirst(Data dat) {
        LLNode<Data> newNode = new LLNode<>(dat);
        if (IsEmpty()) {
            headref.Set(newNode);
            tailref.Set(newNode);
        } else {
            newNode.SetNext(headref.Get());
            headref.Set(newNode);
        }
        size.Increment();
    }

    protected LLNode<Data> getNodeAt(long index) {
        if (index < 0 || index >= size.ToLong()) return null;
        LLNode<Data> curr = headref.Get();
        for (long i = 0; i < index; i++) curr = curr.GetNext().Get();
        return curr;
    }

    @Override
    public ForwardIterator<Data> FIterator() {
        return new ForwardIterator<Data>() {
            LLNode<Data> current = headref.Get();

            @Override
            public boolean IsValid() {
                return current != null;
            }

            @Override
            public Data GetCurrent() {
                return current.Get();
            }

            @Override
            public void Next() {
                if (current != null) current = current.GetNext().Get();
            }

            @Override
            public void Next(Natural n) {
                for (long i = 0; i < n.ToLong() && current != null; i++) Next();
            }

            @Override
            public Data DataNNext() {
                Data d = GetCurrent();
                Next();
                return d;
            }

            @Override
            public void Reset() {
                current = headref.Get();
            }
        };
    }

    @Override
    public Natural Size() {
        return new Natural(size);
    }

    @Override
    public boolean IsEmpty() {
        return size.IsZero();
    }

    @Override
    public void Clear() {
        headref.Set(null);
        tailref.Set(null);
        size.Zero();
    }

    @Override
    public boolean Remove(Data dat) {
        if (IsEmpty()) return false;
        if ((dat == null && headref.Get().Get() == null) || (dat != null && dat.equals(headref.Get().Get()))) {
            RemoveFirst();
            return true;
        }
        LLNode<Data> curr = headref.Get();
        while (curr.GetNext().Get() != null) {
            Data d = curr.GetNext().Get().Get();
            if ((dat == null && d == null) || (dat != null && dat.equals(d))) {
                LLNode<Data> toRemove = curr.GetNext().Get();
                curr.SetNext(toRemove.GetNext().Get());
                if (tailref.Get() == toRemove) tailref.Set(curr);
                size.Decrement();
                return true;
            }
            curr = curr.GetNext().Get();
        }
        return false;
    }

    @Override
    public void RemoveFirst() {
        if (IsEmpty()) return;
        headref.Set(headref.Get().GetNext().Get());
        size.Decrement();
        if (size.IsZero()) tailref.Set(null);
    }

    @Override
    public void RemoveLast() {
        if (IsEmpty()) return;
        if (size.ToLong() == 1) {
            Clear();
            return;
        }
        LLNode<Data> prev = getNodeAt(size.ToLong() - 2);
        prev.SetNext(null);
        tailref.Set(prev);
        size.Decrement();
    }

    @Override
    public Data FirstNRemove() {
        if (IsEmpty()) return null;
        Data d = GetFirst();
        RemoveFirst();
        return d;
    }

    @Override
    public Data LastNRemove() {
        if (IsEmpty()) return null;
        Data d = GetLast();
        RemoveLast();
        return d;
    }

    @Override
    public Data GetFirst() {
        if (IsEmpty()) throw new IndexOutOfBoundsException();
        return headref.Get().Get();
    }

    @Override
    public Data GetLast() {
        if (IsEmpty()) throw new IndexOutOfBoundsException();
        return tailref.Get().Get();
    }

    @Override
    public BackwardIterator<Data> BIterator() {
        return new Vector<>(this).BIterator();
    }

    @Override
    public boolean IsEqual(IterableContainer<Data> c) {
        if (c == null || Size().compareTo(c.Size()) != 0) return false;
        ForwardIterator<Data> it = FIterator();
        return !c.TraverseForward(d -> !d.equals(it.DataNNext()));
    }

    @Override
    public boolean TraverseForward(Predicate<Data> p) {
        LLNode<Data> c = headref.Get();
        while (c != null) {
            if (p.Apply(c.Get())) return true;
            c = c.GetNext().Get();
        }
        return false;
    }

    @Override
    public boolean TraverseBackward(Predicate<Data> p) {
        return new Vector<>(this).TraverseBackward(p);
    }

    @Override
    public boolean IsInBound(Natural n) {
        return n.ToLong() < size.ToLong();
    }

    @Override
    public Sequence<Data> SubSequence(Natural s, Natural e) {
        return null;
    }

    @Override
    public void RemoveAt(Natural n) {
        AtNRemove(n);
    }

    @Override
    public Data AtNRemove(Natural n) {
        long idx = n.ToLong();
        if (idx < 0 || idx >= size.ToLong()) throw new IndexOutOfBoundsException();
        if (idx == 0) return FirstNRemove();
        LLNode<Data> prev = getNodeAt(idx - 1);
        LLNode<Data> toRemove = prev.GetNext().Get();
        Data d = toRemove.Get();
        prev.SetNext(toRemove.GetNext().Get());
        if (toRemove == tailref.Get()) tailref.Set(prev);
        size.Decrement();
        return d;
    }

    @Override
    public Data GetAt(Natural n) {
        LLNode<Data> node = getNodeAt(n.ToLong());
        if (node == null) throw new IndexOutOfBoundsException();
        return node.Get();
    }

    @Override
    public boolean Filter(Predicate<Data> p) {
        return false;
    }

    @Override
    public boolean Exists(Data dat) {
        LLNode<Data> c = headref.Get();
        while (c != null) {
            Data d = c.Get();
            if ((d == null && dat == null) || (d != null && d.equals(dat))) return true;
            c = c.GetNext().Get();
        }
        return false;
    }

    @Override
    public boolean Insert(Data dat) {
        return false;
    }

    @Override
    public boolean InsertIfAbsent(Data dat) {
        if (!Exists(dat)) return Insert(dat);
        return false;
    }

    @Override
    public boolean InsertAll(TraversableContainer<Data> c) {
        if (c != null) c.TraverseForward(d -> {
            Insert(d);
            return false;
        });
        return true;
    }

    @Override
    public boolean InsertSome(TraversableContainer<Data> c) {
        return InsertAll(c);
    }

    @Override
    public boolean RemoveAll(TraversableContainer<Data> c) {
        return false;
    }

    @Override
    public boolean RemoveSome(TraversableContainer<Data> c) {
        return false;
    }

    @Override
    public void RemoveOccurrences(Data dat) {
    }

    @Override
    public Chain<Data> SubChain(Natural s, Natural e) {
        return null;
    }

    @Override
    public Natural Search(Data dat) {
        long i = 0;
        LLNode<Data> c = headref.Get();
        while (c != null) {
            Data d = c.Get();
            if ((d == null && dat == null) || (d != null && d.equals(dat))) return new Natural((int) i);
            c = c.GetNext().Get();
            i++;
        }
        return null;
    }
}