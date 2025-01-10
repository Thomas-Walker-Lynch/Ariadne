/*
Ariadne_SRTM with index

*/

package com.ReasoningTechnology.Ariadne;
import java.math.BigInteger;

public abstract class Ariadne_SRTMI extends Ariadne_SRTM{

  private BigInteger current_index;

  public Ariadne_SRTMI(){
    this.current_index = BigInteger.ZERO;
  }

  public BigInteger index(){
    return current_index;
  }

  public void increment(){
    current_index = current_index.add(BigInteger.ONE);
  }
}
