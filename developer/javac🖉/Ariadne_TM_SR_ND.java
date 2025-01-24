/*
A step right only, non-destructive tape operations, tape machine, TM_SR_ND. This
machine has explicit tracking of the head address.

BigInteger is used so that it will be possible to bind TM_SR_ND machine extensions
to file system objects.

*/

package com.ReasoningTechnology.Ariadne;
import java.math.BigInteger;

// RT == read type
public class Ariadne_TM_SR_ND<RT>{

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
  protected TopoIface<RT> current_topology;
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

  public boolean head_on_same_cell(Ariadne_TM_SR_ND<RT> tm){
    boolean p = this.index.equals(tm.index);
    if( test.is_on() ){
      test.print("head_on_same_cell this id/index: " + this.id() + "/" + this.index );
      test.print("head_on_same_cell tm id/index: " + tm.id() + "/" + tm.index );
      test.print("head_on_same_cell returning: " + p );
    }
    return p;
  }

  protected void entangle(Ariadne_TM_SR_ND<RT> copy){
    copy.current_topology = this.current_topology;
    // Nuance here, BigInteger is immutable, so operation on the original
    // index, and the copy index, will be independent, which is what we want.
    copy.index = this.index; 
  }
  public Ariadne_TM_SR_ND<RT> entangle(){
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

  // Append to rightmost is not considered 'destructive' because the system, including
  // any entangled machines, remains sound after the operation. I.e. no
  // machine is broken or has its current read value changed.
  // However, an entangled machine can detect the end of tape, so a machine
  // can detect that an append happened. Hence append can affect an algorithm
  // that is running on an entangled machine, but this can also happen when
  // any of the entangled machine writes the tape. Hence, append has a similar
  // affect as other writes.
  public void append_rightmost(RT x){
    throw new UnsupportedOperationException("Ariadne_TM_SR_ND::entangle not implemented.");
  }

  // stateful interface
  //

  public boolean can_read(){
    boolean p = current_topology.can_read();
    if( test.is_on() ) test.print("can_read " + p);
    return p;
  }

  public RT read(){
    RT o = current_topology.read();
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
  protected void set_topology(TopoIface<RT> new_topology){
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

  protected interface TopoIface<T>{
    boolean can_read();
    T read();
    boolean can_step();
    void step();
    Topology topology();
  }

  // yes officially this works, but it introduces subtle problems at compile time:
  //  protected final TopoIface<RT> not_mounted =  new TopoIface<RT>(){

  protected class NotMountedTopo implements TopoIface<RT>{
    @Override public boolean can_read(){
      return false;
    }
    @Override public RT read(){
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
  protected final TopoIface<RT> not_mounted = new NotMountedTopo();


  protected class NullTopo implements TopoIface<RT>{
    @Override public boolean can_read(){
      return false;
    }

    @Override public RT read(){
      throw new UnsupportedOperationException("Cannot read from null topology.");
    }

    @Override public boolean can_step(){
      return false;
    }

    @Override public void step(){
      throw new UnsupportedOperationException("Cannot step over null topology.");
    }

    @Override public Topology topology(){
      return Topology.NULL;
    }
  }
  protected final TopoIface<RT> topo_null = new NullTopo();

  // good citizen
  //

  public String to_string_annotated(){

    test.print("::to_string_annotated");

    if(!is_mounted()) return "TM_SR_ND(NotMounted)";
    if(!can_read()) return "TM_SR_ND(Null)";

    // output takes two lines, starting from the left column on each
    StringBuilder data_channel = new StringBuilder("\n");
    StringBuilder control_channel = new StringBuilder("");
    
    data_channel.append( "TM_SR_ND(" ).append( topology().name()).append("(" );
    control_channel.append( " ".repeat(data_channel.length()-1) );

    String element = null;
    Ariadne_TM_SR_ND<RT> copy = this.entangle();
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

  // RT code format style comma separated list
  @Override public String toString(){

    Ariadne_TM_SR_ND<RT> tm = this.entangle();
    if( tm.can_rewind() ) tm.rewind();

    if( !tm.can_read() ) return "";

    StringBuilder output = new StringBuilder();
    do{

      if( tm.head_on_same_cell(this) ) output.append("[");
      RT x = tm.read();
      if( x != null ) output.append(x);
      if( tm.head_on_same_cell(this) ) output.append("]");

      if( !tm.can_step() ) break;

      tm.step();
      if( x == null )
        output.append(",");
      else
        output.append(" ,");

    }while(true);

    return output.toString();
  }


}
