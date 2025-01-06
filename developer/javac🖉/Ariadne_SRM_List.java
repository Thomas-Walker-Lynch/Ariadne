/*
  The Ariadne_SRM_List class provides a Step Right Machine (SRM) for linked lists.
  This implementation uses Java's ListIterator, which lacks a direct method
  to read the current element without advancing the iterator.

*/
package com.ReasoningTechnology.Ariadne;
import java.util.List;

public class Ariadne_SRM_List<T> extends Ariadne_SRM<T>{

  private final List<T> list;
  private int current_index;

  public Ariadne_SRM_List(List<T> list){
    if(list == null || list.isEmpty()){
      this.list = null; // not used, but what Java says, goes, if you want you code.
      set_state(state_null);
    }else{
      this.list = list;
      this.current_index = 0;
      set_state(state_segment);
    }
  }

  private final State state_null = new State(){
    @Override
    boolean can_read(){
      return false;
    }
    @Override
    boolean can_step(){
      return false;
    }
    @Override
    void step(){
      throw new UnsupportedOperationException("Cannot step from NULL state.");
    }
    @Override
    MachineState state(){
      return MachineState.NULL;
    }
  };

  private final State state_segment = new State(){
    @Override
    boolean can_read(){
      return true;
    }
    @Override
    boolean can_step(){
      return current_index < list.size() - 1;
    }
    @Override
    void step(){
      if(can_step()){
        current_index++;
      }else{
        set_state(state_rightmost);
      }
    }
    @Override
    MachineState state(){
      return MachineState.SEGMENT;
    }
  };

  private final State state_rightmost = new State(){
    @Override
    boolean can_read(){
      return true;
    }
    @Override
    boolean can_step(){
      return false;
    }
    @Override
    void step(){
      throw new UnsupportedOperationException("Cannot step from RIGHTMOST state.");
    }
    @Override
    MachineState state(){
      return MachineState.RIGHTMOST;
    }
  };

  @Override
  public T read(){
    if(!can_read()){
      throw new UnsupportedOperationException("Cannot read from NULL state.");
    }
    return list.get(current_index);
  }
}
