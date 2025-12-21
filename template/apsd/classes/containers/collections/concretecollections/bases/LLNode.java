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

  public LLNode(LLNode<Data> node) {
    if (node == null) { throw new NullPointerException("LLNode cannot be null!"); }
    this.dat = node.dat;
    if (node.next.Get() != null) {
        this.next.Set(new LLNode<>(node.next.Get()));
    }
  }

  public Box<LLNode<Data>> GetNext() {
    return next;
  }

  public void SetNext(LLNode<Data> nextnode) {
    next.Set(nextnode);
  }
  
  public Data GetNextData() {
      if (next.Get() != null) return next.Get().Get();
      return null;
  }

  @Override
  public Data Get() {
    return dat;
  }
  
  @Override
  public boolean IsNull() {
      return dat == null;
  }

  @Override
  public void Set(Data dat) {
    this.dat = dat;
  }
  
  @Override
  public Data GetNSet(Data dat) {
      Data old = this.dat;
      this.dat = dat;
      return old;
  }

  @Override
  public int hashCode() {
    return Objects.hash(dat, next.Get());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof LLNode<?> node)) return false;
    return (next.Get() == node.next.Get() && Objects.equals(dat, node.dat));
  }

  @Override
  public String toString() {
    return "LLNode(data: " + dat + ")"; 
  }

}