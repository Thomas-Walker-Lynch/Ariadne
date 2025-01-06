package com.ReasoningTechnology.Ariadne;

import java.math.BigInteger;
import java.util.Arrays;

public class Ariadne_IndexTree_Node extends Ariadne_Node<BigInteger[]>{

  public static Ariadne_IndexTree_Node make(BigInteger[] label){
    return new Ariadne_IndexTree_Node(label);
  }

  private final BigInteger[] first_child_label;
 
  public Ariadne_IndexTree_Node(BigInteger[] label){
    super(label);
    this.first_child_label = new BigInteger[label.length + 1];
    System.arraycopy(label, 0, this.first_child_label, 0, label.length);
    this.first_child_label[label.length] = BigInteger.ZERO;
  }

  @Override
  public Ariadne_IndexTree_Child_SRM neighbor(){
    return Ariadne_IndexTree_Child_SRM.make(first_child_label);
  }

  @Override
  public String toString(){
    return 
      "<" 
      + String.join("," ,Arrays.stream(label()).map(BigInteger::toString).toArray(String[]::new)) 
      + ">";
  }
}
