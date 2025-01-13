package com.ReasoningTechnology.Ariadne;

import java.util.Iterator;
import java.util.Set;

public class Ariadne_SRTM_Set<T> extends Ariadne_SRTM{

  // Static factory method
  public static <T> Ariadne_SRTM_Set<T> make(Set<T> set){
    return new Ariadne_SRTM_Set<>(set);
  }

  // Instance data
  private final Set<T> set;
  private final Iterator<T> iterator;
  private T current_value;

  private final TopoIface topo_null = new TopoNull();
  private final TopoIface topo_segment = new TopoSegment();
  private final TopoIface topo_rightmost = new TopoRightmost();

  // Constructor
  protected Ariadne_SRTM_Set(Set<T> set){
    this.set = set;

    if( set == null || set.isEmpty() ){
      this.iterator = null;
      set_topology(topo_null);
      return;
    }

    this.iterator = set.iterator();
    this.current_value = iterator.hasNext() ? iterator.next() : null;

    if( set.size() == 1 ){
      set_topology(topo_rightmost);
    }else{
      set_topology(topo_segment);
    }
  }

  // Instance interface implementation

  @Override
  @SuppressWarnings("unchecked")
  public T read(){
    return (T)current_topology.read();
  }

  private class TopoNull implements TopoIface{
    @Override public boolean can_read(){
      return false;
    }
    @Override public T read(){
      throw new UnsupportedOperationException("Cannot read from NULL topo.");
    }
    @Override public boolean can_step(){
      return false;
    }
    @Override public void step(){
      throw new UnsupportedOperationException("Cannot step from NULL topo.");
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
      return current_value;
    }
    @Override public boolean can_step(){
      return iterator.hasNext();
    }
    @Override public void step(){
      current_value = iterator.next();
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
      return current_value;
    }
    @Override public boolean can_step(){
      return false;
    }
    @Override public void step(){
      throw new UnsupportedOperationException("Cannot step from RIGHTMOST topo.");
    }
    @Override public Topology topology(){
      return Topology.RIGHTMOST;
    }
  }
}
