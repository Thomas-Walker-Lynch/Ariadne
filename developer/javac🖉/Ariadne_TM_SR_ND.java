/*
An TM_SR_ND with indexing == TM_SR_ND

An index is indicates where the head is located on the tape.  This is done by
tracking the cell address for the head.

BigInteger is used so that it will be possible to bind TM_SR_ND machine extensions
to file system objects.

*/

package com.ReasoningTechnology.Ariadne;
import java.math.BigInteger;

public abstract class Ariadne_TM_SR_ND extends Ariadne_TM_SR_ND{

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
  private BigInteger index;

  // constructor(s)
  //

  public Ariadne_TM_SR_ND(){
    set_topology( not_mounted );
    this.index = BigInteger.ZERO;
  }

  // Implementation of instance interface.
  //

  public BigInteger head_address(){
    return index;
  }

  public Ariadne_TM_SR_ND entangle(){
    try{
      Ariadne_TM_SR_ND copy = (Ariadne_TM_SR_ND) this.clone();

      // entangled copy shares the same tape
      copy.tape_list = this.tape_list; // Shares the same reference
      copy.current_topology = this.current_topology; // Shares the same reference

      // nuance here, BigInteger is immutable, so any operation on the original
      // index, and the copy index will be independent without having to do a deep copy.
      copy.index = this.index; 

      return copy;
    }catch(CloneNotSupportedException e){
      throw new AssertionError( "Clone not supported: " + e.getMessage() );
    }
  }

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

  @Override public String toString(){
    if(!is_mounted()) return "TM_SR_ND(NotMounted)";
    if(!can_read()) return "TM_SR_ND(Null)";

    StringBuilder sb = new StringBuilder();
    sb.append("TM_SR_ND(").append(topology().name()).append("(");

    try{
      // Clone for traversal
      Ariadne_TM_SR_ND copy = (Ariadne_TM_SR_ND) this.clone();

      Object o = null;
      do{
        o = copy.read();
        if(o == null){
          sb.append("null");
        }else if(!copy.can_step()){
          sb.append("[");
          sb.append(o.toString());
          sb.append("]");
        }else{
          sb.append(o.toString());
        }

        if(!copy.can_step()) break;
        sb.append(" ,");
        copy.step();
      }while(true);

    }catch(CloneNotSupportedException e){
      throw new AssertionError("Clone not supported: " + e.getMessage());
    }

    sb.append("))");
    return sb.toString();
  }

}
