package apsd.classes.containers.collections.concretecollections.bases;

import apsd.classes.utilities.Box;
import apsd.interfaces.traits.MutableReference;
import java.util.Objects;

/** Object: Represents a mutable linked-list node for a value of type Data. */
public class LLNode<Data> implements MutableReference<Data> {

  protected Data dat = null;
  protected Box<LLNode<Data>> next = new Box<>();

  public LLNode() {
	  
  }

  public LLNode(Data dat) {
    this.dat = dat;
  }

  public LLNode(Data dat, LLNode<Data> nextnode) {
    this(dat);
    next.Set(nextnode);
  }

  // Costruttore di copia (Deep Copy ricorsiva)
  public LLNode(LLNode<Data> node) {
    if (node == null) { throw new NullPointerException("LLNode cannot be null!"); }
    this.dat = node.dat;
    // Controllo se esiste un prossimo nodo per evitare NPE e ricorsione infinita su null
    if (node.next.Get() != null) {
        this.next.Set(new LLNode<>(node.next.Get()));
    }
  }

  /* ************************************************************************ */
  /* Specific member functions of LLNode                                      */
  /* ************************************************************************ */

  public Box<LLNode<Data>> GetNext() {
    return next;
  }

  public void SetNext(LLNode<Data> nextnode) {
    next.Set(nextnode);
  }
  
  // Metodo utile per accedere direttamente al dato del prossimo nodo (se esiste)
  public Data GetNextData() {
      if (next.Get() != null) return next.Get().Get();
      return null;
  }

  /* ************************************************************************ */
  /* Override specific member functions from Reference                        */
  /* ************************************************************************ */

  @Override
  public Data Get() {
    return dat;
  }
  
  // Metodo mancante richiesto dall'interfaccia Reference (Pagina 3)
  @Override
  public boolean IsNull() {
      return dat == null;
  }

  /* ************************************************************************ */
  /* Override specific member functions from MutableReference                 */
  /* ************************************************************************ */

  @Override
  public void Set(Data dat) {
    this.dat = dat;
  }
  
  // Metodo mancante richiesto dall'interfaccia MutableReference (Pagina 3)
  @Override
  public Data GetNSet(Data dat) {
      Data old = this.dat;
      this.dat = dat;
      return old;
  }

  /* ************************************************************************ */
  /* Override specific member functions from Object                           */
  /* ************************************************************************ */

  @Override
  public int hashCode() {
    return Objects.hash(dat, next.Get());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof LLNode<?> node)) return false;
    // Confronto shallow del puntatore next (non ricorsivo per evitare stack overflow)
    return (next.Get() == node.next.Get() && Objects.equals(dat, node.dat));
  }

  @Override
  public String toString() {
    // Evitiamo di stampare 'next' ricorsivamente per non intasare il log
    return "LLNode(data: " + dat + ")"; 
  }

}