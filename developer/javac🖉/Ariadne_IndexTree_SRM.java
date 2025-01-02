package com.ReasoningTechnology.Ariadne;

import java.math.BigInteger;

public class Ariadne_IndexTree_SRM extends Ariadne_SRM<BigInteger[]> {

  private final BigInteger[] current_label;

  public static Ariadne_IndexTree_SRM make(BigInteger[] initial_label){
    return new Ariadne_IndexTree_SRM(initial_label);
  }

  protected Ariadne_IndexTree_SRM(BigInteger[] initial_label){
    super();
    if (initial_label == null || initial_label.length == 0) {
      throw new IllegalArgumentException("Initial label must not be null or empty.");
    }
    this.current_label = initial_label;
  }

  @Override
  public Topology topology(){
    return Topology.INFINITE_RIGHT;
  }

  @Override
  public BigInteger[] read(){
    // Return a reference to the current label
    return current_label;
  }

  @Override
  public boolean step(){
    // Increment the last element of the label (child counter)
    int lastIndex = current_label.length - 1;
    current_label[lastIndex] = current_label[lastIndex].add(BigInteger.ONE);
    return true;
  }

}
