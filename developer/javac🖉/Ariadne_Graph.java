package com.ReasoningTechnology.Ariadne;

/*
  To define a graph, extend this class and define `start` and `lookup`.

  For a wellformed graph, each start label will be a label for a node found in the graph.
*/

public interface Ariadne_Graph{

  public static Ariadne_Graph make(Object...obj_list){
    return new Ariadne_Graph();
  }

  public Ariadne_SRM<Ariadne_Label> start();
  public Ariadne_Node lookup(String label);

}
