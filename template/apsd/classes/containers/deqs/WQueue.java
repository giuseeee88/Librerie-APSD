package apsd.classes.containers.deqs;

import apsd.classes.containers.collections.concretecollections.VList;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.List;
import apsd.interfaces.containers.deqs.Queue;

public class WQueue<Data> implements Queue<Data> {

    protected final List<Data> lst;

    public WQueue() { this.lst = new VList<Data>(); }
    public WQueue(List<Data> lst) { this.lst = lst; }
    public WQueue(TraversableContainer<Data> con) {
        this.lst = new VList<Data>();
        if (con != null) this.InsertAll(con);
    }
    public WQueue(List<Data> lst, TraversableContainer<Data> con) {
        this.lst = lst;
        if (con != null) this.InsertAll(con);
    }

    @Override
    public void Enqueue(Data dat) {
        lst.InsertLast(dat);
    }

    @Override
    public Data Head() {
        // Fix: Return null se vuoto
        if (IsEmpty()) return null;
        return lst.GetFirst();
    }

    @Override
    public void Dequeue() {
        // Fix: No-op se vuoto
        if (!IsEmpty()) {
            lst.RemoveFirst();
        }
    }

    @Override
    public Data HeadNDequeue() {
        Data d = Head();
        Dequeue();
        return d;
    }

    @Override public void Clear() { if (lst != null) { lst.Clear(); } }
    @Override public Natural Size() { return lst.Size(); }
    @Override public boolean IsEmpty() { return lst.IsEmpty(); }
    @Override public boolean Insert(Data dat) { Enqueue(dat); return true; }
    @Override public boolean InsertAll(TraversableContainer<Data> container) { return lst.InsertAll(container); }
    @Override public boolean InsertSome(TraversableContainer<Data> container) { return lst.InsertSome(container); }
}