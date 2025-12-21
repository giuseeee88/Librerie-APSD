package apsd.classes.containers.collections.abstractcollections;

import apsd.classes.containers.collections.abstractcollections.bases.WSetBase;
import apsd.classes.containers.collections.concretecollections.VList;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.collections.Set;
import apsd.interfaces.containers.iterators.ForwardIterator;

public class WSet<Data> extends WSetBase<Data, VList<Data>> {

    public WSet() {
    	
	}
    
    public WSet(Chain<Data> chn) {
        if (chn != null) {
            ForwardIterator<Data> it = chn.FIterator();
            while (it.IsValid()) { Insert(it.GetCurrent()); it.Next(); }
        }
    }
    
    public WSet(TraversableContainer<Data> con) {
    	if (con != null) InsertAll(con);
	}
    
    public WSet(Chain<Data> chn, TraversableContainer<Data> con) { this(chn); if (con != null) InsertAll(con); }

    @Override
    protected VList<Data> ChainAlloc() { return new VList<>(); }

    @Override
    public boolean Insert(Data dat) {
        if (!chn.Exists(dat)) { chn.Insert(dat); return true; }
        return false;
    }

    @Override
    public void Union(Set<Data> set) {
        if (set == null || set == this) return;
        set.TraverseForward(dat -> { Insert(dat); return false; });
    }

    @Override
    public void Difference(Set<Data> set) {
        if (set == null) return;
        if (set == this) { Clear(); return; }
        set.TraverseForward(dat -> { Remove(dat); return false; });
    }

    @Override
    public void Intersection(Set<Data> set) {
        if (set == null) return;
        if (set == this) return;
        Filter(dat -> set.Exists(dat));
    }

    @Override
    public boolean IsEqual(IterableContainer<Data> container) {
        if (container == null) return false;
        if (this.Size().compareTo(container.Size()) != 0) return false;
        ForwardIterator<Data> it = this.FIterator();
        while (it.IsValid()) {
            Data my = it.GetCurrent();
            boolean found = false;
            if (container instanceof Set) { if (((Set<Data>)container).Exists(my)) found = true; }
            else { 
                ForwardIterator<Data> oth = container.FIterator();
                while (oth.IsValid()) { if (my.equals(oth.GetCurrent())) { found = true; break; } oth.Next(); }
            }
            if (!found) return false;
            it.Next();
        }
        return true;
    }
}