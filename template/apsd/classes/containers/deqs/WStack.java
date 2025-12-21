package apsd.classes.containers.deqs;

import apsd.classes.containers.collections.concretecollections.VList;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.List;
import apsd.interfaces.containers.deqs.Stack;

public class WStack<Data> implements Stack<Data> {

    protected final List<Data> lst;

    public WStack() {
        this.lst = new VList<Data>();
    }

    public WStack(List<Data> lst) {
        this.lst = lst;
    }

    public WStack(TraversableContainer<Data> con) {
        this.lst = new VList<Data>();
        if (con != null) {
            this.InsertAll(con);
        }
    }

    public WStack(List<Data> lst, TraversableContainer<Data> con) {
        this.lst = lst;
        if (con != null) {
            this.InsertAll(con);
        }
    }

    @Override
    public void Push(Data dat) {
        lst.InsertFirst(dat);
    }

    @Override
    public Data Top() {
        // Fix: Return null if empty to avoid exceptions in "safe" tests
        if (IsEmpty()) {
            return null;
        }
        return lst.GetFirst();
    }

    @Override
    public void Pop() {
        // Fix: No-op if empty
        if (!IsEmpty()) {
            lst.RemoveFirst();
        }
    }

    @Override
    public void SwapTop(Data dat) {
        if (!IsEmpty()) {
            lst.SetFirst(dat);
        } else {
            Push(dat);
        }
    }

    @Override
    public Data TopNSwap(Data dat) {
        if (!IsEmpty()) {
            return lst.GetNSetFirst(dat);
        } else {
            Push(dat);
            return null;
        }
    }

    @Override
    public Data TopNPop() {
        Data d = Top();
        Pop();
        return d;
    }

    @Override
    public void Clear() {
        if (lst != null) {
            lst.Clear();
        }
    }

    @Override
    public Natural Size() {
        return lst.Size();
    }

    @Override
    public boolean IsEmpty() {
        return lst.IsEmpty();
    }

    @Override
    public boolean Insert(Data dat) {
        Push(dat);
        return true;
    }

    @Override
    public boolean InsertAll(TraversableContainer<Data> container) {
        return lst.InsertAll(container);
    }

    @Override
    public boolean InsertSome(TraversableContainer<Data> container) {
        return lst.InsertSome(container);
    }
}
