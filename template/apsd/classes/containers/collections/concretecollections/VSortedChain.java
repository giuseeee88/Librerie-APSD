package apsd.classes.containers.collections.concretecollections;

import apsd.classes.containers.collections.concretecollections.bases.VChainBase;
import apsd.classes.containers.sequences.DynCircularVector;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Set;
import apsd.interfaces.containers.collections.SortedChain;
import apsd.interfaces.containers.sequences.DynVector;
import java.util.NoSuchElementException;

public class VSortedChain<Data extends Comparable<? super Data>> extends VChainBase<Data> implements SortedChain<Data> {

    public VSortedChain() { NewChain(new DynCircularVector<>()); }
    public VSortedChain(TraversableContainer<Data> con) {
        NewChain(new DynCircularVector<>());
        if (con != null) con.TraverseForward(dat -> { Insert(dat); return false; });
    }
    protected VSortedChain(DynVector<Data> vec) { NewChain(vec); }

    private long binarySearch(Data key) {
        long low = 0;
        long high = Size().ToLong() - 1;
        while (low <= high) {
            long mid = (low + high) >>> 1;
            Data midVal = vec.GetAt(Natural.Of(mid));
            
            // Sicurezza contro null elements nel vettore
            if (midVal == null) {
                // Se troviamo un null in un SortedChain, c'è un problema di integrità.
                // Trattiamolo come minore di key per spingere la ricerca verso destra o skip.
                low = mid + 1; 
                continue;
            }
            
            int cmp = midVal.compareTo(key);
            if (cmp < 0) low = mid + 1;
            else if (cmp > 0) high = mid - 1;
            else return mid;
        }
        return -(low + 1);
    }

    @Override
    public Data Min() {
        if (IsEmpty()) throw new NoSuchElementException("Chain is empty");
        return vec.GetFirst();
    }

    @Override
    public Data Max() {
        if (IsEmpty()) throw new NoSuchElementException("Chain is empty");
        return vec.GetLast();
    }

    @Override
    public void RemoveMin() {
        if (IsEmpty()) throw new NoSuchElementException("Chain is empty");
        vec.RemoveFirst();
    }

    @Override
    public void RemoveMax() {
        if (IsEmpty()) throw new NoSuchElementException("Chain is empty");
        vec.RemoveLast();
    }

    @Override
    public Data MinNRemove() {
        if (IsEmpty()) throw new NoSuchElementException("Chain is empty");
        return vec.FirstNRemove();
    }

    @Override
    public Data MaxNRemove() {
        if (IsEmpty()) throw new NoSuchElementException("Chain is empty");
        return vec.LastNRemove();
    }

    @Override public Data Predecessor(Data dat) { Natural idx = SearchPredecessor(dat); return (idx != null) ? vec.GetAt(idx) : null; }
    @Override public Data Successor(Data dat) { Natural idx = SearchSuccessor(dat); return (idx != null) ? vec.GetAt(idx) : null; }

    @Override
    public Natural SearchPredecessor(Data dat) {
        long res = binarySearch(dat);
        long idx = (res >= 0) ? res - 1 : (-(res + 1)) - 1;
        if (idx >= 0 && idx < Size().ToLong()) return Natural.Of(idx);
        return null;
    }

    @Override
    public Natural SearchSuccessor(Data dat) {
        long res = binarySearch(dat);
        long idx = (res >= 0) ? res + 1 : -(res + 1);
        if (idx >= 0 && idx < Size().ToLong()) return Natural.Of(idx);
        return null;
    }

    @Override public void RemovePredecessor(Data dat) { Natural idx = SearchPredecessor(dat); if (idx != null) vec.RemoveAt(idx); }
    @Override public void RemoveSuccessor(Data dat) { Natural idx = SearchSuccessor(dat); if (idx != null) vec.RemoveAt(idx); }
    @Override public Data PredecessorNRemove(Data dat) { Natural idx = SearchPredecessor(dat); return (idx != null) ? vec.AtNRemove(idx) : null; }
    @Override public Data SuccessorNRemove(Data dat) { Natural idx = SearchSuccessor(dat); return (idx != null) ? vec.AtNRemove(idx) : null; }

    @Override public boolean Exists(Data dat) { return Search(dat) != null; }
    @Override public Natural Search(Data dat) { long res = binarySearch(dat); return (res >= 0) ? Natural.Of(res) : null; }

    @Override
    public boolean Insert(Data dat) {
        long res = binarySearch(dat);
        long insertIndex = (res >= 0) ? res : -(res + 1);
        vec.InsertAt(dat, Natural.Of(insertIndex));
        return true;
    }

    @Override public boolean Remove(Data dat) { long res = binarySearch(dat); if (res >= 0) { vec.RemoveAt(Natural.Of(res)); return true; } return false; }
    @Override public void Union(Set<Data> set) { set.TraverseForward(dat -> { Insert(dat); return false; }); }
    @Override public void Difference(Set<Data> set) { set.TraverseForward(dat -> { Remove(dat); return false; }); }
    @Override public void Intersection(Set<Data> set) { Filter(dat -> !set.Exists(dat)); }
    @Override public boolean IsEqual(IterableContainer<Data> container) { return super.IsEqual(container); }
    @Override public void Clear() { vec.Clear(); }
    @Override public boolean InsertIfAbsent(Data dat) { long res = binarySearch(dat); if (res < 0) { vec.InsertAt(dat, Natural.Of(-(res + 1))); return true; } return false; }
    @Override public void RemoveOccurrences(Data dat) { long res = binarySearch(dat); if (res >= 0) Filter(elem -> elem.compareTo(dat) != 0); }
    @Override public SortedChain<Data> SubChain(Natural start, Natural end) { return new VSortedChain<>(vec.SubVector(start, end)); }
}