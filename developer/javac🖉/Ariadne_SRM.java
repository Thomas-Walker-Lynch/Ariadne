/*
  Step Right Machine

  This is a mostly abstract base class.

  This is for single threaded execution.  The multiple thread model
  uses `mount` and `dismount` to lock the resources being iterated on.
 
*/

package com.ReasoningTechnology.Ariadne;

public class Ariadne_SRM<TElement> {

  public static <TElement> Ariadne_SRM<TElement> make(){
    return new Ariadne_SRM<>();
  }
  protected Ariadne_SRM(){
  }

  // machine and tape status/properties
  //

  public enum Topology{
    UNDEFINED
    ,NO_CELLS
    ,SINGLETON
    ,SEGMENT
    ,SEGMENT_OR_CIRCLE
    ,CIRCLE
    ,CIRCLE_OR_INFINITE_RIGHT
    ,INFINITE_RIGHT
    ;
  }
  public enum Location{
    OTHER
    ,LEFTMOST
    ,INTERIM
    ,RIGHTMOST
    ;
  }

  public Topology topology(){
    return Topology.UNDEFINED;
  }
  public Location location(){
    throw new UnsupportedOperationException("Ariadne_SRM::location not implemented.");
  }
  public boolean can_step() {
      return topology().ordinal() >= Topology.SEGMENT.ordinal()
          && location().ordinal() <= Location.INTERIM.ordinal();
  }
  public boolean can_read() {
      return topology().ordinal() >= Topology.SINGLETON.ordinal();
  }

  // moving the head
  //

  public void step(){
    throw new UnsupportedOperationException("Ariadne_SRM::step not implemented.");
  }

  public void rewind(){
    throw new UnsupportedOperationException("Ariadne_SRM::rewind not implemented.");      
  }

  public void fast_forward(){
    throw new UnsupportedOperationException("Ariadne_SRM::fast_forward not implemented.");      
  }

  // access
  //
  
  public TElement access(){ 
    // returns a reference, cell can then be read or written
    throw new UnsupportedOperationException("Ariadne_SRM::read not implemented.");
  }

  public TElement read(){
    TElement accessed_cell = access();   
    return deep_copy( accessed_cell );
  }
  private TElement deep_copy( TElement original ){
    throw new UnsupportedOperationException("Ariadne_SRM::deep_copy not implemented.");
  }

  // writes value
  public void write(TElement e){
    throw new UnsupportedOperationException("Ariadne_SRM::read not implemented.");
  }

}
