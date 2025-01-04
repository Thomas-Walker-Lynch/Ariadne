/*
  The Ariadne_SRM_List class provides a Step Right Machine (SRM) for linked lists.
  This implementation uses Java's ListIterator, which lacks a direct method
  to read the current element without advancing the iterator.

*/

package com.ReasoningTechnology.Ariadne;

import java.util.List;
import java.util.ListIterator;

public class Ariadne_SRM_List<TElement> extends Ariadne_SRM<TElement> {

  public static <TElement> Ariadne_SRM_List<TElement> make(List<TElement> list){
    return new Ariadne_SRM_List<>(list);
  }

  private final List<TElement> _list;  // The attached linked list
  private ListIterator<TElement> iterator;  // Iterator for traversal
  private TElement read_value;  // Stores the current cell value

  private Topology _topology;
  private Location _location;

  // Protected constructor for controlled instantiation
  protected Ariadne_SRM_List(List<TElement> list){
    _list = list;
    
    if( _list == null || _list.isEmpty() ) 
      _topology = Topology.NO_CELLS;
    else if( _list.size() == 1 ) 
      _topology = Topology.SINGLETON;
    else
      _topology = Topology.SEGMENT;

    if(_topology == Topology.SEGMENT)
      _location = Location.LEFTMOST;
    else
      _location = Location.OTHER;

    if(_topology >= SINGLETON){
      iterator = _list.listIterator();
      read_value = iterator.next();
    }
  }

  @Override
  public Topology topology(){
    return _topology;
  }

  @Override
  public Location location(){
    return _location;
  }

  @Override
  public TElement access(){
    if( can_read() ) return read_value;
    throw new UnsupportedOperationException("Ariadne_SRM_List::access can not read tape.");
  }

  @Override
  public void step(){
    if( can_step() ){
      read_value = iterator.next();  // Move to the next cell and update current value
      if( !iterator.has_next() ) _location = Location.RIGHTMOST;
      return;
    }
    throw new UnsupportedOperationException("Ariadne_SRM_List::step can not step.");
  }


}
