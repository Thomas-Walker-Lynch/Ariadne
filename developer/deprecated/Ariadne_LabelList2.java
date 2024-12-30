/*
  A 'tape' as a model for computation is a sequence of cells, where something can be written or read from each cell. We talk about the sequence as though written on paper, running from left to right.  The elements in the sequence have neighbors, so the sequence of cells can be said to be mutually connected. This is to say that if cell B is to the right of cell A in the sequence, then cell A is to the left of cell B; Also if cell A is to the left of cell B, then cell B is to the right of cell A.

  One of the cells on the tape is specially marked as being the 'mounted cell'. This is the cell that can be written or read after the tape is mounted on a 'tape machine', and before any steps have been taken. 

  Information from the `topology` method will remain valid for as long as the topology of
  the tape is not modified.  

  A finite tape will have a leftmost cell, which has no left neighbor, and a rightmost cell, which has no right neighbor. All other cells will have two neighbors.

  A tape can have an infinite number of cells to the left of the mount point, to the right of the mount point, or in both directions. Hence it is possible that two, one, or zero cells on a tape have only one neighbor, where the zero neighbor case is for finite tapes, and the latter cases are for infinite tapes.

  An algorithm running on a tape machine that has a left going tape can be translated into an algorithm for a right going tape simply by swapping `step_right` for `step_left`. Hence there is no utility to be had by keeping both models.

  Another isomorphism can be setup between a single ended tape and a double direction tape by replacing each step by two steps, and then placing odd cell into correspondence with the right going tape, and even cells with left going tape.  Hence an algorithm implemented over the top of either can be mechanically transformed to an algorithm for the other.

  However, what we can not do without affecting the power of our computation machine is to
  eliminate 'step-left', yet this is a common simplification in data structures. The Lisp language is based on single linked lists for example.

  This 'Step Right Machine' (SRM) defined here can only be stepped to the right. Thus whether cells are mutually connected, or not, becomes irrelevant.  Also, saying 'leftmost' is a feature of the tape, becomes muddled, as the mount point cell will be the leftmost cell that is ever visited.

  A SRM can be defined using functions, or it can be used as an iterator for traversing through a container.

  The property methods defined here are kept general so that they can be used with other tape machines.
*/

package com.ReasoningTechnology.Ariadne;

import java.util.ArrayList;
import java.util.List;

public class Ariadne_SRM_LabelList extends Ariadne_SRM<Ariadne_Label>{

  // owned by the class
  public static Ariadne_LabelList make(){
    return new Ariadne_LabelList();
  }

  public static Ariadne_LabelList make( Object...label_list ){
    Ariadne_LabelList instance = new Ariadne_LabelList();
    instance.add( List.of( label_list ) );
    return instance;
  }

  // data owned by the instance
  private ArrayList<Ariadne_Label> list;
  private int current_index;

  // constructors
  protected Ariadne_LabelList(){
    super();
    this.list = new ArrayList<>();
    this.current_index = -1; // No label mounted initially
  }

  // instance interface
  @Override
  public Topology topology(){
    return list.isEmpty() ? Topology.NO_CELLS : Topology.SEGMENT;
  }

  @Override
  public Status status(){
    if( list.isEmpty() ){
      return Status.TAPE_NOT_MOUNTED;
    }
    if( current_index == 0 ){
      return Status.LEFTMOST;
    }
    if( current_index == list.size() - 1 ){
      return Status.RIGHTMOST;
    }
    return Status.INTERIM;
  }

  @Override
  public Ariadne_Label read(){
    if( current_index < 0 || current_index >= list.size() ){
      throw new UnsupportedOperationException( "Ariadne_SRM::read, out of bounds or unmounted tape." );
    }
    return list.get( current_index );
  }

  @Override
  public boolean step(){
    if( current_index + 1 < list.size() ){
      current_index++;
      return true;
    }
    return false; // Cannot step further
  }

  public boolean add( List<Object> obj_list ){
    boolean modified = false;
    if( obj_list != null ){
      for( Object obj : obj_list ){
        modified |= add_one( obj );
      }
    }
    return modified;
  }

  @Override
  public String toString(){
    return list.toString();
  }

  // private helpers
  private boolean add_one( Object obj ){
    if( obj instanceof String ){
      return add_one( (String) obj );
    } else if( obj instanceof Ariadne_Label ){
      return add_one( (Ariadne_Label) obj );
    }
    throw new IllegalArgumentException(
      "Ariadne_LabelList::add_one, cannot make label from object of type: " + obj.getClass().getName()
    );
  }

  private boolean add_one( String string ){
//    return list.add( Ariadne_Label.make( string ) );
    return false;
  }

  private boolean add_one( Ariadne_Label label ){
    return list.add( label );
  }
}
