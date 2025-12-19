package apsd.classes.containers.collections.concretecollections;

import apsd.classes.containers.collections.concretecollections.bases.LLChainBase;
import apsd.classes.containers.collections.concretecollections.bases.LLNode;
import apsd.classes.containers.sequences.Vector;
import apsd.classes.utilities.Box;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Set;
import apsd.interfaces.containers.collections.SortedChain;
import apsd.interfaces.containers.iterators.ForwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
import apsd.interfaces.containers.sequences.Sequence;

/**
 * Object: Concrete sorted chain implementation on linked-list.
 * Mantains elements sorted in ascending order.
 */
public class LLSortedChain<Data extends Comparable<? super Data>> extends LLChainBase<Data> implements SortedChain<Data> {

    /* ************************************************************************ */
    /* Costruttori                                                              */
    /* ************************************************************************ */

    public LLSortedChain() {
        super(new Vector<Data>());
    }

    public LLSortedChain(TraversableContainer<Data> con) {
        super(new Vector<Data>()); // Inizializza vuoto
        if (con != null) {
            con.TraverseForward(dat -> {
                Insert(dat); // Usa Insert ordinato
                return false;
            });
        }
    }

    // Costruttore protetto per SubSequence / NewChain
    protected LLSortedChain(long size, LLNode<Data> head, LLNode<Data> tail) {
        super(new Vector<Data>()); // Dummy call
        this.size.Assign(new Natural((int) size));
        this.headref.Set(head);
        this.tailref.Set(tail);
    }

    /* ************************************************************************ */
    /* Metodi Factory                                                           */
    /* ************************************************************************ */

    @Override
    public LLChainBase<Data> NewChain(long capacity, LLNode<Data> head, LLNode<Data> tail) {
        return new LLSortedChain<>(capacity, head, tail);
    }

    /* ************************************************************************ */
    /* Implementazione SortedChain / OrderedSet                                 */
    /* ************************************************************************ */

    @Override
    public Data Min() {
        if (IsEmpty()) throw new IllegalStateException("Chain is empty");
        return headref.Get().Get(); // In una lista ordinata, il minimo è la testa
    }

    @Override
    public Data Max() {
        if (IsEmpty()) throw new IllegalStateException("Chain is empty");
        return tailref.Get().Get(); // Il massimo è la coda
    }

    @Override
    public void RemoveMin() {
        if (IsEmpty()) throw new IllegalStateException("Chain is empty");
        RemoveFirst();
    }

    @Override
    public void RemoveMax() {
        if (IsEmpty()) throw new IllegalStateException("Chain is empty");
        RemoveLast();
    }

    @Override
    public Data MinNRemove() {
        if (IsEmpty()) throw new IllegalStateException("Chain is empty");
        return FirstNRemove();
    }

    @Override
    public Data MaxNRemove() {
        if (IsEmpty()) throw new IllegalStateException("Chain is empty");
        return LastNRemove();
    }

    @Override
    public Natural Search(Data dat) {
        // Ricerca ottimizzata: si ferma se trova un elemento maggiore (dato che è ordinata)
        long index = 0;
        MutableForwardIterator<Box<LLNode<Data>>> it = FRefIterator();
        
        while (it.IsValid()) {
            Data curr = it.GetCurrent().Get().Get();
            int cmp = curr.compareTo(dat);
            
            if (cmp == 0) return new Natural((int) index); // Trovato
            if (cmp > 0) return null; // Elemento corrente > cercato -> non esiste (lista ordinata)
            
            it.Next();
            index++;
        }
        return null;
    }

    @Override
    public boolean Exists(Data dat) {
        return Search(dat) != null;
    }

    /* ************************************************************************ */
    /* Inserimento Ordinato                                                     */
    /* ************************************************************************ */

