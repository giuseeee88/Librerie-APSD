package apsd.interfaces.containers.base;

/** Interface: IterableContainer con struttura dei dati ordinata. */
/* da controllare */
public interface SortedIterableContainer<Data extends Comparable<? super Data>> extends IterableContainer<Data>{} // Must extends IterableContainer; Data must extend Comparable