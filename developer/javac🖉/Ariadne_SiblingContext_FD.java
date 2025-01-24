package com.ReasoningTechnology.Ariadne;

import java.util.ArrayList;
import java.util.List;

// LT == Label Type
public class Ariadne_SiblingContext_FD<LT extends Ariadne_Label>
  extends Ariadne_SiblingContext<LT>
{

  // Instance data
  //

  private final List<LT> sibling_list;
  private int head_index;

  // Constructors
  //

  public Ariadne_SiblingContext_FD(List<LT> siblings){
    this.sibling_list = new ArrayList<>(siblings);
    this.head_index = 0;
  }

  // Instance methods
  //

  @Override public boolean can_read(){
    return head_index < sibling_list.size();
  }

  @Override public LT read(){
    if( !can_read() ){
      throw new IllegalStateException("Cannot read: No siblings at current position.");
    }
    return sibling_list.get(head_index);
  }

  @Override public boolean can_step(){
    return head_index < sibling_list.size() - 1;
  }

  @Override public void step(){
    if( !can_step() ){
      throw new IllegalStateException("Cannot step: No more siblings.");
    }
    head_index++;
  }

  @Override public boolean can_rewind(){
    return head_index > 0;
  }

  @Override public void rewind(){
    if( !can_rewind() ){
      throw new IllegalStateException("Cannot rewind: Already at the first sibling.");
    }
    head_index--;
  }
}
