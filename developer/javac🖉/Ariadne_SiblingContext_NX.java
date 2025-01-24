/*
Currently SiblingContext is an alias for Ariadne_TM_SR_NX<LT>.

*/
package com.ReasoningTechnology.Ariadne;

import java.math.BigInteger;

// LT == Label Type
public abstract class 
  Ariadne_SiblingContext<LT extends Ariadne_Label>
  extends Ariadne_TM_SR_NX_Array<LT>
{

  // static
  //

  public static <T extends Ariadne_Label> Ariadne_SiblingContext<T> make(){
    throw new UnsupportedOperationException("Ariadne_SiblingContext::make not implemented.");
  }

  // instance data
  //

  // constructor(s)
  //

  protected Ariadne_SiblingContext(){
    super();
  }

}
