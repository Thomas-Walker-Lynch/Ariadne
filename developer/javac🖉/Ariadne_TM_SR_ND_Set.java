package com.ReasoningTechnology.Ariadne;

import java.util.Set;
import java.util.Iterator;

public class Ariadne_TM_SR_ND_Set<RT> extends Ariadne_TM_SR_ND<RT>{

  // Static methods
  public static <RT> Ariadne_TM_SR_ND_Set<RT> make(Set<RT> set){
    return new Ariadne_TM_SR_ND_Set<>(set);
  }

  // Instance data
  private Set<RT> set;
  private Iterator<RT> iterator;
  private RT read_value;

  protected final TopoIface<RT> topo_segment = new SegmentTopo();
  protected final TopoIface<RT> topo_rightmost = new RightmostTopo();

  // Constructor
  protected Ariadne_TM_SR_ND_Set(Set<RT> set){
    this.set = set;
    if (set == null || set.isEmpty()){
      this.iterator = null;
      set_topology(topo_null);
      return;
    }
    this.iterator = set.iterator();
    this.read_value = iterator.hasNext() ? iterator.next() : null;
    set_topology(set.size() == 1 ? topo_rightmost : topo_segment);
  }

  // Instance interface implementation
  @Override public void rewind(){
    super.rewind();
    if (set == null || set.isEmpty()){
      set_topology(topo_null);
      return;
    }
    this.iterator = set.iterator();
    this.read_value = iterator.hasNext() ? iterator.next() : null;
    set_topology(set.size() == 1 ? topo_rightmost : topo_segment);
  }

  protected void entangle(Ariadne_TM_SR_ND_Set<RT> copy){
    super.entangle(copy);
    copy.iterator = this.set.iterator();
    copy.read_value = this.read_value;
  }

  @Override public Ariadne_TM_SR_ND_Set<RT> entangle(){
    Ariadne_TM_SR_ND_Set<RT> copy = Ariadne_TM_SR_ND_Set.make(this.set);
    entangle(copy);
    switch (this.current_topology.topology()){
      case NULL:
        copy.current_topology = copy.topo_null;
        break;
      case SEGMENT:
        copy.current_topology = copy.topo_segment;
        break;
      case RIGHTMOST:
        copy.current_topology = copy.topo_rightmost;
        break;
      default:
        throw new IllegalStateException("Unexpected topology: " + this.current_topology.topology());
    }
    return copy;
  }

  protected class SegmentTopo implements TopoIface<RT>{
    @Override public boolean can_read(){ return true; }
    @Override public RT read(){ return read_value; }
    @Override public boolean can_step(){ return iterator.hasNext(); }
    @Override public void step(){
      read_value = iterator.next();
      if (!iterator.hasNext()) set_topology(topo_rightmost);
    }
    @Override public Topology topology(){ return Topology.SEGMENT; }
  }

  protected class RightmostTopo implements TopoIface<RT>{
    @Override public boolean can_read(){ return true; }
    @Override public RT read(){ return read_value; }
    @Override public boolean can_step(){ return false; }
    @Override public void step(){
      throw new UnsupportedOperationException("Cannot step from RIGHTMOST topo.");
    }
    @Override public Topology topology(){ return Topology.RIGHTMOST; }
  }
}
