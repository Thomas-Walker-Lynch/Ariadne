/*
  Step Right Machine

  This is a mostly abstract base class.

  This is for single-threaded execution. The multi-threaded model
  uses `mount` and `dismount` to lock the resources being iterated on.
*/
package com.ReasoningTechnology.Ariadne;

public class Ariadne_SRM<T>{

  public enum Topology{
    NULL
    ,CYCLIC
    ,SEGMENT
    ,RIGHTMOST
    ,INFINITE
    ;
  }

  public static <TElement> Ariadne_SRM<TElement> make(){
    return new Ariadne_SRM<>();
  }

  protected TopoIface<T> current_topology;
  public final TopoIface<T> not_mounted = new NotMounted();

  protected Ariadne_SRM(){
    set_topology(not_mounted);
  }

  public boolean is_mounted(){
    return 
      current_topology != null 
      && current_topology != not_mounted;
  }

  // Interface for interacting with a tape.
  protected interface TopoIface<T>{
    boolean can_read();
    T read();
    boolean can_step();
    void step();
    Topology topology();
  }

  // Initially the tape has not been mounted so it can not be interacted with.
  protected class NotMounted implements TopoIface<T>{
    public boolean can_read(){
      return false;
    }
    public T read(){
      throw new UnsupportedOperationException("Ariadne_SRM::NotMounted::read.");
    }
    public boolean can_step(){
      return false;
    }
    public void step(){
      throw new UnsupportedOperationException("Ariadne_SRM::NotMounted::step.");
    }
    public Topology topology(){
      throw new UnsupportedOperationException("Ariadne_SRM::NotMounted::topology.");
    }
  }

  // sets the tape access methods to be used
  protected void set_topology(TopoIface<T> new_topology){
    current_topology = new_topology;
  }

  public boolean can_read(){
    return current_topology.can_read();
  }
  public T read(){
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
 
