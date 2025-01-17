/*
  User defines a graph by implementing this interface. For the build tool, the defined
  graph is dynamically loaded.

  Generally labels are returned and passed around. Only `lookup` returns a Node.

  In a wellformed graph, the labels returned by `start()` will be in the graph. This
  can be checked by calling `lookup`.
*/

package com.ReasoningTechnology.Ariadne;

// LT = Label Type
public class Ariadne_Graph<LT extends Ariadne_Label>{

  public static Ariadne_Graph make(){
    return new Ariadne_Graph();
  }
  protected Ariadne_Graph(){
  }

  public Ariadne_TM_SR_ND<LT> start(){
    throw new UnsupportedOperationException("Ariadne_Graph::start.");
  }

  public Ariadne_Node lookup_node(LT label){
    throw new UnsupportedOperationException("Ariadne_Graph::lookup.");
  }

  public Ariadne_Node lookup_edge(LT label0 ,LT label1){
    throw new UnsupportedOperationException("Ariadne_Graph::lookup.");
  }


}
