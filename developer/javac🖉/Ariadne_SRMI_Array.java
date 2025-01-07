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

  private final TopoIface<T> state_null = new StateNull();
  private final TopoIface<T> state_segment = new StateSegment();
  private final TopoIface<T> state_rightmost = new StateRightmost();

  // Constructor
  protected Ariadne_SRMI_Array(List<T> array){
    super();
    this.array = array;

    if( array == null || array.isEmpty() ){
      set_topology( state_null );
      return;
    }

    if( array.size() == 1 ){
      set_topology( state_rightmost );
      return;
    }

    set_topology( state_segment );
  }

  // StateNull
  private class StateNull implements TopoIface<T>{
    @Override
    public boolean can_read(){
      return false;
    }
    @Override
    public T read(){
      throw new UnsupportedOperationException( "Cannot read from NULL state." );
    }
    @Override
    public boolean can_step(){
      return false;
    }
    @Override
    public void step(){
      throw new UnsupportedOperationException( "Cannot step from NULL state." );
    }
    @Override
    public Topology topology(){
      return Topology.NULL;
    }
  }

  // StateSegment
  private class StateSegment implements TopoIface<T>{
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
        set_topology(state_rightmost);
    }
    @Override
    public Topology topology(){
      return Topology.SEGMENT;
    }
  }

  // StateRightmost
  private class StateRightmost implements TopoIface<T>{
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
      throw new UnsupportedOperationException( "Cannot step from RIGHTMOST state." );
    }
    @Override
    public Topology topology(){
      return Topology.RIGHTMOST;
    }
  }
}
