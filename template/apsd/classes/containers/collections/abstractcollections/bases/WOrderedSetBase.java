package apsd.classes.containers.collections.abstractcollections.bases;

import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.collections.OrderedSet;
import apsd.interfaces.containers.collections.SortedChain;

/** Object: Abstract wrapper set base implementation via chain. */
public abstract class WOrderedSetBase<Data extends Comparable<? super Data>, Chn extends SortedChain<Data>> extends WSetBase<Data, Chn> implements OrderedSet<Data> {

    @Override
    public Data Min() {
        return chn.Min();
    }

    @Override
    public Data Max() {
        return chn.Max();
    }

    @Override
    public void RemoveMin() {
        chn.RemoveMin();
    }

    @Override
    public void RemoveMax() {
        chn.RemoveMax();
    }

    @Override
    public Data MinNRemove() {
        return chn.MinNRemove();
    }

    @Override
    public Data MaxNRemove() {
        return chn.MaxNRemove();
    }

    @Override
    public Data Predecessor(Data dat) {
        return chn.Predecessor(dat);
    }

    @Override
    public Data Successor(Data dat) {
        return chn.Successor(dat);
    }

    @Override
    public void RemovePredecessor(Data dat) {
        chn.RemovePredecessor(dat);
    }

    @Override
    public void RemoveSuccessor(Data dat) {
        chn.RemoveSuccessor(dat);
    }

    @Override
    public Data PredecessorNRemove(Data dat) {
        return chn.PredecessorNRemove(dat);
    }

    @Override
    public Data SuccessorNRemove(Data dat) {
        return chn.SuccessorNRemove(dat);
    }

    @Override
    protected abstract Chn ChainAlloc();
}