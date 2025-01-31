/*
A node with a finite fixed set of neighbors.


*/

package com.ReasoningTechnology.Ariadne;

import java.util.HashSet;
import java.util.Arrays;

// LT == Label Type
public class Ariadne_Node_Set<LT extends Ariadne_Label> extends Ariadne_Node_FD<LT>{

  // Owned by the class
  //

  @SafeVarargs
  public static <T extends Ariadne_Label> Ariadne_Node_Set<T> make(T label ,T... neighbors){
    return new Ariadne_Node_Set<>(label ,neighbors);
  }

  // Data owned by the instance
  //

  private final HashSet<LT> neighbor_set;

  // Constructors
  // 

  @SafeVarargs
  protected Ariadne_Node_Set(LT label ,LT... neighbors){
    super( label );
    this.neighbor_set = new HashSet<>();
    if( neighbors != null ){
      this.neighbor_set.addAll(Arrays.asList(neighbors));
    }
  }

  // Instance interface
  //
  
  @Override public Ariadne_TM_SR_NX_F<LT> neighbor(){
    //    return Ariadne_TM_SR_NX_Set.make(neighbor_set);
    return Ariadne_TM_SR_NX_Set.<LT>make(neighbor_set);
  }

  // Good citizen
  //


}
