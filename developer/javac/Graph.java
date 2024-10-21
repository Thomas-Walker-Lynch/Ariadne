package com.ReasoningTechnology.Ariadne;

import java.util.HashMap;
import java.util.Map;

public class Graph{

  /*--------------------------------------------------------------------------------
    constructors
  */

  public Graph(Map<Label ,Node> node_map ,ProductionList recognizer_f_list){
    if(node_map == null && recognizer_f_list == null){
      System.err.println("AriadneGraph: At least one of 'node_map' (Map) or 'recognizer_f_list' (List) must be provided.");
      System.exit(1);
    }

    // Initialize each of node_map and recognizer_f_list to empty collections if null
    this.node_map = (node_map != null) ? node_map : new HashMap<Label ,Node>();
    this.recognizer_f_list = (recognizer_f_list != null) ? recognizer_f_list : new ProductionList();
  }

  /*--------------------------------------------------------------------------------
   instance data 
  */

  private static boolean debug = true;
  private Map<Label ,Node> node_map;
  private ProductionList recognizer_f_list;

  /*--------------------------------------------------------------------------------
   interface
  */

  // Lookup method to find a node by its label
  public Node lookup(Label node_label ,boolean verbose){
    if( node_label == null || node_label.isEmpty() ){
      if(verbose){
        System.out.println("lookup:: given node_label is null or empty.");
      }
      return null;
    }

    // Try to retrieve the node from the map
    Node node = this.node_map.get(node_label);

    if(verbose){
      if(node != null){
        System.out.println("lookup:: found node: " + node);
      } else {
        System.out.println("lookup:: node not found for label: " + node_label);
      }
    }

    return node;
  }

  // Overloaded lookup method with default verbosity (true)
  public Node lookup(Label node_label){
    return lookup(node_label ,true);
  }


  

}
