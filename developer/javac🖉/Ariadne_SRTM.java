/*
  Step Right Machine

  This is a mostly abstract base class.

  This is for single-threaded execution. The multi-threaded model
  uses `mount` and `dismount` to lock the resources being iterated on.
*/
package com.ReasoningTechnology.Ariadne;

public class Ariadne_SRMT{

  // static
  //

  public enum Topology{
    NULL
    ,CYCLIC
    ,SEGMENT
    ,RIGHTMOST
    ,INFINITE
  }

  public static Ariadne_SRMT make(){
    return new Ariadne_SRMT();
  }

  // instance data
  //

  protected TopoIface current_topology;
  public final TopoIface not_mounted = new NotMounted();

  // constructor(s)
  //

  protected Ariadne_SRMT(){
    set_topology( not_mounted );
  }

  public boolean is_mounted(){
    return 
      current_topology != null 
      && current_topology != not_mounted;
  }

  // Implementation of instance interface.

  protected interface TopoIface{
    boolean can_read();
    Object read();
    boolean can_step();
    void step();
    Topology topology();
  }

  // Initially, the tape has not been mounted.
  protected class NotMounted implements TopoIface{
    @Override public boolean can_read(){
      return false;
    }
    @Override public Object read(){
      throw new UnsupportedOperationException("Ariadne_SRMT::NotMounted::read.");
    }
    @Override public boolean can_step(){
      return false;
    }
    @Override public void step(){
      throw new UnsupportedOperationException("Ariadne_SRMT::NotMounted::step.");
    }
    @Override public Topology topology(){
      throw new UnsupportedOperationException("Ariadne_SRMT::NotMounted::topology.");
    }
  }

  // Sets the tape access methods to be used.
  protected void set_topology(TopoIface new_topology){
    current_topology = new_topology;
  }

  public boolean can_read(){
    return current_topology.can_read();
  }

  public Object read(){
    return current_topology.read();
  }

  public boolean can_step(){
    return current_topology.can_step();
  }

  public void step(){
    current_topology.step();
  }

  public Topology topology(){
    return current_topology.topology();
  }

}

