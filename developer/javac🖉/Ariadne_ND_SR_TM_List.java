/*
  By convention an ND_SR_TM is named after the type of values found in the tape cells.
  However, in this case the name reflects the type that that ND_SR_TM is made from.
  The type of values found in the ND_SR_TM cells is abstracted as "T".

  This implementation uses Java's ListIterator, which lacks a direct
  method to read the current cell value, rather it yields the next
  cell value upon advancing the iterator.
*/
package com.ReasoningTechnology.Ariadne;
import java.util.List;
import java.util.ListIterator;

public class Ariadne_ND_SR_TM_List<T> extends Ariadne_ND_SR_TM{

  // Static methods
  //

  public static <T> Ariadne_ND_SR_TM_List<T> make(List<T> list) {
    return new Ariadne_ND_SR_TM_List<>(list);
  }

  // instance data
  //

  private List<T> list;  // The attached linked list
  private ListIterator<T> iterator;  // Iterator for traversal
  private T read_value;  // Stores the current cell value

  private final TopoIface topo_null = new TopoNull();
  private final TopoIface topo_segment = new TopoSegment();
  private final TopoIface topo_rightmost = new TopoRightmost();

  // constructor(s)
  //

  protected Ariadne_ND_SR_TM_List(List<T> list){
    this.list = list;

    if( list == null || list.isEmpty() ){
      this.iterator = null;
      set_topology(topo_null);
      return;
    }

    this.iterator = list.listIterator();
    read_value = iterator.next();

    if( list.size() == 1 ){
      set_topology(topo_rightmost);
      return;
    }

    set_topology(topo_segment);
  }

  // instance interface implementation
  //

  @Override 
  @SuppressWarnings("unchecked")
  public T read(){
    return (T) current_topology.read(); // Cast to ensure T is returned
  }

  private class TopoNull implements TopoIface{
    @Override public boolean can_read(){
      return false;
    }
    @Override public T read(){
      throw new UnsupportedOperationException( "Cannot read from NULL topo." );
    }
    @Override public boolean can_step(){
      return false;
    }
    @Override public void step(){
      throw new UnsupportedOperationException( "Cannot step over NULL topo." );
    }
    @Override public Topology topology(){
      return Topology.NULL;
    }
  }

  private class TopoSegment implements TopoIface{
    @Override public boolean can_read(){
      return true;
    }
    @Override public T read(){
      return (T)read_value;
    }
    @Override public boolean can_step(){
      return true;
    }
    @Override public void step(){
      read_value = iterator.next();
      if( !iterator.hasNext() ) set_topology(topo_rightmost);
    }
    @Override public Topology topology(){
      return Topology.SEGMENT;
    }
  }

  private class TopoRightmost implements TopoIface{
    @Override public boolean can_read(){
      return true;
    }
    @Override public T read(){
      return read_value;
    }
    @Override public boolean can_step(){
      return false;
    }
    @Override public void step(){
      throw new UnsupportedOperationException( "Cannot step from RIGHTMOST topo." );
    }
    @Override public Topology topology(){
      return Topology.RIGHTMOST;
    }
  }
}


