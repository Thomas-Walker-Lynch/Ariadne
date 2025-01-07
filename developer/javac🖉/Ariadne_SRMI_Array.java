package com.ReasoningTechnology.Ariadne;

import java.math.BigInteger;
import java.util.List;

public class Ariadne_SRMI_Array<T> extends Ariadne_SRMI<T>{

  // Static methods
  public static <T> Ariadne_SRMI_Array<T> make(List<T> array){
    return new Ariadne_SRMI_Array<>( array );
  }

  // Instance data
  private final List<T> array;

  private final TopoIface<T> topo_null = new TopoNull();
  private final TopoIface<T> topo_segment = new TopoSegment();
  private final TopoIface<T> topo_rightmost = new TopoRightmost();

  // Constructor
  protected Ariadne_SRMI_Array(List<T> array){
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

  // TopoSegment
  private class TopoSegment implements TopoIface<T>{
    @Override
    public boolean can_read(){
      return true;
    }
    @Override
    public T read(){
      return array.get( index().intValueExact() );
    }
    @Override
    public boolean can_step(){
      return true;
    }
    @Override
    public void step(){
      Ariadne_SRMI_Array.super.step();
      if( index().compareTo(BigInteger.valueOf(array.size() - 1)) < 0 )
        set_topology(topo_rightmost);
    }
    @Override
    public Topology topology(){
      return Topology.SEGMENT;
    }
  }

  // TopoRightmost
  private class TopoRightmost implements TopoIface<T>{
    @Override
    public boolean can_read(){
      return true;
    }
    @Override
    public T read(){
      return array.get( index().intValueExact() );
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
