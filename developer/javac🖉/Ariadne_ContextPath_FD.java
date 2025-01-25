package com.ReasoningTechnology.Ariadne;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// SiblingContext a tm of graph node labels, that are siblings to a path node, in a tree search
// LT == Label Type
public class Ariadne_ContextPath_FD
  <
    SiblingContext extends Ariadne_TM_SR_NX<LT> 
    ,LT extends Ariadne_Label
    >
{

  //----------------------------------------
  // Static

  public static
    <
     SiblingContext extends Ariadne_TM_SR_NX<LT> 
     ,LT extends Ariadne_Label
     >
    Ariadne_ContextPath_FD<SiblingContext ,LT> 
    make()
  {
    return new Ariadne_ContextPath_FD<>();
  }

  public static
    <
     SiblingContext extends Ariadne_TM_SR_NX<LT> 
     ,LT extends Ariadne_Label
     >
    Ariadne_ContextPath_FD<SiblingContext ,LT> 
    make(Ariadne_ContextPath_FD<SiblingContext ,LT> other)
 {
    return new Ariadne_ContextPath_FD<>();
  }


  //----------------------------------------
  // Instance data

  private final List<SiblingContext> context_path_list;
  private final HashSet<LT> path_member_set;
  private LT cycle_node_label;

  //----------------------------------------
  // Constructors

  public Ariadne_ContextPath_FD(){
    this.context_path_list = new ArrayList<>();
    this.path_member_set = new HashSet<>();
  }

  public Ariadne_ContextPath_FD(Ariadne_ContextPath_FD<SiblingContext ,LT> other){
    this.context_path_list = new ArrayList<>( other.context_path_list );
    this.path_member_set = new HashSet<>( other.path_member_set );
  }

  //----------------------------------------
  // Instance methods

  /**
   * Adds a traversal state to the context path.
   *
   * @param sibling_context The traversal state to add.
   * @return true if successfully added, false if it introduces a cycle.
   */
  public boolean push(SiblingContext sibling_context){
    if( sibling_context == null || !sibling_context.can_read() ){
      return false;
    }

    LT label = sibling_context.read();
    if( path_member_set.contains(label) ){
      cycle_node_label = label;
      return false; // Cycle detected
    }

    context_path_list.add( sibling_context );
    path_member_set.add(label);
    return true;
  }

  /**
   * Removes the last traversal state from the context path.
   *
   * @return The removed traversal state, or null if the path is empty.
   */
  public SiblingContext pop(){
    if( context_path_list.isEmpty() ){
      return null;
    }

    SiblingContext last = context_path_list.remove( context_path_list.size() - 1 );
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
    StringBuilder output = new StringBuilder("Ariadne_ContextPath_FD(");
    Ariadne_TM_SR_NX_F< SiblingContext > tm = Ariadne_TM_SR_NX_Array.make(context_path_list);
    if( tm.can_read() ){
      do{
        output.append("Sibling( ");
        output.append( tm.toString() );
        output.append(" )");
        if( !tm.can_step() ) break;
        tm.step();
        output.append(" ,");
      }while(true);
    }
    output.append(")");
    return output.toString();
  }

}

