package com.ReasoningTechnology.Ariadne;

import java.util.HashMap;
import java.util.Map;

public class Ariadne_Graph {

  /*--------------------------------------------------------------------------------
    constructors
  */

  public Ariadne_Graph(Map<Ariadne_Label, Ariadne_Node> node_map, Ariadne_ProductionList recognizer_f_list) {
    if (node_map == null && recognizer_f_list == null) {
      System.err.println("Ariadne_Graph: At least one of 'node_map' (Map) or 'recognizer_f_list' (List) must be provided.");
      System.exit(1);
    }

    // Initialize each of node_map and recognizer_f_list to empty collections if null
    this.node_map = (node_map != null) ? node_map : new HashMap<>();
    this.recognizer_f_list = (recognizer_f_list != null) ? recognizer_f_list : new Ariadne_ProductionList();
  }

  /*--------------------------------------------------------------------------------
   instance data 
  */

  private static boolean debug = true;
  private Map<Ariadne_Label, Ariadne_Node> node_map;
  private Ariadne_ProductionList recognizer_f_list;

  /*--------------------------------------------------------------------------------
   interface
  */

  // Lookup method to find a node by its label
  public Ariadne_Node lookup(Ariadne_Label node_label, boolean verbose) {
    if (node_label == null || node_label.isEmpty()) {
      if (verbose) {
        System.out.println("lookup:: given node_label is null or empty.");
      }
      return null;
    }

    // Try to retrieve the node from the map
    Ariadne_Node node = this.node_map.get(node_label);

    if (verbose) {
      if (node != null) {
        System.out.println("lookup:: found node: " + node);
      } else {
        System.out.println("lookup:: node not found for label: " + node_label);
      }
    }

    return node;
  }

  // Overloaded lookup method with default verbosity (true)
  public Ariadne_Node lookup(Ariadne_Label node_label) {
    return lookup(node_label, true);
  }
}
