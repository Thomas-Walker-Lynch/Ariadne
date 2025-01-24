/*

Each member of a `path` list is a node label. A path is created by traversing the graph.

Imagine expanding each member of a path into a sibling list tm, with the cell under
the head being the label on the path.  This is the context_path.

Because a context path holds the siblings, it can be used as the state variable during
a tree traversal.

*/

import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_ND;
import com.ReasoningTechnology.Ariadne.Ariadne_Graph;
import com.ReasoningTechnology.Ariadne.Ariadne_Node;
import com.ReasoningTechnology.Ariadne.Ariadne_Label;
import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_ND_Array;
import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_ND_List;

class TM_SR_ND_Depth extends Ariadne_TM_SR_ND<Label>{

  // ----------------------------------------
  // static
  //

  TM_SR_ND_Depth make(Ariadne_Graph graph){
    if(graph == null) return null;
    TM_SR_ND_Depth depth = new TM_SR_ND_Depth();
    depth.graph = graph;
    depth.context_path.add( graph.start() );
    depth.context_path_tm = Ariadne_TM_SR_ND_Array.make(context_path);
    initialize i = initialize.f(depth.context_path_tm ,depth.path_member_set);
    if( !i.success() ) return null;
    first_in_path_cycle_node_label = i.first_in_path_cycle_node_label;
    return depth;
  }

  // ----------------------------------------
  // instance data
  //

  protected Graph graph = null;
  protected List<Ariadne_TM_SR_ND<Label>> context_path = new ArrayList<>();
  protected Ariadne_TM_SR_ND_Array<Ariadne_TM_SR_ND> context_path_tm = null;
  protected Label first_in_path_cycle_node_label = null;

  // Used for cycle detection.
  HashSet<Ariadne_Label> path_member_set = new HashSet<>();

  // ----------------------------------------
  // constructor
  //

  protected TM_SR_ND_Depth(){
    set_topology(topo_null);
  }


  // ----------------------------------------
  // instance interface implementation
  //

  // Given a context_path_tm and path_member_set.
  // Initializes the path_member_set. Leaves head on last cell of context_path_tm.
  // Returns a successful initlaization flag and possibly not null cycle_node_label
  public class initialize{
    static public initialize f(context_path_tm ,path_member_set){
      initialize instance = new initialize();
      instance.g(context_path_tm ,path_member_set);
      return instance;
    }
    public Label first_in_path_cycle_node_label = null;
    public boolean success = false;

    protected void g(context_path_tm ,path_member_set){

      if( context_path.isEmpty() ){
        System.out.println("TM_SR_ND_Depth::initialize required context_path is empty.");
        success = false;
        return;
      }

      boolean is_cycle_node = false;
      Ariadne_TM_SR_ND sibling_tm = null;
      Ariadne_Label path_node_label = null;

      do{
        sibling_tm = context_path_tm.read();
        path_node_label = sibling_tm.read();
        if(path_node_label == null){
          System.out.println("TM_SR_ND_Depth::complete_context_path hit null path label on path");
          success = false;
          return;
        }

        is_cycle_node = path_member_set.contains(path_node_label);
        if( is_cycle_node ) break;

        path_member_set.add( path_node_label );

        if( !context_path_tm.can_step() ) break;
        context_path_tm.step();

      }while(true);

      if( is_cycle_node && context_path_tm.can_step() ){
        System.out.println
          (
           "TM_SR_ND_Depth::initialize_path_member_set: cycle found in initial context_path."
           );
        first_in_path_cycle_node_label = path_node_label;
        success = false;
        return;
      }

      success = true;
      return;
    }
  }

  public boolean path_has_cycle(){
    return first_in_path_cycle_node_label != null;
  }

  
  // Upon stepping on a cycle node, we must go back and pick the next start node.
  // The context path that stepped on the cycle node is saved to document the cycle.
  // A path is terminated by a cycle being found, or a leaf node, or all the children having been visited. The terminating node (label) becomes the read value. Upon entering `step()`
  // the head is on the prior leaf node.
  public void step(){

    boolean is_cycle_node = false;
    Ariadne_Node path_node = null;
    boolean is_leaf_node = false;
    Ariadne_TM_SR_ND sibling_tm = null;
    Ariadne_Label path_node_label = null;

    sibling_tm = context_path_tm.read();
    path_node_label = sibling_tm.read();

    do{
      path_node = graph.node(path_node_label);
      if(path_node == null){
        System.out.println
          (
           "TM_SR_ND_Depth::complete_context_path internal algorithm error, node on path not found in graph for: label(\""
           + path_node_label
           + "\")"
           );
        return false;
      }

      // attempt to extend downward
      sibling_tm = path_node.neighbor();
      is_leaf_node = sibling_tm == null || !sibling_tm.can_read();
      if(is_leaf_node){ 

      // add the sibling_tm to the context_path
      context_path.add(sibling_tm);

      // exit if path_node cycles back
      path_node_label = sibling_tm.read();
      is_cycle_node = path_member_set.contains(path_node_label);
      if( is_cycle_node ){
        // caller needs to check for cycle found before each new step
        first_in_path_cycle_node_label = path_node_label;
        return true; 
      }

      // path_node_label has now been visited
      path_member_set.add(path_node_label);

    }while(true);
  }

  // being a good object citizen
  //


}
