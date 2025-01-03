/*
  Step Right Machine

  This is a mostly abstract base class.

*/

package com.ReasoningTechnology.Ariadne;

public class Ariadne_SRM<TElement> {

  public static <TElement> Ariadne_SRM<TElement> make(){
    return new Ariadne_SRM<>();
  }
  protected Ariadne_SRM(){
  }

  public enum Topology{
    NO_CELLS
    ,SEGMENT
    ,CIRCLE
    ,INFINITE_RIGHT
    ,UNKNOWN
    ,UNDEFINED
    ;
  }
  public Topology topology(){
    return Topology.UNDEFINED;
  }

  // categorizes the head location
  public enum Status{
    TAPE_NOT_MOUNTED
    ,LEFTMOST
    ,INTERIM
    ,RIGHTMOST
    ;
  }
  public Status status(){
    throw new UnsupportedOperationException("Ariadne_SRM::status not implemented.");
  }
  public boolean can_step(){
    return 
      status() == Status.LEFTMOST
      || status() == Status.INTERIM;
  }
  public boolean mounted(){
    return status() != Status.TAPE_NOT_MOUNTED;
  }

  public TElement read(){
    throw new UnsupportedOperationException("Ariadne_SRM::read not implemented.");
  }

  public void step(){
    if( !can_step() )
      throw new UnsupportedOperationException("Ariadne_SRM::step can not step.");      
  }

}
