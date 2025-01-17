/*
TM_SR_ND_Depth is a list of neighbor TM_SR_NDs going down the leftmost side of
a graph traversal. A leftmost traversal is one characterized by
following leftmost not yet visited node on the most recently visited
node's neighbor list.

Depth traversal from a start node, ends when reaching a node that has
no neighbors, or when reaching encountering a cycle.

The `Node::neighbor()` function returns an TM_SR_ND for iterating over the
node's neighbors. An TM_SR_ND is returned rather than a list, because in
general a neighbor list is allowed to be unbounded.  Though with a
finite graph, that can not happen. (See IndexTree for an example of an
infinite depth and infinite breadth graph traveral.)

It is possible to construct and infinite graph such that the
`TM_SR_ND_Depth::make()` function would never return. 

For a finite graph, this depth traversal will provably terminate, due
to running out unique (non cycle) nodes to visit.  More generally, if
graph traversal from a start node is guaranteed to reach a leaf node
(has no neighbors), or a a cycle, within a finite number of node
traversal steps, then `TM_SR_ND_Depth::make()` will always return.

Each call to step causes the TM read head to move to the next lowest
depth, leftmost unvisited node. This might require backtracking and
descending again.

Nodes are referenced by label.

A null valued label is flagged as an error, though we still look it up
in the graph. It is a fatal error, `exit(1)` if `lookup` returns a
null node.

A `path` is a list of nodes (each referenced by a label).  `context_path`
carries with it the child list for each node in the path.  The child
list is represented as a srtm, and the head of the tape machine marks
the child node that is no the path.

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

  // static
  //
  TM_SR_ND_Depth make(Ariadne_Graph graph){
    TM_SR_ND_Depth depth = new TM_SR_ND_Depth();
    if(graph == null) return null;
    depth.graph = graph;
    depth.context_path.add( graph.start() );
    boolean flag = complete_context_path();
    if( flag ) return depth;
    return null;
  }

  // instance data
  //
  protected Ariadne_Graph graph = null;
  protected List<Ariadne_TM_SR_ND<Label>> context_path = new ArrayList<>();
  protected Ariadne_Label cycle_node_label = null;

  // constructor
  //
  protected TM_SR_ND_Depth(){
    set_topology(topo_null);
  }

  // instance interface implementation
  //

  // A path is terminated by a leaf node or upon discovering a cycle.
  // Return false iff can not complete the context path. This is generally a fatal error.
  protected boolean complete_context_path(){

    if( context_path.isEmpty() ){
      System.out.println("TM_SR_ND_Depth::complete_context_path empty context_path");
      return false;
    }

    HashSet<Ariadne_Label> path_node_label_set = new HashSet<>();
    boolean is_cycle_node = false;

    // should add cycle check for anomalous case caller fed us an initial path with a cycle
    // initialize the path_node set
    Ariadne_TM_SR_ND_Array<Ariadne_TM_SR_ND> context_path_srtm = Ariadne_TM_SR_ND_Array.make(context_path);
    Ariadne_TM_SR_ND child_srtm = null;
    Ariadne_Label path_node_label = null;

    // context_path is known not to be empty, so can_read() is true
    do{
      child_srtm = context_path_srtm.read();
      path_node_label = child_srtm.read();
      if(path_node_label == null){
        System.out.println("TM_SR_ND_Depth::complete_context_path null path label");
        return false;
      }
      is_cycle_node = path_node_label_set.contains(path_node_label);
      if( is_cycle_node ){
        System.out.println
        (
         "TM_SR_ND_Depth::complete_context_path: cycle found in initial context_path"
         );
        cycle_node_label = path_node_label;
        return false; 
      }
      path_node_label_set.add( path_node_label );
      // when completing from the `start()`, this will be break out on the first try
      if( !context_path_srtm.can_step() ) break;
      context_path_srtm.step();
    }while(true);

    // path descends down the left side of the unvisted portion of the tree.
    // extend the context_path downward until leftmost is a leaf node or a cycle node
    Ariadne_Node path_node = null;
    boolean is_leaf_node = false;
    do{
      path_node = graph.lookup_node(path_node_label);
      if(path_node == null){
        System.out.println
          (
           "TM_SR_ND_Depth::complete_context_path node not found in graph for: label(\""
           + path_node_label
           + "\")"
           );
        return false;
      }

      // descend
      child_srtm = path_node.neighbor();

      // exit conditions
      //
      is_leaf_node = child_srtm == null || !child_srtm.can_read();
      if(is_leaf_node) return true;
      //
      path_node_label = child_srtm.read();
      is_cycle_node = path_node_label_set.contains(path_node_label);
      if( is_cycle_node ){
        // caller needs to check for cycle found before each new step
        cycle_node_label = path_node_label;
        return true; 
      }

      // path_node_label has now been visited
      path_node_label_set.add(path_node_label);

    }while(true);
  }

  // being a good object citizen
  //


}
