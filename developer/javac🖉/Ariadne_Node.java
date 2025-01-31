/*
A Node potentially has an infinite number of neighbors, in which case its
neighbor set is defined using a function. See the IndexTree example.

A node extends from HashMap so to facilitate the user in adding his or
her own properties.


*/

package com.ReasoningTechnology.Ariadne;

import java.util.HashMap;
import java.util.HashSet;

// LT == Label Type
public class Ariadne_Node<LT extends Ariadne_Label> extends HashMap<String ,Object>{

  // Owned by the class
  //

  public static 
  <T extends Ariadne_Label> 
  Ariadne_Node<T> make(T label){
    return new Ariadne_Node<>(label);
  }

  // Data owned by the instance
  //

  private final LT label;
  private final HashSet<Ariadne_Token> mark_set;

  // Constructors
  // 

  protected Ariadne_Node(LT label){
    this.label = label;
    this.mark_set = new HashSet<>();
  }

  // Instance interface
  //
  
  public LT label(){
    return this.label;
  }

  // the returned machine should have the head on leftmost
  public Ariadne_TM_SR_NX<LT> neighbor(){
    throw new UnsupportedOperationException("Ariadne_Node::neighbor not implemented in the base class.");
  }

  public void mark(Ariadne_Token token){
    mark_set.add(token);
  }

  public boolean has_mark(Ariadne_Token token){
    return mark_set.contains(token);
  }

  public void remove_mark(Ariadne_Token token){
    mark_set.remove(token);
  }


  // good citizen
  // 

  @Override public String toString(){
    StringBuilder output = new StringBuilder();

    // Node representation
    if( label == null ){
      output.append( "Node()" );
    }else{
      output
        .append( "Node(" )
        .append( label.toString() )
        .append( ")" )
        ;
    }

    // Marks representation
    if( !mark_set.isEmpty() ){
      Ariadne_TM_SR_NX_Set srm = Ariadne_TM_SR_NX_Set.make(mark_set);
      output.append( " Mark(" );

      do{
        output.append( srm.read().toString() );
        if( !srm.can_step() ) break;
        output.append( ", " );
        srm.step();
      }while(true);

      output.append( ")" );
    }

    return output.toString();
  }

}
