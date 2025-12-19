package apsd.classes.containers.collections.concretecollections;

import apsd.classes.containers.collections.concretecollections.bases.VChainBase;
import apsd.classes.containers.sequences.DynCircularVector;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.collections.List;
import apsd.interfaces.containers.iterators.MutableBackwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
import apsd.interfaces.containers.sequences.DynVector;
import apsd.interfaces.containers.sequences.MutableSequence;

/** Object: Concrete list implementation on (dynamic circular) vector. */
public class VList<Data> extends VChainBase<Data> implements List<Data> {

    /* ************************************************************************ */
    /* Costruttori                                                              */
    /* ************************************************************************ */

    /** Costruttore di default: crea una lista vuota basata su DynCircularVector. */
    public VList() {
        // Inizializza la base con un vettore circolare dinamico
        NewChain(new DynCircularVector<>());
    }

    /** Costruttore di copia: crea una lista copiando gli elementi da un container. */
    public VList(TraversableContainer<Data> con) {
        NewChain(new DynCircularVector<>(con));
    }

    /** Costruttore protetto: utilizzato per creare sottoliste o wrapper interni. */
    protected VList(DynVector<Data> vec) {
        NewChain(vec);
    }

    /* ************************************************************************ */
    /* Metodi Factory                                                           */
    /* ************************************************************************ */

    // In VChainBase, NewChain è void e setta 'vec'. Qui non serve un factory 
    // abstract come in LLChainBase, usiamo il costruttore protetto per le SubList.

    /* ************************************************************************ */
    /* Override specific member functions from MutableIterableContainer         */
    /* ************************************************************************ */

    @Override
    public MutableForwardIterator<Data> FIterator() {
        // Delega al vettore che supporta iteratori mutabili
        return vec.FIterator();
    }

    @Override
    public MutableBackwardIterator<Data> BIterator() {
        return vec.BIterator();
    }

    /* ************************************************************************ */
    /* Override specific member functions from MutableSequence                  */
    /* ************************************************************************ */

    @Override
    public void SetAt(Data dat, Natural n) {
        vec.SetAt(dat, n);
    }

    @Override
    public Data GetNSetAt(Data dat, Natural n) {
        return vec.GetNSetAt(dat, n);
    }

    @Override
    public void SetFirst(Data dat) {
        vec.SetFirst(dat);
    }

    @Override
    public Data GetNSetFirst(Data dat) {
        return vec.GetNSetFirst(dat);
    }

    @Override
    public void SetLast(Data dat) {
        vec.SetLast(dat);
    }

    @Override
    public Data GetNSetLast(Data dat) {
        return vec.GetNSetLast(dat);
    }

    @Override
    public void Swap(Natural pos1, Natural pos2) {
        vec.Swap(pos1, pos2);
    }

    @Override
    public MutableSequence<Data> SubSequence(Natural start, Natural end) {
        // Ottiene il sottovettore e lo wrappa in una nuova VList
        return new VList<>(vec.SubVector(start, end));
    }

    /* ************************************************************************ */
    /* Override specific member functions from InsertableAtSequence             */
    /* ************************************************************************ */

    @Override
    public void InsertAt(Data dat, Natural n) {
        vec.InsertAt(dat, n);
    }

    @Override
    public void InsertFirst(Data dat) {
        vec.InsertFirst(dat);
    }

    @Override
    public void InsertLast(Data dat) {
        vec.InsertLast(dat);
    }

    /* ************************************************************************ */
    /* Implementazione metodi List                                              */
    /* ************************************************************************ */

    @Override
    public List<Data> SubList(Natural start, Natural end) {
        // Covarianza: SubSequence ritorna MutableSequence, qui specializziamo in List
        return new VList<>(vec.SubVector(start, end));
    }

    @Override
    public boolean Insert(Data dat) {
        // Nelle liste, l'inserimento generico è un append (InsertLast)
        InsertLast(dat);
        return true;
    }

    /* ************************************************************************ */
    /* Implementazione metodi Chain                                             */
    /* ************************************************************************ */

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
        // Rimuove tutte le occorrenze uguali a dat
        // Utilizziamo Filter (implementato in VChainBase)
        Filter(element -> (dat == null) ? (element != null) : !dat.equals(element));
    }

    @Override
    public Chain<Data> SubChain(Natural start, Natural end) {
        // Ritorna una VList che è anche una Chain
        return SubList(start, end);
    }

    @Override
    public Natural Search(Data dat) {
        // Delega al vettore (ricerca lineare O(N) su vettore non ordinato)
        return vec.Search(dat);
    }

    /* ************************************************************************ */
    /* Metodi Bulk (InsertAll / RemoveAll)                                      */
    /* ************************************************************************ */

    @Override
    public boolean InsertAll(TraversableContainer<Data> container) {
        if (container == null || container.IsEmpty()) return false;
        container.TraverseForward(dat -> {
            InsertLast(dat);
            return false;
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
        // Rimuove se presente nel container dato
        return Filter(dat -> !container.Exists(dat));
    }

    @Override
    public boolean RemoveSome(TraversableContainer<Data> container) {
        return RemoveAll(container);
    }
}