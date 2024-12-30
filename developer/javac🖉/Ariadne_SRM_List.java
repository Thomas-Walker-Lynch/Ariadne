/*
Suitable for attaching to a linked list structure. Something that
does not have fast random access ability.

*/


package com.ReasoningTechnology.Ariadne;

import java.util.List;
import java.util.ListIterator;

public class Ariadne_SRMI_List<T> extends Ariadne_SRM<T> {

  private final List<T> list;        // The attached linked list
  private ListIterator<T> iterator;       // Iterator for traversal
  private T currentElement;               // Tracks the current element

  // Factory method to attach SRM to a linked list
  public static <T> Ariadne_SRMI_List<T> attach( List<T> list ){
    return new Ariadne_SRMI_List<>( list );
  }

  // Private constructor to enforce factory usage
  private Ariadne_SRMI_List( List<T> list ){
    if( list == null ){
      throw new IllegalArgumentException( "Ariadne_SRMI_List::list cannot be null" );
    }
    this.list = list;
    this.iterator = list.listIterator();
    this.currentElement = iterator.hasNext() ? iterator.next() : null;
  }

  @Override
  public Topology topology(){
    return list.isEmpty() ? Topology.NO_CELLS : Topology.SEGMENT;
  }

  @Override
  public Status status(){
    if( list.isEmpty() ){
      return Status.TAPE_NOT_MOUNTED;
    }
    if( !iterator.hasPrevious() ){
      return Status.LEFTMOST;
    }
    if( !iterator.hasNext() ){
      return Status.RIGHTMOST;
    }
    return Status.INTERIM;
  }

  @Override
  public T read(){
    if( currentElement == null ){
      throw new UnsupportedOperationException( "Ariadne_SRMI_List::read, no current element." );
    }
    return currentElement;
  }

  @Override
  public boolean step(){
    if( iterator.hasNext() ){
      currentElement = iterator.next();
      return true;
    }
    currentElement = null; // Reached the end
    return false;
  }

  // Optional: Reset to the beginning of the list
  public void reset(){
    this.iterator = list.listIterator();
    this.currentElement = iterator.hasNext() ? iterator.next() : null;
  }

  // Optional: Get the underlying linked list
  public List<T> getList(){
    return list;
  }
}
