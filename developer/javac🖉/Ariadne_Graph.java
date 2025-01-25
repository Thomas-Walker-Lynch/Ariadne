/*
  User defines a graph by implementing this interface. Not all applications will define all methods here. 

  For the build tool, the defined graph is dynamically loaded. 

  Instances of `Label` are returned and passed around. No method takes a `Node` as an argument, and only the method `node()` returns a `Node`.

  Infinite and/or non-deterministic graphs are possible. See `example/GraphIndexTree` for an example deterministic infinite Graph.

  For a graph to be `wellformed()`:
  1. For `Node`s in the graph, no two `Node`s have `Label`s that compare `equals`.
  2. `lookup_node` will return a node for each read value on the TM returned by `start()` 

  Generally to know if an infinite or non-deterministic graph is Wellformed will require a proof. A diagonal iterator can be used on a deterministic infinite graph to get a
  deterministic finite prefix graph, again see example/GraphIndexTree.

  For finite deterministic graphs inherit from `Graph_FD`.

*/

package com.ReasoningTechnology.Ariadne;

// LT = Label Type, NT = Node Type
public class Ariadne_Graph<LT extends Ariadne_Label, NT extends Ariadne_Node<LT>> {

  // static
  //

  public enum Wellformed {
    TRUE
    ,FALSE
    ,UNKNOWN
  }

  public static < T extends Ariadne_Label, N extends Ariadne_Node<T> > Ariadne_Graph<T, N> make() {
    return new Ariadne_Graph<>();
  }

  public static <T extends Ariadne_Label, N extends Ariadne_Node<T>> Ariadne_Graph<T, N> make(String name) {
    return new Ariadne_Graph<>(name);
  }

  // instance data
  // 

  private final String name;

  // constructors
  //

  protected Ariadne_Graph() {
    this.name = null;
  }

  protected Ariadne_Graph(String name) {
    this.name = name;
  }

  // implements instance interface
  //

  public String name() {
    return name;
  }

  public boolean is_wellformed() {
    throw new UnsupportedOperationException("Ariadne_Graph::is_wellformed.");
  }

  public Ariadne_TM_SR_NX<LT> start() {
    throw new UnsupportedOperationException("Ariadne_Graph::start.");
  }

  // lookup a node on the graph by label
  public NT node(LT label) {
    throw new UnsupportedOperationException("Ariadne_Graph::node.");
  }

  // lookup an edge by two labels, for a directed graph label0 is the origin node label
  public Ariadne_Edge edge(LT label0, LT label1) {
    throw new UnsupportedOperationException("Ariadne_Graph::edge.");
  }

  // good citizen
  // 

  @Override
  public String toString() {
    throw new UnsupportedOperationException("Ariadne_Graph::toString.");
  }
}
