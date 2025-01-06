/*
A node extends a Map. This map is for use by the user to add properties to the node.
It is not used by the Ariadne code.  The class itself is already a sort of map, so
node specific fields are expressed in the class itself.  

Node specific fields include the node label, and a set for Ariadne algorithms
to use when adding token marks to nodes.  An extension will have to add
a function or data set to hold the labels of neighboring nodes.

Currently node labels are strings. I should probably have made them a
generic type.

A graph itself is a similar data structure to the Node.  A graph is defined by its
lookup function, that makes it a map.  Also there is a start function that returns
node labels, while a node has 'neighbor' which returns node labels.  Here are the
differences:

  The graph type lookup implementation is not constrained to any type, and
  could be a function.  The node lookup comes from a HashMap, and thus is
  guaranteed to have a finite number of entries.

  The graph type is defined by the user, where as the node type is defined
  by the programmer who is creating a graph based application.

We should take a closer look at the possibility of unifying these later.

*/

package com.ReasoningTechnology.Ariadne;

import java.util.HashMap;
import java.util.HashSet;

public class Ariadne_Node<TLabel> extends HashMap<String, Object> {

  // Owned by the class
  public static <TLabel> Ariadne_Node<TLabel> make(TLabel label) {
    return new Ariadne_Node<>(label);
  }

  // Data owned by the instance
  private final TLabel label;
  private final HashSet<Ariadne_Token> markSet;
  private static final String NEIGHBOR_PROPERTY_NAME = "neighbor_property";

  // Constructors
  public Ariadne_Node(TLabel label) {
    super();
    this.label = label;
    this.markSet = new HashSet<>();
  }

  // Instance interface
  public TLabel label() {
    return this.label;
  }

  public Ariadne_SRM<TLabel> neighbor(){
    throw new UnsupportedOperationException("Neighbor is not implemented in the base class.");
  }

  public void mark(Ariadne_Token token) {
    markSet.add(token);
  }

  public boolean hasMark(Ariadne_Token token) {
    return markSet.contains(token);
  }

  public void removeMark(Ariadne_Token token) {
    markSet.remove(token);
  }

  // Object interface
  @Override
  public String toString() {
    return "Ariadne_Node{"
           + "label=" + label
           + " ,markSet=" + markSet
           + "}";
  }
}
