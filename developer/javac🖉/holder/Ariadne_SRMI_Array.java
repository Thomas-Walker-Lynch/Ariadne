package com.ReasoningTechnology.Ariadne;

import java.math.BigInteger;
import java.util.List;

public class Ariadne_SRMI_Array<T> extends Ariadne_SRMI<T>{

  private final List<T> array;

  public Ariadne_SRMI_Array(List<T> array){
    super(BigInteger.ZERO, array == null || array.isEmpty() ? BigInteger.ZERO : BigInteger.valueOf(array.size() - 1));

    if (array == null || array.isEmpty()){
      set_state(state_null);
    } else if (array.size() == 1){
      set_state(state_rightmost);
    } else{
      set_state(state_segment);
    }

    this.array = array;
  }

  private final ASRM state_null = new ASRM(){
    @Override public boolean can_read(){
      return false;
    }
    @Override public T read(){
      throw new UnsupportedOperationException("Cannot read from NULL state.");
    }
    @Override public boolean can_step(){
      return false;
    }
    @Override public void step(){
      throw new UnsupportedOperationException("Cannot step from NULL state.");
    }
    @Override public State state(){
      return State.NULL;
    }
  };

  private final ASRM state_segment = new ASRM(){
    @Override public boolean can_read(){
      return true;
    }
    @Override public void read(){
      return array.get(index().intValueExact());
    }
    @Override public boolean can_step(){
      return index().compareTo(rightmost_index().subtract(BigInteger.ONE)) < 0;
    }
    @Override public void step(){
      if (can_step()){
        seek(index().add(BigInteger.ONE));
      } else{
        set_state(state_rightmost);
      }
    }
    @Override public State state(){
      return State.SEGMENT;
    }
  };

  private final ASRM state_rightmost = new ASRM(){
    @Override public boolean can_read(){
      return true;
    }
    @Override public void read(){
      return array.get(index().intValueExact());
    }
    @Override public boolean can_step(){
      return false;
    }
    @Override public void step(){
      throw new UnsupportedOperationException("Cannot step from RIGHTMOST state.");
    }
    @Override public State state(){
      return State.RIGHTMOST;
    }
  };
}
