package com.ReasoningTechnology.Ariadne;

import java.math.BigInteger;

public class Ariadne_IndexTree_Graph implements Ariadne_Graph<BigInteger[]>{

  @Override
  public Ariadne_IndexTree_SRM start(){
    return Ariadne_IndexTree_SRM.make(new BigInteger[0]);
  }

  @Override
  public Ariadne_IndexTree_Node lookup(BigInteger[] label){
    return Ariadne_IndexTree_Node.make(label);
  }

}
