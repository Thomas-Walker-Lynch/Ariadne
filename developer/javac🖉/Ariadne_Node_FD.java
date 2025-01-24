/*
A node with a finite fixed set of neighbors.


*/

package com.ReasoningTechnology.Ariadne;

import java.util.HashSet;
import java.util.Arrays;

// LT == Label Type
public class Ariadne_Node_FD<LT extends Ariadne_Label> extends Ariadne_Node<LT>{

  // Owned by the class
  //

  @SafeVarargs
  public static <T extends Ariadne_Label> Ariadne_Node_FD<T> make(T label ,T... neighbors){
    return new Ariadne_Node_FD<>(label ,neighbors);
  }

  // Data owned by the instance
  //

  private final HashSet<LT> neighbor_set;

  // Constructors
  // 

  @SafeVarargs
  protected Ariadne_Node_FD(LT label ,LT... neighbors){
    super(label);
    this.neighbor_set = new HashSet<>();
    if( neighbors != null ){
      this.neighbor_set.addAll(Arrays.asList(neighbors));
    }
  }

  // Instance interface
  //
  
  @Override public Ariadne_TM_SR_NX<LT> neighbor(){
    return Ariadne_TM_SR_NX_Set.make(neighbor_set);
  }

  // Good citizen
  //

  @Override public String toString(){
    Ariadne_TM_SR_NX_Set<LT> tm = Ariadne_TM_SR_NX_Set.make(neighbor_set);
    boolean has_label = label() != null;
    boolean has_neighbor = tm.can_read();

    if( !has_label && !has_neighbor ){
      return "Node_FD()";
    }

    StringBuilder output = new StringBuilder();

    if( has_label && !has_neighbor ){
      output
        .append( "node_FD(" )
        .append( label().toString() )
        .append( ")" );
      return output.toString();
    }

    // must has_neighbor at this point

    if( !has_label )
      output.append( "Node_FD(," );
    else
      output
        .append( "node_FD(" )
        .append( label().toString() )
        .append(" ,");

    output
      .append( tm.toString() )
      .append( ")" );

    return output.toString();
  }

}
