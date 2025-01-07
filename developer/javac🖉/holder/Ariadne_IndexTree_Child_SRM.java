package com.ReasoningTechnology.Ariadne;

import java.math.BigInteger;

public class Ariadne_IndexTree_Child_SRM extends Ariadne_SRMI<BigInteger[]>{

  private BigInteger[] label;

  public static Ariadne_IndexTree_Child_SRM make(BigInteger[] initial_label){
    return new Ariadne_IndexTree_Child_SRM(initial_label);
  }

  protected Ariadne_IndexTree_Child_SRM(BigInteger[] initial_label){
    super(BigInteger.ZERO ,null);
    if(initial_label == null || initial_label.length == 0){
      throw new IllegalArgumentException("Initial label must not be null or empty.");
    }
    this.label = initial_label;
    set_state(state_infinite_right);
  }

  private final Ariadne_SRM.ASRM state_infinite_right = new Ariadne_SRM.ASRM(){
    @Override public boolean can_read(){
      return true;
    }
    @Override public BigInteger[] read(){
      return label;
    }
    @Override public boolean can_step(){
      return true;
    }
    @Override public void step(){
      label[label.length - 1] = super.index();
    }
    @Override public Ariadne_SRM.State state(){
      return Ariadne_SRM.State.INFINITE;
    }
  };

}
