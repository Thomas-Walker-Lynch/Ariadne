/*
Ariadne_SRM with index

*/

package com.ReasoningTechnology.Ariadne;
import java.math.BigInteger;

public abstract class Ariadne_SRMI<T> extends Ariadne_SRM<T>{

  private BigInteger current_index;
  private final BigInteger leftmost_index;
  private final BigInteger rightmost_index;

  public static 

  public Ariadne_SRMI(BigInteger leftmost_index, BigInteger rightmost_index){
    if(leftmost_index == null 
       || rightmost_index == null 
       || leftmost_index.compareTo(rightmost_index) > 0
       ){
      throw new IllegalArgumentException("Invalid tape bounds.");
    }
    this.leftmost_index = leftmost_index;
    this.rightmost_index = rightmost_index;
    this.current_index = leftmost_index;
  }

  public BigInteger index(){
    return current_index;
  }

  public BigInteger leftmost_index(){
    return leftmost_index;
  }

  public BigInteger rightmost_index(){
    return rightmost_index;
  }

  public void seek(BigInteger new_index){
    if(new_index.compareTo(leftmost_index) < 0 || new_index.compareTo(rightmost_index) > 0){
      throw new IndexOutOfBoundsException("Index out of bounds.");
    }
    this.current_index = new_index;
  }

  @Override
  public void step(){
    super.step();
    current_index = current_index.add(BigInteger.ONE);
  }
}
