/*
  The Ariadne_SRM_List class provides a Step Right Machine (SRM) for linked lists.
  This implementation relies on Java's ListIterator, which lacks a direct method
  to read the current element without advancing the iterator. 

  To address this, the `read` method temporarily moves the iterator back to access 
  the current element, then restores its position. This approach, referred to as the 
  "wiggle" method, is safe under the assumption of single-threaded execution. 

  In multi-threaded environments, external synchronization would be required to 
  ensure the list remains consistent during the wiggle operation.
*/

package com.ReasoningTechnology.Ariadne;

import java.util.List;
import java.util.ListIterator;

public class Ariadne_SRM_List<T> extends Ariadne_SRM<T> {

  private final List<T> list;  // The attached linked list
  private ListIterator<T> iterator;  // Iterator for traversal

  public static <T> Ariadne_SRM_List<T> mount(List<T> list){
    return new Ariadne_SRM_List<>(list);
  }

  protected Ariadne_SRM_List(List<T> list){
    if (list == null){
      throw new IllegalArgumentException("Ariadne_SRM_List::list cannot be null");
    }
    this.list = list;
    this.iterator = list.listIterator();
  }

  @Override
  public Topology topology(){
    return list.isEmpty() ? Topology.NO_CELLS : Topology.SEGMENT;
  }

  @Override
  public Status status(){
    if (list.isEmpty()){
      return Status.TAPE_NOT_MOUNTED;
    }
    if (!iterator.hasPrevious() && iterator.hasNext()){
      return Status.LEFTMOST;
    }
    if (!iterator.hasNext() && iterator.hasPrevious()){
      return Status.RIGHTMOST;
    }
    if (iterator.hasNext() && iterator.hasPrevious()){
      return Status.INTERIM;
    }
    return Status.TAPE_NOT_MOUNTED;  // Fallback, should not occur
  }

  @Override
  public T read(){
    if (status() == Status.TAPE_NOT_MOUNTED){
      throw new UnsupportedOperationException("Ariadne_SRM_List::read, tape not mounted.");
    }
    if (!iterator.hasPrevious()){
      throw new UnsupportedOperationException("Ariadne_SRM_List::read, no cell to read at the leftmost position.");
    }
    // Wiggle: Move back to read the current element, then restore position
    T previous = iterator.previous();
    iterator.next();  // Restore iterator to the current position
    return previous;
  }

  @Override
  public void step(){
    if (!iterator.hasNext()){
      throw new UnsupportedOperationException("Ariadne_SRM_List::step, no next cell.");
    }
    iterator.next();
  }

  // Optional: Reset to the beginning of the list
  public void reset(){
    this.iterator = list.listIterator();
  }

  // Optional: Get the underlying linked list
  public List<T> getList(){
    return list;
  }
}
