/*
  The Ariadne_SRTM_List class provides a Step Right Tape Machine (SRTMT) for linked lists.
  This implementation uses Java's ListIterator, which lacks a direct method
  to read the current element without advancing the iterator.

*/
package com.ReasoningTechnology.Ariadne;
import java.util.List;
import java.util.ListIterator;

public class Ariadne_SRTM_List extends Ariadne_SRTM{

  // Static methods
  public static  Ariadne_SRTM_List make(List list){
    return new Ariadne_SRTM_List(list);
  }

  private List list;  // The attached linked list
  private ListIterator iterator;  // Iterator for traversal
  private Object read_value;  // Stores the current cell value

  private final TopoIface topo_null = new TopoNull();
  private final TopoIface topo_segment = new TopoSegment();
  private final TopoIface topo_rightmost = new TopoRightmost();

  protected Ariadne_SRTM_List(List list){
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

  private class TopoNull implements TopoIface{
    @Override public boolean can_read(){
      return false;
    }
    @Override public Object read(){
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
    @Override public Object read(){
      return read_value;
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
    @Override public Object read(){
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


