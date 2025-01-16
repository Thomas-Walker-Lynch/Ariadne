package com.ReasoningTechnology.Ariadne;

import java.math.BigInteger;
import java.util.List;

public class Ariadne_TM_SR_ND_Array<RT> extends Ariadne_TM_SR_ND<RT>{

  // Static methods
  //
  
  public static <T> Ariadne_TM_SR_ND_Array<T> make(List<T> array){
    return new Ariadne_TM_SR_ND_Array<>(array);
  }

  // Instance data
  //
  
  private final List<RT> array;

  // Constructor(s)
  //
  
  protected Ariadne_TM_SR_ND_Array(List<RT> array){
    super();
    this.array = array;

    if( array == null || array.isEmpty() ){
      set_topology( topo_null );
      return;
    }

    if( array.size() == 1 ){
      set_topology( topo_rightmost );
      return;
    }

    set_topology( topo_segment );
  }

  // instance interface implementation
  //

  // Children of this can call super.entangle(copy) to perform the parent part of the entanglement.
  // This calls super to perform its parent portion of the entanglement.
  protected void entangle(Ariadne_TM_SR_ND_Array<RT> copy){
    super.entangle(copy);
  }

  @Override public Ariadne_TM_SR_ND_Array<RT> entangle(){
    Ariadne_TM_SR_ND_Array<RT> copy = Ariadne_TM_SR_ND_Array.make(this.array);
    entangle(copy);

    switch (this.current_topology.topology()) {
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

  @Override public boolean can_rewind(){
    return true;
  }

  @Override public void rewind() {
    super.rewind();
    if (array == null || array.isEmpty()) {
      set_topology(topo_null); // Null topology for empty or null arrays
      return;
    }
    set_topology(array.size() == 1 ? topo_rightmost : topo_segment); // Adjust topology
  }

  protected class SegmentTopo implements TopoIface<RT>{
    @Override public boolean can_read(){return true;}
    @Override public RT read(){
      return array.get(head_address().intValueExact());
    }
    @Override public boolean can_step(){return true;}
    @Override public void step(){
      if(head_address().compareTo(BigInteger.valueOf(array.size() - 1)) == 0)
        set_topology(topo_rightmost);
    }
    @Override public Topology topology(){return Topology.SEGMENT;}
  }
  protected final TopoIface<RT> topo_segment = new SegmentTopo();

  protected class RightmostTopo implements TopoIface<RT>{
    @Override public boolean can_read(){return true;}
    @Override public RT read(){
      return array.get(head_address().intValueExact());
    }
    @Override public boolean can_step(){return false;}
    @Override public void step(){
      throw new UnsupportedOperationException("Cannot step from RIGHTMOST topo.");
    }
    @Override public Topology topology(){return Topology.RIGHTMOST;}
  }
  protected final TopoIface<RT> topo_rightmost = new RightmostTopo();
  
}
