/*
Ariadne_SRM with index

*/

package com.ReasoningTechnology.Ariadne;
import java.math.BigInteger;

public abstract class Ariadne_SRMI<T> extends Ariadne_SRM<T>{

  private BigInteger current_index;

  public Ariadne_SRMI(){
    this.current_index = BigInteger.ZERO;
  }

  public BigInteger index(){
    return current_index;
  }

  public void increment(){
    current_index = current_index.add(BigInteger.ONE);
  }
}
