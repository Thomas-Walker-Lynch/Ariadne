/*
A node with a finite fixed set of neighbors.

*/

package com.ReasoningTechnology.Ariadne;

import java.util.HashSet;
import java.util.Arrays;
import java.util.List;

// LT == Label Type
public class Ariadne_Node_FD<LT extends Ariadne_Label> extends Ariadne_Node<LT>{

  // Owned by the class
  //

  // Data owned by the instance
  //

  // Constructors
  // 

  protected Ariadne_Node_FD(LT label){
    super(label);
  }

  // Instance interface
  //
  
  @Override public Ariadne_TM_SR_NX_F<LT> neighbor(){
    throw new UnsupportedOperationException("Ariadne_Node_F::neighbor not implemented in the base class.");
  }

  // Good citizen
  //

  @Override public String toString(){

    boolean has_label = label() != null;

    Ariadne_TM_SR_NX_F<LT> tm = neighbor();
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
