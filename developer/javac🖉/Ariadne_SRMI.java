/*
Ariadne_SRM with index

*/

package com.ReasoningTechnology.Ariadne;
import java.math.BigInteger;

public class Ariadne_SRMI<T> extends Ariadne_SRM<T>{

  private BigInteger index;

  public static <T> Ariadne_SRMI<T> make(){
    return new Ariadne_SRMI<T>();
  }
  protected Ariadne_SRMI(){
    super();
    index = BigInteger.ZERO;;
  }

  @Override
  public boolean step(){
    throw new UnsupportedOperationException("Ariadne_SRMI::can't step unmounted tape.");
    // index.add(BigInteger.ONE);
  }

  BigInteger index(){return index;}
  BigInteger leftmost_index(){
    return BigInteger.ZERO;
  }
  BigInteger rightmost_index(){
    throw new UnsupportedOperationException("Ariadne_SRMI:: rightmost_index() of undefined.");
  }

}
