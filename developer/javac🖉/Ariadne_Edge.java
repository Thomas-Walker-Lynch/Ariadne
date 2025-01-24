/*

An edge is a property name-value map. It is keyed in a graph based on
two node labels. For purposes of introspection, we keep a copy of the
two labels in each Edge, similar how a Node keeps a copy of its label.

*/

package com.ReasoningTechnology.Ariadne;

import java.util.HashMap;
import java.util.HashSet;

// LT == Label Type
public class Ariadne_Edge <LT extends Ariadne_Label> extends HashMap<String, Object>{

  // Owned by the class
  public static <T extends Ariadne_Label> Ariadne_Edge<T> make(T label_0 ,T label_1){
    return new Ariadne_Edge<>(label_0 ,label_1);
  }

  // instance data
  LT label_0;
  LT label_1;

  // Constructors
  protected Ariadne_Edge(LT label_0 ,LT label_1){
    this.label_0 = label_0;
    this.label_1 = label_1;
  }

  // Object interface
  @Override public String toString(){
    StringBuilder output = new StringBuilder();
    output
      .append( "Edge(" )
      .append( label_0 )
      .append( " ,") 
      .append( label_1 )
      .append( ")" )
      ;
    return output.toString();
  }

}
