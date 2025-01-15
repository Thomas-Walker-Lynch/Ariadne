/*
  User defines a graph by implementing this interface. For the build tool, the defined
  graph is dynamically loaded.

  Generally labels are returned and passed around. Only `lookup` returns a Node.

  In a wellformed graph, the labels returned by `start()` will be in the graph. This
  can be checked by calling `lookup`.
*/

package com.ReasoningTechnology.Ariadne;

public class Ariadne_Graph{

  public static Ariadne_Graph make(){
    return new Ariadne_Graph();
  }
  protected Ariadne_Graph(){
  }

  public Ariadne_TM_SR_ND start(){
    throw new UnsupportedOperationException("Ariadne_Graph::start.");
  }

  public Ariadne_Node lookup(Ariadne_Label label){
    throw new UnsupportedOperationException("Ariadne_Graph::lookup.");
  }

}
