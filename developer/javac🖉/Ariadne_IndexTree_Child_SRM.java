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

  private final Ariadne_SRM.State state_infinite_right = new Ariadne_SRM.State(){
    @Override boolean can_read(){
      return true;
    }
    @Override boolean can_step(){
      return true;
    }
    @Override void step(){
      increment_label();
    }
    @Override Ariadne_SRM.MachineState state(){
      return Ariadne_SRM.MachineState.INFINITE;
    }
  };

  private void increment_label(){
    label[label.length - 1] = super.index();
  }

  @Override public void step(){
    super.step();
    label[label.length - 1] = super.index();
  }

  @Override public BigInteger[] read(){
    return label;
  }
}
