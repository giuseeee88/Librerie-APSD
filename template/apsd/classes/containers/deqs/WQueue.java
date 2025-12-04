package apsd.classes.containers.deqs;

import apsd.classes.containers.collections.concretecollections.VList;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.List;
import apsd.interfaces.containers.deqs.Queue;

/** Object: Wrapper queue implementation. */
public class WQueue<Data> implements Queue<Data> {

    /* ************************************************************************ */
    /* Variabili Membro                                                         */
    /* ************************************************************************ */

    // La lista interna che contiene i dati
    protected final List<Data> lst;

    /* ************************************************************************ */
    /* Costruttori                                                              */
    /* ************************************************************************ */

    // Costruttore di default
    public WQueue() {
        this.lst = new VList<Data>();
    }

    // Dependency Injection
    public WQueue(List<Data> lst) {
        this.lst = lst;
    }

    // Costruttore con copia da Container
    public WQueue(TraversableContainer<Data> con) {
        this.lst = new VList<Data>();
        if (con != null) {
            this.InsertAll(con);
        }
    }

    // Dependency Injection + Copia
    public WQueue(List<Data> lst, TraversableContainer<Data> con) {
        this.lst = lst;
        if (con != null) {
            this.InsertAll(con);
        }
    }

    /* ************************************************************************ */
    /* Override specific member functions from Queue                            */
    /* ************************************************************************ */

    @Override
    public void Enqueue(Data dat) {
        // MAPPING: Enqueue -> InsertLast (Dall'interfaccia Chain)
        // Aggiungiamo in coda alla lista
        lst.InsertLast(dat);
    }

    @Override
    public Data Head() {
        // MAPPING: Head -> First (Dall'interfaccia Sequence)
        // Leggiamo il primo elemento (chi è arrivato prima, esce prima)
        return lst.GetFirst();
    }

    @Override
    public void Dequeue() {
        // MAPPING: Dequeue -> RemoveFirst (o RemoveAt 0)
        // Rimuoviamo dalla testa.
        // Se 'RemoveFirst' non è definito nella tua versione di Chain/List,
        // usa: lst.RemoveAt(new Natural(0));
        lst.RemoveFirst(); 
    }

    @Override
    public Data HeadNDequeue() {
        // Legge e rimuove in un colpo solo
        Data d = Head();
        Dequeue();
        return d;
    }

    /* ************************************************************************ */
    /* Override specific member functions from Container / Clearable            */
    /* ************************************************************************ */

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
        // L'inserimento generico in una Coda corrisponde a una Enqueue
        Enqueue(dat);
        return true;
    }

    @Override
    public boolean InsertAll(TraversableContainer<Data> container) {
        // Delega alla lista (attenzione: mantiene l'ordine del container sorgente)
        return lst.InsertAll(container);
    }

    @Override
    public boolean InsertSome(TraversableContainer<Data> container) {
        return lst.InsertSome(container);
    }
}