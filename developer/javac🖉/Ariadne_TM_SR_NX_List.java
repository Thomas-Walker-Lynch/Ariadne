/*
  By convention an TM_SR_NX is named after the type of values found in the tape cells.
  However, in this case the name reflects the type that that TM_SR_NX is made from.
  The type of values found in the TM_SR_NX cells is abstracted as "T".

  This implementation uses Java's ListIterator, which lacks a direct
  method to read the current cell value, rather it yields the next
  cell value upon advancing the iterator.
*/
package com.ReasoningTechnology.Ariadne;
import java.util.List;
import java.util.ListIterator;

public class Ariadne_TM_SR_NX_List<RT> extends Ariadne_TM_SR_NX_F<RT>{

  // Static methods
  //

  public static <T> Ariadne_TM_SR_NX_List<T> make(List<T> list) {
    return new Ariadne_TM_SR_NX_List<>(list);
  }

  // instance data
  //

  private List<RT> list;  // The attached linked list
  private ListIterator<RT> iterator;  // Iterator for traversal
  private RT read_value;  // Stores the current cell value

  // constructor(s)
  //

  protected Ariadne_TM_SR_NX_List(List<RT> list){
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

  // Children of this can call entangle(copy) to perform the parent part of the entanglement.
  // This calls super to perform its parent portion of the entanglement.
  protected void entangle(Ariadne_TM_SR_NX_List<RT> copy){
    super.entangle(copy);
    copy.read_value = this.read_value;
    copy.iterator = this.list.listIterator(this.iterator.nextIndex());
  }

  @Override public Ariadne_TM_SR_NX_List<RT> entangle(){
    Ariadne_TM_SR_NX_List<RT> copy = Ariadne_TM_SR_NX_List.make(this.list);
    entangle(copy);

    // Set the appropriate topology in the copy based on the current topology
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

  @Override public void rewind(){
    super.rewind();
    if(list == null || list.isEmpty()){
      set_topology(topo_null);
      return;
    }
    iterator = list.listIterator(); // Reset the iterator
    read_value = iterator.next(); // Sync the read value
    set_topology(list.size() == 1 ? topo_rightmost : topo_segment); // Adjust topology
  }

  protected class SegmentTopo implements TopoIface<RT>{
    @Override public boolean can_read(){ return true; }
    @Override public RT read(){ return (RT) read_value; }
    @Override public boolean can_step(){ return true; }
    @Override public void step(){
      read_value = iterator.next();
      if( !iterator.hasNext() ) set_topology(topo_rightmost);
    }
    @Override public Topology topology(){ return Topology.SEGMENT; }
  }
  protected final TopoIface<RT> topo_segment = new SegmentTopo();

  protected class RightmostTopo implements TopoIface<RT>{
    @Override public boolean can_read(){ return true; }
    @Override public RT read(){ return read_value; }
    @Override public boolean can_step(){ return false; }
    @Override public void step(){
      throw new UnsupportedOperationException("Cannot step from RIGHTMOST topo.");
    }
    @Override public Topology topology(){ return Topology.RIGHTMOST; }
  }
  protected final TopoIface<RT> topo_rightmost = new RightmostTopo();

}


