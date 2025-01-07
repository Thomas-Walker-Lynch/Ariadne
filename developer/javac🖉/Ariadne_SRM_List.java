/*
  The Ariadne_SRM_List class provides a Step Right Machine (SRM) for linked lists.
  This implementation uses Java's ListIterator, which lacks a direct method
  to read the current element without advancing the iterator.

*/
package com.ReasoningTechnology.Ariadne;
import java.util.List;

public class Ariadne_SRM_List<T> extends Ariadne_SRM<T>{

  private final List<T> list;
  private int current_index;

  private final TopoIface<T> topo_null = new TopoNull();
  private final TopoIface<T> topo_segment = new TopoSegment();
  private final TopoIface<T> topo_rightmost = new TopoRightmost();

  public Ariadne_SRM_List(List<T> list){

    if( list == null || list.isEmpty() ){
      this.list = null;
      set_topology( topo_null );
      return;
    }

    this.list = list;
    this.current_index = 0;
    set_topology( topo_segment );
  }

  private class TopoNull implements TopoIface<T>{
    @Override
    public boolean can_read(){
      return false;
    }
    @Override
    public T read(){
      throw new UnsupportedOperationException( "Cannot read from NULL topo." );
    }
    @Override
    public boolean can_step(){
      return false;
    }
    @Override
    public void step(){
      throw new UnsupportedOperationException( "Cannot step from NULL topo." );
    }
    @Override
    public Topology topology(){
      return Topology.NULL;
    }
  }

  private class TopoSegment implements TopoIface<T>{
    @Override
    public boolean can_read(){
      return true;
    }
    @Override
    public T read(){
      return list.get( current_index );
    }
    @Override
    public boolean can_step(){
      return current_index < list.size() - 1;
    }
    @Override
    public void step(){
      if( can_step() ){
        current_index++;
      }else{
        set_topology( topo_rightmost );
      }
    }
    @Override
    public Topology topology(){
      return Topology.SEGMENT;
    }
  }

  private class TopoRightmost implements TopoIface<T>{
    @Override
    public boolean can_read(){
      return true;
    }
    @Override
    public T read(){
      return list.get( current_index );
    }
    @Override
    public boolean can_step(){
      return false;
    }
    @Override
    public void step(){
      throw new UnsupportedOperationException( "Cannot step from RIGHTMOST topo." );
    }
    @Override
    public Topology topology(){
      return Topology.RIGHTMOST;
    }
  }
}


