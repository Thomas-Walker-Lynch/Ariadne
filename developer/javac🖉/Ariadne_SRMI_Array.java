package com.ReasoningTechnology.Ariadne;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Ariadne_SRMI_Array<T> extends Ariadne_SRMI<T>{

  private List<T> list;
  private Semaphore lock;

  public static <T> Ariadne_SRMI_Array<T> make(){
    return new Ariadne_SRMI_Array<>();
  }

  protected Ariadne_SRMI_Array(){
    super();
  }

  @Override
  public Topology topology(){
    if(list == null || list.isEmpty()){
      return Topology.NO_CELLS;
    }
    return Topology.SEGMENT;
  }

  @Override
  public Status status(){
    return super.status();
  }

  @Override
  public T read(){
    if(!mounted()){
      throw new UnsupportedOperationException("Ariadne_SRMI_Array::read tape not mounted or out of bounds.");
    }
    return list.get(index().intValue());
  }

  @Override
  public void step(){
    if(!can_step()){
      throw new UnsupportedOperationException("Ariadne_SRMI_Array::step, cannot step further.");
    }
    if(index().compareTo(rightmost_index()) < 0){
      index = index.add(BigInteger.ONE);
//      set_status(index().equals(rightmost_index()) ? Status.RIGHTMOST : Status.INTERIM);
    } else{
      throw new UnsupportedOperationException("Ariadne_SRMI_Array::step, no more cells.");
    }
  }

  @Override
  public BigInteger rightmost_index(){
    if(list == null){
      throw new UnsupportedOperationException("Ariadne_SRMI_Array::rightmost_index no tape mounted.");
    }
    return BigInteger.valueOf(list.size() - 1);
  }

}
