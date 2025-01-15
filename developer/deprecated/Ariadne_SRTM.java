/*
  Step Right Tape Machine

  Depending how the undefined methods here are defined, the TM_SR_ND can
  equally be a finite iterator, a generator, or an infinite stream.

  This is for single-threaded execution. The multi-threaded model
  uses `mount` and `dismount` to lock the resources being iterated over.
*/
package com.ReasoningTechnology.Ariadne;

public class Ariadne_TM_SR_ND{

  // static
  //

  public enum Topology{
    NULL
    ,CYCLIC
    ,SEGMENT
    ,RIGHTMOST
    ,INFINITE
  }

  public static Ariadne_TM_SR_ND make(){
    return new Ariadne_TM_SR_ND();
  }

  // instance data
  //

  protected TopoIface current_topology;
  public final TopoIface not_mounted = new NotMounted();

  // constructor(s)
  //

  protected Ariadne_TM_SR_ND(){
    set_topology( not_mounted );
  }

  // Implementation of instance interface.
  //

  public boolean is_mounted(){
    return 
      current_topology != null 
      && current_topology != not_mounted;
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

  // Sets the tape access methods to be used.
  protected void set_topology(TopoIface new_topology){
    current_topology = new_topology;
  }

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
      throw new UnsupportedOperationException("Ariadne_TM_SR_ND::NotMounted::read.");
    }
    @Override public boolean can_step(){
      return false;
    }
    @Override public void step(){
      throw new UnsupportedOperationException("Ariadne_TM_SR_ND::NotMounted::step.");
    }
    @Override public Topology topology(){
      throw new UnsupportedOperationException("Ariadne_TM_SR_ND::NotMounted::topology.");
    }
  }


  // good citizen
  //


}

