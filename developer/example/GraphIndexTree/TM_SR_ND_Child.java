/*
TM_SR_ND_Child represents in the abstract the infinite child list of an
IndexTree node.  Index tree node labels are paths through the tree, so
labels can be computed.

TM_SR_ND_Child is made from the leftmost child label. Then step() takes
the current label and computes from it the right neighbor sibling
node's label.

*/


import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_ND_Label;

public class TM_SR_ND_Child extends Ariadne_TM_SR_ND_Label{

  // Static
  //

  public static TM_SR_ND_Child make( Label leftmost_child_label ){
    return new TM_SR_ND_Child( leftmost_child_label );
  }

  // Instance data
  //

  // Label is a container of co-ordinates to a node, so the only thing 'final'
  // is the container, not its contents.
  private final Label label;

  // Constructor(s)
  //

  protected TM_SR_ND_Child( Label leftmost_child_label ){
    this.label = leftmost_child_label.copy();

    if( label == null ){
      set_topology(topo_null);
      return;
    }

    // the label for the root node is an empty array, "[]"
    if( label.length() == 0){
      set_topology(topo_rightmost);
      return;
    }

    set_topology(topo_infinite_right);
  }

  // Implementation of the instance interface
  //

  @Override public Label read(){
    return (Label)super.read();
  }

  private final TopoIface topo_null = new TopoIface(){
    @Override public boolean can_read(){
      return false;
    }
    @Override public Object read(){
      throw new UnsupportedOperationException( "Cannot read from NULL topology." );
    }
    @Override public boolean can_step(){
      return false;
    }
    @Override public void step(){
      throw new UnsupportedOperationException( "Cannot step from NULL topology." );
    }
    @Override public Topology topology(){
      return Topology.NULL;
    }
  };

  private final TopoIface topo_infinite_right = new TopoIface(){
    @Override public boolean can_read(){
      return true;
    }
    @Override public Object read(){
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
  };

  private final TopoIface topo_rightmost = new TopoIface(){
    @Override public boolean can_read(){
      return true;
    }
    @Override public Object read(){
      return label;
    }
    @Override public boolean can_step(){
      return false;
    }
    @Override public void step(){
      throw new UnsupportedOperationException( "Cannot step from RIGHTMOST topology." );
    }
    @Override public Topology topology(){
      return Topology.RIGHTMOST;
    }
  };

}
