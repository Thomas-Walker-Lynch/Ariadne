/*
  User defines a graph by implementing this interface.  For the build tool, the defined
  graph is dynamically loaded.

  Generally labels are returned and passed around. Only `lookup` returns a Node.

  In a wellformed graph, the labels returned by `start()` will be in the graph. This
  can be checked by calling `lookup`.
*/

package com.ReasoningTechnology.Ariadne;

public interface Ariadne_Graph<TLabel> {

  // returns list of TLabel
  Ariadne_SRM<TLabel> start();

  // lookup a Node by label
  Ariadne_Node<TLabel> lookup(TLabel label);

}
