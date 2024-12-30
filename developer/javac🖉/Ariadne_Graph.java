/*
  User defines a graph by implementing this interface.  For the build tool, the defined
  graph is dynamically loaded.

  In a wellformed graph, the labels returned by `start()` will be in the graph. This
  can be checked by calling `lookup`.
*/

package com.ReasoningTechnology.Ariadne;

public interface Ariadne_Graph {

  // one or more nodes for starting graph traversals
  Ariadne_SRM<Ariadne_Label> start();

  // Method to look up a node by label
  Ariadne_Node lookup( Ariadne_Label label );

}
