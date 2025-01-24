package com.ReasoningTechnology.Ariadne;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// LT == Label Type
public class Ariadne_ContextPath_FD< Ariadne_TM_SR_NX<LT> >{

  //----------------------------------------
  // static

  ... make ...

  //----------------------------------------
  // Instance data

  private final List< Ariadne_TM_SR_NX<LT> > context_path_list;
  private final HashSet<LT> path_member_set;
  private Ariand_Label cycle_node_label;;

  //----------------------------------------
  // Constructors
  //

  public Ariadne_ContextPath(){
    this.context_path_list = new ArrayList<>();
    this.path_member_set = new HashSet<>(); 
  }

  public Ariadne_ContextPath(Ariadne_ContextPath<LT> other){
    this.context_path_list = new ArrayList<>( other.context_path_list );
    this.path_member_set = new HashSet<>( other.path_member_set );
  }

  //----------------------------------------
  // Instance methods

  /**
   * Adds a traversal state to the context path.
   *
   * @param tm The traversal state to add.
   * @return true if successfully added, false if it introduces a cycle.
   */
  public boolean push(Ariadne_TM_SR_NX<LT> tm){
    if( tm == null || !tm.can_read() ){
      return false;
    }

    LT label = tm.read();
    if( path_member_set.contains(label) ){
      cycle_node_label = label;
      return false; // Cycle detected
    }

    context_path_list.add(tm);
    path_member_set.add(label);
    return true;
  }

  /**
   * Removes the last traversal state from the context path.
   *
   * @return The removed traversal state, or null if the path is empty.
   */
  public Ariadne_TM_SR_NX<LT> pop(){
    if( context_path_list.isEmpty() ){
      return null;
    }

    Ariadne_TM_SR_NX<LT> last = context_path_list.remove( context_path_list.size() - 1 );
    path_member_set.remove( last.read() );
    return last;
  }

  /**
   * Checks if the context path contains the given label.
   *
   * @param label The label to check.
   * @return true if the label is in the path, false otherwise.
   */
  public boolean contains(LT label){
    return path_member_set.contains(label);
  }

  /**
   * Creates a copy of the current context path.
   *
   * @return A new ContextPath instance with the same state.
   */
  public Ariadne_ContextPath<LT> copy(){
    return new Ariadne_ContextPath<>(this);
  }

  /**
   * Returns the size of the context path.
   *
   * @return The number of traversal states in the path.
   */
  public int size(){
    return context_path_list.size();
  }

  /**
   * Provides a string representation of the context path for debugging.
   *
   * @return A string representing the context path.
   */
  @Override
  public String toString(){
    StringBuilder output = new StringBuilder("Ariadne_ContextPath(");
    @SuppressWarnings("unchecked")
      Ariadne_TM_SR_NX<Ariadne_TM_SR_NX<LT>> tm = Ariadne_TM_SR_NX_Array.make(context_path_list);
    if( tm.can_read() ){
      do{

        if( tm.head_on_same_cell(this) ){
          output.append("[Sibling(");
        } else {
          output.append("Sibling(");
        }
        output.append( tm.toString() );
        if( tm.head_on_same_cell(this) )
          output.append(")]");
        else
          output.append(")");

        if( !tm.can_step() ) break;
        tm.step();
        output.append(" ,");

      }while(true);
    }

    output.append(")");
    return output.toString();
  }

}
