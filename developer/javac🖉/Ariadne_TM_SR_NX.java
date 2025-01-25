/*
A step right only, non-destructive tape operations, tape machine, TM_SR_NX. This
machine has explicit tracking of the head address.

BigInteger is used so that it will be possible to bind TM_SR_NX machine extensions
to file system objects.

It is an interesting question as to whether a TM_SR should support rewind, which
is similar to stepping left. However, a new machine can always be made from
the same initial data, and would have its head on the leftmost cell. Hence, the
effect of adding rewind is to require that the machine keep enough information
to perform the same function that 'make' originally performed.


*/

package com.ReasoningTechnology.Ariadne;
import java.math.BigInteger;

// RT == read type
public class Ariadne_TM_SR_NX<RT>{

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

  public static Ariadne_TM_SR_NX make(){
    return new Ariadne_TM_SR_NX();
  }

  // instance data
  //

  private int id;
  protected Ariadne_Test test = null;
  protected TopoIface<RT> current_topology;
  protected BigInteger index;

  // constructor(s)
  //

  protected Ariadne_TM_SR_NX(){
    id = id_well++;
    test = Ariadne_Test.make("Ariadne_TM_SR_NX::" + id + "::");
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

  public boolean head_on_same_cell(Ariadne_TM_SR_NX<RT> tm){
    boolean p = this.index.equals(tm.index);
    if( test.is_on() ){
      test.print("head_on_same_cell this id/index: " + this.id() + "/" + this.index );
      test.print("head_on_same_cell tm id/index: " + tm.id() + "/" + tm.index );
      test.print("head_on_same_cell returning: " + p );
    }
    return p;
  }

  protected void entangle(Ariadne_TM_SR_NX<RT> copy){
    copy.current_topology = this.current_topology;
    // Nuance here, BigInteger is immutable, so operation on the original
    // index, and the copy index, will be independent, which is what we want.
    copy.index = this.index; 
  }
  public Ariadne_TM_SR_NX<RT> entangle(){
    throw new UnsupportedOperationException("Ariadne_TM_SR_NX::entangle not implemented.");
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
      throw new UnsupportedOperationException("Ariadne_TM_SR_NX::NotMounted::read.");
    }
    @Override public boolean can_step(){
      return false;
    }
    @Override public void step(){
      throw new UnsupportedOperationException("Ariadne_TM_SR_NX::NotMounted::step.");
    }
    @Override public Topology topology(){
      throw new UnsupportedOperationException("Ariadne_TM_SR_NX::NotMounted::topology.");
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


}
