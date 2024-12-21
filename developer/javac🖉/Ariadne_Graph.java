package com.ReasoningTechnology.Ariadne;

import java.util.HashMap;
import java.util.Map;

public class Ariadne_Graph{

  // Test messaging
  //
    private static boolean test = false;
    public static void test_switch(boolean test){
      if (Ariadne_Graph.test && !test){
        test_print("Ariadne_Graph:: test messages off");
      }
      if (!Ariadne_Graph.test && test){
        test_print("Ariadne_Graph:: test messages on");
      }
    }
    private static void test_print(String message){
      if(test){
        System.out.println(message);
      }
    }

  // Class data (static data)
  //


  // Instance data and access
  //
    private Map<Ariadne_Label, Ariadne_Node> node_map;
    private Ariadne_ProductionList recognizer_f_list;

  // Constructors
  //
    public Ariadne_Graph(Map<Ariadne_Label, Ariadne_Node> node_map, Ariadne_ProductionList recognizer_f_list){
      if (node_map == null && recognizer_f_list == null){
        System.err.println("Ariadne_Graph: At least one of 'node_map' (Map) or 'recognizer_f_list' (List) must be provided.");
        System.exit(1);
      }

      // Initialize each of node_map and recognizer_f_list to empty collections if null
      this.node_map = (node_map != null) ? node_map : new HashMap<>();
      this.recognizer_f_list = (recognizer_f_list != null) ? recognizer_f_list : new Ariadne_ProductionList();
    }

  // Interface methods
  //

    // Lookup by label
    public Ariadne_Node lookup(Ariadne_Label node_label){
      if (node_label == null || node_label.isEmpty()){
        if (verbose){
          System.out.println("lookup:: given node_label is null or empty.");
        }
        return null;
      }

      // Try to retrieve the node from the map
      Ariadne_Node node = this.node_map.get(node_label);

      if(test)
        if(node == null) test_print("lookup:: node not found for label: " + node_label);
        else test_print("lookup:: found node: " + node);

      return node;
    }

  // standard interface
  //
  // need a toString ...

}
