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

  protected static int id_well = 100;

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

  private int id;
  Ariadne_Test test = null;
  protected TopoIface current_topology;
  protected BigInteger index;

  // constructor(s)
  //

  public Ariadne_TM_SR_ND(){
    id = id_well++;
    test = Ariadne_Test.make("Ariadne_TM_SR_ND::" + id + "::");
    test.switch_test(false);
    set_topology( not_mounted );
    this.index = BigInteger.ZERO;
  }

  // Implementation of instance interface.
  //

  public int id(){
    return this.id;
  }

  public BigInteger head_address(){
    return index;
  }

  protected void increment(){
    index = index.add(BigInteger.ONE);
  }

  public boolean head_on_same_cell(Ariadne_TM_SR_ND tm){
    boolean p = this.index.equals(tm.index);
    if( test.is_on() ){
      test.print("head_on_same_cell this id/index: " + this.id() + "/" + this.index );
      test.print("head_on_same_cell tm id/index: " + tm.id() + "/" + tm.index );
      test.print("head_on_same_cell returning: " + p );
    }
    return p;
  }

  protected void entangle(Ariadne_TM_SR_ND copy){
    copy.current_topology = this.current_topology;
    // Nuance here, BigInteger is immutable, so operation on the original
    // index, and the copy index, will be independent, which is what we want.
    copy.index = this.index; 
  }
  public Ariadne_TM_SR_ND entangle(){
    throw new UnsupportedOperationException("Ariadne_TM_SR_ND::entangle not implemented.");
  }

  public boolean is_mounted(){
    return 
      current_topology != null 
      && current_topology != not_mounted
      ;
  }

  public boolean can_rewind(){
    return false;
  }

  public void rewind(){
    index = BigInteger.ZERO;
  }

  public boolean can_read(){
    boolean p = current_topology.can_read();
    if( test.is_on() ) test.print("can_read " + p);
    return p;
  }

  public Object read(){
    Object o = current_topology.read();
    if( test.is_on() ) test.print("read: " + o);
    return o;
  }

  public boolean can_step(){
    boolean p = current_topology.can_step();
    if( test.is_on() ) test.print("can_step " + p);
    return p;
  }

  public void step(){
    increment();
    if( test.is_on() ) test.print("step: " + index);
    current_topology.step();
  }

  public Topology topology(){
    return current_topology.topology();
  }

  // Sets the tape access methods to be used.
  protected void set_topology(TopoIface new_topology){
    current_topology = new_topology;
    if( test.is_on() ){
      test.print("set_topology i: " + index);
      if(is_mounted()){
        test.print("set_topology to: " + new_topology.topology());
      }else{
        test.print("set_topology to: Unmounted");
      }
    }
  }

  protected interface TopoIface{
    boolean can_read();
    Object read();
    boolean can_step();
    void step();
    Topology topology();
  }

  protected final TopoIface not_mounted = new TopoIface(){
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
    };

  protected final TopoIface topo_null = new TopoIface(){
    @Override public boolean can_read(){
      return false;
    }
    @Override public Object read(){
      throw new UnsupportedOperationException( "Cannot read from null topology." );
    }
    @Override public boolean can_step(){
      return false;
    }
    @Override public void step(){
      throw new UnsupportedOperationException( "Cannot step over null topology." );
    }
    @Override public Topology topology(){
      return Topology.NULL;
    }
    };

  // good citizen
  //

  @Override public String toString(){
    if(!is_mounted()) return "TM_SR_ND(NotMounted)";
    if(!can_read()) return "TM_SR_ND(Null)";

    StringBuilder data_channel = new StringBuilder("");
    StringBuilder control_channel = new StringBuilder("");
    
    data_channel.append( "TM_SR_ND(" ).append( topology().name()).append("( " );
    control_channel.append( " ".repeat(data_channel.length()) );

    String element = null;
    Ariadne_TM_SR_ND copy = this.entangle();
    if( copy.can_rewind() ) copy.rewind();

    Object o = null;
    do{

      o = copy.read();

      if(o == null){
        data_channel.append( " ".repeat(3) );
        if( head_on_same_cell(copy) ){
          control_channel.append("<->");
        }else{
          control_channel.append("|-|");
        }
      }else if(o.toString().isEmpty()){
        data_channel.append( " ".repeat(3) );
        if( head_on_same_cell(copy) ){
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

      if( !copy.can_step() ) break;
      copy.step();

    }while(true);

    data_channel.append(" )");
    control_channel.append("  ");

    return 
      data_channel
      .append("\n")
      .append(control_channel)
      .toString();
  }

}
