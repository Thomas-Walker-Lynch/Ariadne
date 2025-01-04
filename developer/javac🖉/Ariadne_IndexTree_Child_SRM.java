package com.ReasoningTechnology.Ariadne;

import java.math.BigInteger;

public class Ariadne_IndexTree_Child_SRM extends Ariadne_SRM<BigInteger[]> {

  private BigInteger[] label;

  public static Ariadne_IndexTree_Child_SRM make(BigInteger[] initial_label){
    return new Ariadne_IndexTree_Child_SRM(initial_label);
  }

  protected Ariadne_IndexTree_Child_SRM(BigInteger[] initial_label){
    super();
    if (initial_label == null || initial_label.length == 0) {
      throw new IllegalArgumentException("Initial label must not be null or empty.");
    }
    this.label = initial_label;
  }

  @Override
  public Topology topology(){
    return Topology.INFINITE_RIGHT;
  }

  @Override
  public BigInteger[] access(){
    // Return a reference to the current label
    return label;
  }

  @Override
  public void step(){
    int max_index = label.length - 1;
    label[max_index] = label[max_index].add(BigInteger.ONE);
  }

}
