package com.ReasoningTechnology.Ariadne;

public class Ariadne_IndexTree_Child_SRM extends Ariadne_SRM_Label {

  public static Ariadne_IndexTree_Child_SRM make( Ariadne_IndexTree_Label first_child_label ){
    return new Ariadne_IndexTree_Child_SRM( first_child_label );
  }

  private final Ariadne_IndexTree_Label label;

  protected Ariadne_IndexTree_Child_SRM( Ariadne_IndexTree_Label first_child_label ){
    this.label = first_child_label.copy();

    if( label == null ){
      set_topology( topo_null );
      return;
    }

    if( label.isEmpty() ){
      set_topology( topo_rightmost );
      return;
    }

    set_topology( topo_infinite_right );
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
