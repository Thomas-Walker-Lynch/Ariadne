/*
Ariadne_SRMT with index

*/

package com.ReasoningTechnology.Ariadne;
import java.math.BigInteger;

public abstract class Ariadne_SRMTI extends Ariadne_SRMT{

  private BigInteger current_index;

  public Ariadne_SRMTI(){
    this.current_index = BigInteger.ZERO;
  }

  public BigInteger index(){
    return current_index;
  }

  public void increment(){
    current_index = current_index.add(BigInteger.ONE);
  }
}
