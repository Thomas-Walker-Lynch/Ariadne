package com.ReasoningTechnology.Ariadne;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Ariadne_GraphDirectedAcyclic extends Ariadne_Graph{

  // test messaging
  //
    private static boolean test = false;
    public static void test_switch(boolean test){
      if(Ariadne_Graph.test && !test){
        test_print("Ariadne_Graph:: test messages off");
      }
      if(!Ariadne_Graph.test && test){
        test_print("Ariadne_Graph:: test messages on");
      }
    }
    private static void test_print(String message){
      if(test){
        System.out.println(message);
      }
    }

  // class data
  //

    // marks this class might put on a node
    public static Ariadne_TokenSet node_marks = new Ariadne_TokenSet(){{
      add(new Ariadne_Token("empty_root_label_list"));
      add(new Ariadne_Token("cycle_exists"));
      add(new Ariadne_Token("undefined_node_exists"));
      add(new Ariadne_Token("bad_descent_termination"));
      add(new Ariadne_Token("max_depth_reached"));
    }};

    // graph descend method return values
    private static Ariadne_TokenSet graph_descend_set = new Ariadne_TokenSet(){{
      add(new Ariadne_Token("empty_path_stack"));
      add(new Ariadne_Token("cycle_found"));
      add(new Ariadne_Token("undefined_node"));
      add(new Ariadne_Token("leaf"));
      add(new Ariadne_Token("max_depth_reached"));
    }};


  // instance data

  // constructors
  //
    public Ariadne_GraphDirectedAcyclic(){
      super(new HashMap<>(), null);
    }

    public Ariadne_GraphDirectedAcyclic(Map<Ariadne_Label, Ariadne_Node> node_map, Ariadne_DefinitionList recognizer_f_list, Ariadne_LabelList root_node_list, int max_depth, boolean verbose){
      super(node_map, recognizer_f_list);
      Ariadne_TokenSet cycle_detection_result = graph_mark_cycles(root_node_list, max_depth, verbose);
    }

    public Ariadne_GraphDirectedAcyclic(Map<Ariadne_Label, Ariadne_Node> node_map, Ariadne_DefinitionList recognizer_f_list, Ariadne_LabelList root_node_list){
      super(node_map, recognizer_f_list);
      Ariadne_TokenSet cycle_detection_result = graph_mark_cycles(root_node_list);
    }

  // interface functions
  //
    private List<Integer> path_find_cycle(Ariadne_LabelList path){
      if(path.size() <= 1) return null;

      int rightmost_index = path.size() - 1;
      Ariadne_Label rightmost_node_label = path.get(rightmost_index);

      int cycle_leftmost_index = path.indexOf(rightmost_node_label);
      Boolean has_cycle = cycle_leftmost_index < rightmost_index;
      if(!has_cycle) return null;

      List<Integer> result = new ArrayList<>();
      result.add(cycle_leftmost_index);
      result.add(rightmost_index);
      return result;
    }

    private boolean graph_descend_cycle_case(Ariadne_LabelList left_path, List<Ariadne_LabelList> path_stack, boolean verbose){

      List<Integer> cycle_index_interval = path_find_cycle(left_path);
      if(cycle_index_interval == null){
        return false;
      }

      int cycle_i0 = cycle_index_interval.get(0);
      int cycle_n = cycle_index_interval.get(1);

      if(verbose) Ariadne_Util.print_list(
        "Found cycle:", 
        left_path.subList(cycle_i0, cycle_n + 1)
      );

      Ariadne_LabelList undefined_node_list = new Ariadne_LabelList();
      for (int i = cycle_i0; i <= cycle_n; i++){
        Ariadne_Label node_label = left_path.get(i);
        Ariadne_Node node = super.lookup(node_label);
        if(node != null){
          node.mark(new Ariadne_Token("cycle_member"));
        } else{
          undefined_node_list.add(node_label);
        }
      }

      if(verbose) Ariadne_Util.print_list(
        "Each undefined node could not be marked as a cycle member:", 
        undefined_node_list
      );

      path_stack.subList(cycle_i0 + 1, cycle_n + 1).clear();

      return true;
    }

    private Ariadne_TokenSet graph_descend(List<Ariadne_LabelList> path_stack, int max_depth, boolean verbose){
      Ariadne_TokenSet ret_value = new Ariadne_TokenSet();

      if(path_stack.isEmpty()){
        ret_value.add(new Ariadne_Token("empty_path_stack"));
        return ret_value;
      }

      Ariadne_LabelList left_path = new Ariadne_LabelList();
      for (Ariadne_LabelList neighbor_list : path_stack){
        left_path.add(neighbor_list.get(0));
      }

      do{

        if(graph_descend_cycle_case(left_path, path_stack, verbose)){
          ret_value.add(new Ariadne_Token("cycle_found"));
          return ret_value;
        }

        Ariadne_Label it_node_label = path_stack.get(path_stack.size() - 1).get(0);
        Ariadne_Node it_node = super.lookup(it_node_label);
        if(it_node == null){
          ret_value.add(new Ariadne_Token("undefined_node"));
          return ret_value;
        }

        Ariadne_LabelList neighbor_list = it_node.neighbor_LabelList();
        if(neighbor_list.isEmpty()){
          ret_value.add(new Ariadne_Token("leaf"));
          return ret_value;
        }

        path_stack.add(new Ariadne_LabelList(neighbor_list));
        Ariadne_Label it_next_label = neighbor_list.get(0);
        left_path.add(it_next_label);

        if(max_depth > 0){
          max_depth--;
          if(max_depth == 0){
            if(verbose){
              Ariadne_Util.print_list("GraphDirectedAcyclic.GraphDescend:: max_depth reached, ending descent:", path_stack);
            }
            ret_value.add(new Ariadne_Token("max_depth_reached"));
            return ret_value;
          }
        }

      } while (true);
    }


  public Ariadne_TokenSet graph_mark_cycles(Ariadne_LabelList root_node_LabelList, int max_depth, boolean verbose){
    Ariadne_TokenSet ret_value = new Ariadne_TokenSet();
    boolean exists_malformed = false;
    Ariadne_TokenSet result;

    if(root_node_LabelList.isEmpty()){
      ret_value.add(new Ariadne_Token("empty_root_label_list"));
      return ret_value;
    }

    List<Ariadne_LabelList> path_stack = new ArrayList<>();
    path_stack.add(new Ariadne_LabelList(root_node_LabelList));

    do{
      result = graph_descend(path_stack, max_depth, verbose);
      if(result.contains(new Ariadne_Token("cycle_found"))) ret_value.add(new Ariadne_Token("cycle_exists"));
      if(result.contains(new Ariadne_Token("undefined_node"))) ret_value.add(new Ariadne_Token("undefined_node_exists"));
      if(result.contains(new Ariadne_Token("max_depth_reached"))) ret_value.add(new Ariadne_Token("max_depth_reached"));
      if(!result.contains(new Ariadne_Token("leaf")) && !result.contains(new Ariadne_Token("cycle_found"))) ret_value.add(new Ariadne_Token("bad_descent_termination"));

      Ariadne_LabelList top_list = path_stack.get(path_stack.size() - 1);
      top_list.remove(0);
      if(top_list.isEmpty()) path_stack.remove(path_stack.size() - 1);

    } while (!path_stack.isEmpty());

    if(verbose){
      if(ret_value.contains("bad_descent_termination")){
        System.out.println("GraphDirectedAcyclic.graph_mark_cycles:: terminated with unexpected condition.");
      }
      if(ret_value.contains("cycle_exists")){
        System.out.println("GraphDirectedAcyclic.graph_mark_cycles:: One or more cycles detected.");
      }
      if(ret_value.contains("undefined_node_exists")){
        System.out.println("GraphDirectedAcyclic.graph_mark_cycles:: Undefined nodes exist.");
      }
    }

    return ret_value;
  }

  public Ariadne_TokenSet graph_mark_cycles(Ariadne_LabelList root_node_LabelList){
    return graph_mark_cycles(root_node_LabelList, this.debug ? 40 : -1, this.debug);
  }

  @Override
  public Ariadne_Node lookup(Ariadne_Label node_label, boolean verbose){
    Ariadne_Node node = super.lookup(node_label, verbose);
    if(node != null && node.has_mark(new Ariadne_Token("cycle_member"))){
      if(verbose){
        System.out.println("GraphDirectedAcyclic.lookup:: Node is part of a cycle, not returned: " + node_label);
      }
      return null;
    }
    return node;
  }

  public Ariadne_Node lookup(Ariadne_Label node_label){
    return lookup(node_label, this.debug);
  }

}
