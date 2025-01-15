/*
An TM_SR_ND with indexing == TM_SR_ND

An index is indicates where the head is located on the tape.  This is done by
tracking the cell address for the head.

BigInteger is used so that it will be possible to bind TM_SR_ND machine extensions
to file system objects.

*/

package com.ReasoningTechnology.Ariadne;
import java.math.BigInteger;

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
  protected BigInteger index;

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

  protected void increment(){
    index = index.add(BigInteger.ONE);
  }

  public boolean head_on_same_cell(Ariadne_TM_SR_ND tm){
    return this.index.equals(tm.index);
  }

  public Ariadne_TM_SR_ND entangle(){
    Ariadne_TM_SR_ND copy = make();

    // entangled copy shares the same tape
    copy.current_topology = this.current_topology; // Shares the same reference

    // Nuance here, BigInteger is immutable, so operation on the original
    // index, and the copy index, will be independent, which is what we want.
    copy.index = this.index; 

    return copy;
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
    increment();
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

    StringBuilder data_channel = new StringBuilder(" ");
    StringBuilder control_channel = new StringBuilder("|");
    
    data_channel.append( "TM_SR_ND(" ).append( topology().name()).append("( " );
    control_channel.append( " ".repeat(data_channel.length()) );

    String element = null;
    Ariadne_TM_SR_ND copy = (Ariadne_TM_SR_ND) this.entangle();
    Object o = null;
    do{
      o = copy.read();

      if(o == null){
        data_channel.append( " ".repeat(3) );
        if(head_on_same_cell(copy)){
          control_channel.append("<->");
        }else{
          control_channel.append("|-|");
        }
      }else if(o.toString().isEmpty()){
        data_channel.append( " ".repeat(3) );
        if(head_on_same_cell(copy)){
          control_channel.append("<e>");
        }else{
          control_channel.append("|e|");
        }
      }else{
        element = o.toString();
        data_channel.append(" ").append(element).append(" ");
        if(head_on_same_cell(copy)){
          control_channel
            .append("<")
            .append("d".repeat(element.length()))
            .append(">");
        }else{
          control_channel
            .append("|")
            .append("d".repeat(element.length()))
            .append("|");
        }
      }

      if(!copy.can_step()) break;
      copy.step();
    }while(true);

    data_channel.append(" )");
    control_channel.append("  ");

    return 
      data_channel
      .append("\n")
      .append(control_channel)
      .append("\n")
      .toString();
  }

}
