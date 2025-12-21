package apsd.classes.containers.collections.concretecollections;

import apsd.classes.containers.collections.concretecollections.bases.VChainBase;
import apsd.classes.containers.sequences.DynCircularVector;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.collections.List;
import apsd.interfaces.containers.iterators.MutableBackwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
// *** FIX CRITICO: Importare l'INTERFACCIA ***
import apsd.interfaces.containers.sequences.DynVector;
import apsd.interfaces.containers.sequences.MutableSequence;

public class VList<Data> extends VChainBase<Data> implements List<Data> {

    public VList() {
        super();
        NewChain(new DynCircularVector<Data>());
    }

    public VList(TraversableContainer<Data> con) {
        super();
        NewChain(new DynCircularVector<Data>(con));
    }

    protected VList(DynVector<Data> vec) {
        super();
        NewChain(vec);
    }

    @Override public MutableForwardIterator<Data> FIterator() { return vec.FIterator(); }
    @Override public MutableBackwardIterator<Data> BIterator() { return vec.BIterator(); }
    @Override public void SetAt(Data dat, Natural n) { vec.SetAt(dat, n); }
    @Override public Data GetNSetAt(Data dat, Natural n) { return vec.GetNSetAt(dat, n); }
    @Override public void SetFirst(Data dat) { if (IsEmpty()) throw new IndexOutOfBoundsException("List is empty"); vec.SetFirst(dat); }
    @Override public Data GetNSetFirst(Data dat) { if (IsEmpty()) throw new IndexOutOfBoundsException("List is empty"); return vec.GetNSetFirst(dat); }
    @Override public void SetLast(Data dat) { if (IsEmpty()) throw new IndexOutOfBoundsException("List is empty"); vec.SetLast(dat); }
    @Override public Data GetNSetLast(Data dat) { if (IsEmpty()) throw new IndexOutOfBoundsException("List is empty"); return vec.GetNSetLast(dat); }
    @Override public void Swap(Natural pos1, Natural pos2) { vec.Swap(pos1, pos2); }
    @Override public MutableSequence<Data> SubSequence(Natural start, Natural end) { return new VList<>(vec.SubVector(start, end)); }
    @Override public void InsertAt(Data dat, Natural n) { vec.InsertAt(dat, n); }
    @Override public void InsertFirst(Data dat) { vec.InsertFirst(dat); }
    @Override public void InsertLast(Data dat) { vec.InsertLast(dat); }
    @Override public List<Data> SubList(Natural start, Natural end) { return new VList<>(vec.SubVector(start, end)); }
    @Override public boolean Insert(Data dat) { InsertLast(dat); return true; }
    @Override public boolean InsertIfAbsent(Data dat) { if (!Exists(dat)) { InsertLast(dat); return true; } return false; }
    @Override public void RemoveOccurrences(Data dat) { Filter(element -> (dat == null) ? (element != null) : !dat.equals(element)); }
    @Override public Chain<Data> SubChain(Natural start, Natural end) { return SubList(start, end); }
    @Override public Natural Search(Data dat) { return vec.Search(dat); }
    @Override public boolean InsertAll(TraversableContainer<Data> c) { if (c == null || c.IsEmpty()) return false; c.TraverseForward(d -> { InsertLast(d); return false; }); return true; }
    @Override public boolean InsertSome(TraversableContainer<Data> c) { return InsertAll(c); }
    @Override public boolean RemoveAll(TraversableContainer<Data> c) { if (c == null || c.IsEmpty()) return false; return Filter(d -> !c.Exists(d)); }
    @Override public boolean RemoveSome(TraversableContainer<Data> c) { return RemoveAll(c); }
}