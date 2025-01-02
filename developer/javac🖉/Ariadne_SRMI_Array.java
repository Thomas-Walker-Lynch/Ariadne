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

  public void mount(List<T> list ,Semaphore semaphore){
    if(this.list != null){
      throw new IllegalStateException("Ariadne_SRMI_Array::mount tape already mounted.");
    }
    if(list == null){
      throw new IllegalArgumentException("Ariadne_SRMI_Array::mount list cannot be null.");
    }
    this.list = list;
    this.lock = semaphore;
    if(lock != null){
      try{
        lock.acquire();
      } catch(InterruptedException e){
        Thread.currentThread().interrupt();
        throw new IllegalStateException("Ariadne_SRMI_Array::mount interrupted while acquiring lock.");
      }
    }
    set_status(list.isEmpty() ? Status.TAPE_NOT_MOUNTED : Status.LEFTMOST);
  }

  public void unmount(){
    if(this.list == null){
      throw new IllegalStateException("Ariadne_SRMI_Array::unmount no tape mounted.");
    }
    this.list = null;
    set_status(Status.TAPE_NOT_MOUNTED);
    if(lock != null){
      lock.release();
      lock = null;
    }
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
    if(!can_read()){
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
      set_status(index().equals(rightmost_index()) ? Status.RIGHTMOST : Status.INTERIM);
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
