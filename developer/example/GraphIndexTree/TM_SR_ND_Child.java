/*
TM_SR_ND_Child represents in the abstract the infinite child list of an
IndexTree node.  Index tree node labels are paths through the tree, so
labels can be computed.

TM_SR_ND_Child is made from the leftmost child label. Then step() takes
the current label and computes from it the right neighbor sibling
node's label.

*/

import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_ND;

public class TM_SR_ND_Child extends Ariadne_TM_SR_ND<Label>{

  // Static
  //
  public static TM_SR_ND_Child make(Label leftmost_child_label){
    return new TM_SR_ND_Child(leftmost_child_label);
  }

  // Instance data
  //
  private final Label label;

  // Constructor(s)
  //
  protected TM_SR_ND_Child(Label leftmost_child_label){
    this.label = leftmost_child_label != null ? leftmost_child_label.copy() : null;

    if(label == null){
      set_topology(topo_null);
      return;
    }
    if(label.length() == 0){
      set_topology(topo_rightmost);
      return;
    }
    set_topology(topo_infinite_right);
  }

  // Implementation of the instance interface
  //

  private class InfiniteRightTopo implements TopoIface<Label>{
    @Override public boolean can_read(){
      return true;
    }
    @Override public Label read(){
      return label;
    }
    @Override public boolean can_step(){
      return true;
    }
    @Override public void step(){
      label.inc_across();
    }
    @Override public Topology topology(){
      return Topology.INFINITE;
    }
  }
  private final TopoIface<Label> topo_infinite_right = new InfiniteRightTopo();

  private class RightmostTopo implements TopoIface<Label>{
    @Override public boolean can_read(){
      return true;
    }
    @Override public Label read(){
      return label;
    }
    @Override public boolean can_step(){
      return false;
    }
    @Override public void step(){
      throw new UnsupportedOperationException("Cannot step from RIGHTMOST topology.");
    }
    @Override public Topology topology(){
      return Topology.RIGHTMOST;
    }
  }
  private final TopoIface<Label> topo_rightmost = new RightmostTopo();
}