    @Override
    public boolean Insert(Data dat) {
        // Inserimento ordinato: trova il punto giusto e inserisce
        LLNode<Data> newNode = new LLNode<>(dat);
        
        // Caso 0: Lista vuota o inserimento in testa (dat < head)
        if (IsEmpty() || dat.compareTo(headref.Get().Get()) < 0) {
            newNode.SetNext(headref.Get());
            headref.Set(newNode);
            if (IsEmpty()) tailref.Set(newNode); // Se era vuota, anche tail punta qui
            size.Increment();
            return true;
        }

        // Caso 1: Inserimento nel mezzo o in coda
        Box<LLNode<Data>> curBox = headref;
        while (curBox.Get() != null) {
            LLNode<Data> current = curBox.Get();
            LLNode<Data> next = current.GetNext().Get();
            
            // Se siamo alla fine o il prossimo è maggiore, inseriamo qui
            if (next == null || dat.compareTo(next.Get()) < 0) {
                newNode.SetNext(next);
                current.SetNext(newNode);
                
                if (next == null) tailref.Set(newNode); // Aggiorna tail se inserito in fondo
                size.Increment();
                return true;
            }
            curBox = current.GetNext(); // Avanza
        }
        return false; // Should not reach here
    }

    @Override
    public boolean InsertIfAbsent(Data dat) {
         if (!Exists(dat)) return Insert(dat);
         return false;
    }

    /* ************************************************************************ */
    /* Predecessori e Successori                                                */
    /* ************************************************************************ */

    @Override
    public Data Predecessor(Data dat) {
        // Cerca il nodo con valore < dat più vicino (cioè l'elemento subito prima di Search(dat) o di dove sarebbe dat)
        Data prev = null;
        ForwardIterator<Data> it = FIterator();
        while(it.IsValid()) {
            Data curr = it.GetCurrent();
            if (curr.compareTo(dat) >= 0) return prev; // Appena superiamo o eguagliamo, ritorniamo il precedente
            prev = curr;
            it.Next();
        }
        return prev; // Se finisce la lista e sono tutti minori, l'ultimo è il predecessore
    }

    @Override
    public Data Successor(Data dat) {
        // Cerca il primo nodo con valore > dat
        ForwardIterator<Data> it = FIterator();
        while(it.IsValid()) {
            Data curr = it.GetCurrent();
            if (curr.compareTo(dat) > 0) return curr;
            it.Next();
        }
        return null;
    }

    // Metodi di rimozione predecessore/successore (opzionali o delegati alla rimozione per valore)
    @Override
    public void RemovePredecessor(Data dat) {
        Data p = Predecessor(dat);
        if (p != null) Remove(p);
    }

    @Override
    public void RemoveSuccessor(Data dat) {
        Data s = Successor(dat);
        if (s != null) Remove(s);
    }

    @Override
    public Data PredecessorNRemove(Data dat) {
        Data p = Predecessor(dat);
        if (p != null) { Remove(p); return p; }
        return null;
    }

    @Override
    public Data SuccessorNRemove(Data dat) {
        Data s = Successor(dat);
        if (s != null) { Remove(s); return s; }
        return null;
    }
    
    @Override
    public Natural SearchPredecessor(Data dat) {
        Data p = Predecessor(dat);
        return (p != null) ? Search(p) : null;
    }

    @Override
    public Natural SearchSuccessor(Data dat) {
         Data s = Successor(dat);
         return (s != null) ? Search(s) : null;
    }

    /* ************************************************************************ */
    /* Operazioni Insiemistiche (Set)                                           */
    /* ************************************************************************ */
    
    @Override
    public void Union(Set<Data> set) {
        set.TraverseForward(dat -> {
            InsertIfAbsent(dat);
            return false;
        });
    }

    @Override
    public void Difference(Set<Data> set) {
        set.TraverseForward(dat -> {
            Remove(dat);
            return false;
        });
    }

    @Override
    public void Intersection(Set<Data> set) {
        // Rimuovi elementi che NON sono nel set
        Filter(dat -> !set.Exists(dat)); 
        // Nota: Filter rimuove se il predicato è true. Quindi qui rimuove se !Exists
    }
    
    @Override
    public void RemoveOccurrences(Data dat) {
        // In un SortedSet (se inteso come Set matematico) non ci sono duplicati.
        // Se invece permette duplicati (SortedChain), rimuovi tutti quelli uguali.
        while (Remove(dat)) {};
    }
    
    @Override
    public SortedChain<Data> SubChain(Natural start, Natural end) {
         return (SortedChain<Data>) SubSequence(start, end);
    }

    // I metodi base Remove(Data), Clear(), Size() sono ereditati da LLChainBase e funzionano correttamente.
}