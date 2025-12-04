package apsd.classes.containers.deqs;

import apsd.classes.containers.collections.concretecollections.VList;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.List;
import apsd.interfaces.containers.deqs.Stack;

/** Object: Wrapper stack implementation. */
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
        if (con != null) this.InsertAll(con);
    }
    
    public WStack(List<Data> lst, TraversableContainer<Data> con) {
        this.lst = lst;
        if (con != null) this.InsertAll(con);
    }

    // --- IMPLEMENTAZIONE STACK ---

    @Override
    public void Push(Data dat) {
        // Usa InsertFirst dall'interfaccia Chain
        lst.InsertFirst(dat);
    }

    @Override
    public Data Top() {
        // Usa First dall'interfaccia Sequence
        return lst.GetFirst();
    }

    @Override
    public void Pop() {
        // Di solito esiste RemoveFirst in Chain/RemovableSequence.
        // Se ti dà errore "undefined", usa: lst.RemoveAt(new Natural(0));
        lst.RemoveFirst(); 
    }
    
    // --- OTTIMIZZAZIONI CON MUTABLESEQUENCE ---
    
    @Override
    public void SwapTop(Data dat) {
        // Invece di fare Pop + Push, usiamo SetFirst di MutableSequence
        // Sovrascrive direttamente il primo elemento.
        if (!IsEmpty()) {
            lst.SetFirst(dat);
        } else {
            Push(dat); // Se è vuoto, facciamo Push
        }
    }

    @Override
    public Data TopNSwap(Data dat) {
        // Usa GetNSetFirst di MutableSequence: legge il vecchio e scrive il nuovo
        if (!IsEmpty()) {
            return lst.GetNSetFirst(dat);
        } else {
            Push(dat);
            return null; // O gestisci come preferisci il ritorno su stack vuoto
        }
    }
    
    @Override
    public Data TopNPop() {
        Data d = Top();
        Pop();
        return d;
    }

    // --- METODI CONTAINER / LIST ---

    @Override
    public void Clear() {
        if (lst != null) lst.Clear();
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