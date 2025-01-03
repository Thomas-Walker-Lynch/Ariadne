package com.ReasoningTechnology.Ariadne;

import java.math.BigInteger;
import java.util.Arrays;

public class Ariadne_IndexTree_Node extends Ariadne_Node<BigInteger[]> {

  public static Ariadne_IndexTree_Node make(BigInteger[] label){
    return new Ariadne_IndexTree_Node(label);
  }

  public Ariadne_IndexTree_Node(BigInteger[] label){
    super(label);
  }

  @Override
  public Ariadne_IndexTree_Child_SRM neighbor(){
    // Copy the current label
    BigInteger[] parentLabel = this.label();
    BigInteger[] childLabel = new BigInteger[parentLabel.length + 1];
    System.arraycopy(parentLabel, 0, childLabel, 0, parentLabel.length);

    childLabel[parentLabel.length] = BigInteger.ZERO;

    return Ariadne_IndexTree_Child_SRM.make(childLabel);
  }

  @Override
  public String toString(){
    return 
      "<" 
      + String.join("," ,Arrays.stream(label()).map(BigInteger::toString).toArray(String[]::new)) 
      + ">";
  }
}
