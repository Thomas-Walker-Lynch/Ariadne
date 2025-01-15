package com.ReasoningTechnology.Ariadne;

import java.math.BigInteger;
import java.util.List;

public class Ariadne_TM_SR_ND_Array<T> extends Ariadne_TM_SR_ND{

  // Static methods
  //
  
  public static <T> Ariadne_TM_SR_ND_Array<T> make(List<T> array){
    return new Ariadne_TM_SR_ND_Array<>(array);
  }

  // Instance data
  //
  
  private final List<T> array;

  private final TopoIface topo_null = new TopoNull();
  private final TopoIface topo_segment = new TopoSegment();
  private final TopoIface topo_rightmost = new TopoRightmost();

  // Constructor
  protected Ariadne_TM_SR_ND_Array(List<T> array){
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

  // TopoNull
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
      throw new UnsupportedOperationException( "Cannot step from NULL topo." );
    }
    @Override public Topology topology(){
      return Topology.NULL;
    }
  }

  // TopoSegment
  private class TopoSegment implements TopoIface{
    @Override public boolean can_read(){
      return true;
    }
    @Override public Object read(){
      return array.get( head_address().intValueExact() );
    }
    @Override public boolean can_step(){
      return true;
    }
    @Override public void step(){
      if( head_address().compareTo(BigInteger.valueOf(array.size() - 1)) == 0 )
        set_topology(topo_rightmost);
    }
    @Override public Topology topology(){
      return Topology.SEGMENT;
    }
  }

  // TopoRightmost
  private class TopoRightmost implements TopoIface{
    @Override public boolean can_read(){
      return true;
    }
    @Override public Object read(){
      return array.get( head_address().intValueExact() );
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
