package com.ReasoningTechnology.Ariadne;

/*
Our build tool graph is directed from targets to dependencies.

It must be cycle free. This class extends `Graph` by marking
cycles that are found when descending from root nodes. Then
`lookup` will not return these nodes.

If:

 1. The production functions produce the same output given the
 same input. (Which is a given for the map definition portion of
 the graph).

 2. There are a finite number of states.

 3. Any input values that are used in the definition of the graph are not
 changed after some point in time when the graph is said to have been 'defined'.

 4. No computed values are used for changing the graph definition.

 5. No computed values direct graph traversal.

Then:

  We can write an algorithm that in turn starts at each node
  in the graph and searches for cycles, and will find all
  cycles.

  Our GraphDirectedAcyclic constructor would then not need
  to know the root nodes of the traversal.

However:

  Had the graph definition been limited to the map object, and there is no
  interaction with the build functions, we would meet the criteria.

  However, our bestowed upon the user the ability to define 'production'
  functions, which are used to build the graph dynamically at run time.

  With such production functions is possible that a a graph definition would
  emerge after a finite number of calls to the build function, and thus we still
  meet the above criteria, and do not need to give a root node list to the
  GraphDirectedAcyclic.

  When a graph is to be used with the build tool, it is required that the graph
  meet the criteria above ** when starting from the root nodes **.  However, it
  is not generally required. Hence, we provide the root nodes to this function.

  It is possible that the user defines one or more production functions,
  intentionally nor not, that results in building an unbounded graph definition.
  Further it is possible for a user to define a production that has an unbounded
  nature that results in the cycle marking method of this class from never
  finishing.

  As examples, suppose that the a production follows the digits of pi, giving
  a different node definition each time it is fed a label to recognize. Or
  suppose that a production returns an ever longer node label each time it
  is called. Such a production would cause the graph definition to be open,
  and potentially for this cycle detection algorithm to never complete.

  A production for a conventional build environment will not do these things,
  However, a buggy production for build environment, or an unconventional
  build environment not imagined at this time, could.

  A breadth first search combined programmed as a generator would be able
  to handle such cases without hanging. Where as this algorithm plunges
  down depth first, and returns when it has the answer.

*/

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class GraphDirectedAcyclic extends Graph{

  /*--------------------------------------------------------------------------------
    constructors
  */

  public GraphDirectedAcyclic(Map<Label ,Node> node_map ,ProductionList recognizer_f_list ,List<Label> root_node_list ,boolean verbose){
    super( node_map ,recognizer_f_list );

    // Step 2: Run the cycle detection logic and mark up the graph.
    TokenSet cycle_detection_result = mark_cycle_graph( root_node_list ,verbose );

    // You can further process cycle detection results here if needed

  }


  public GraphDirectedAcyclic(Map<Label ,Node> node_map ,ProductionList recognizer_f_list ,List<Label> root_node_list){
    super( node_map ,recognizer_f_list );

    // Step 2: Run the cycle detection logic and mark up the graph.
    TokenSet cycle_detection_result = mark_cycle_graph( root_node_list ,this.debug );

    // You can further process cycle detection results here if needed
  }


  /*--------------------------------------------------------------------------------
   instance data extension
  */

  private static boolean debug = true;


  /*--------------------------------------------------------------------------------
    Interface

    1. nodes are referenced by label.

    2. A list is a kind of sequence. It consists of a leftmost item, subsequent
    items, and a rightmost item.

    3. A node list consists of a leftmost node, subsequent nodes, and a rightmost node.

    4. `path_stack`
        
        The `path_stack` is a list. Each item in the stack is a node list.

        The rightmost items is the top of the stack.

        The leftmost item is a list of root nodes, where traversal of the graph
        starts.

        Given two adjacent items on the path stack, say e0 and e1:

            Say k0 is the leftmost node on the node list e0.
            
            The e1 will be the neighbor (child) list from node k0.

            Hence, the path stack consists of the child lists of nodes along
            a traversal. We chose a leftmost traversal.

                e0:   k_0       k_1    ... k_2  

                e1:   k_0_0    k_0_1   ... k_0_2   ; children of k_0

                e2:   K_0_0_0  k_0_0_1 ... k_0_0_2 ; children of k_0_0
                

    5. `path`

        A list of the leftmost nodes from a path stack.  

        Given that e0 is a root node, `path` will consist of 

            k_0, k_0_0, k_0_0_0 ... k_0_0..._0

        Within the context of a path, k_0 is the leftmost item, and k_n is the
        rightmost item.


    6. removing a cycle

       This is a build tool. Each node corresponds to a build objective, and the
       neighbor nodes are dependencies. A neighbor node is a child node in our
       tree descent, and is a dependency to our build tool.

       This routine is called as part of the analysis phase. Nothing is
       built here, rather we are merely marking dependency cycles that we
       find when descending from the root nodes.

       When we find a cycle, we remove those nodes from the current traversal
       path, because we do not went the analysis to do in circles.  It is
       possible that there are spurs that emanate from the cycle, and following
       these might lead to finding more cycles.

       Our build tool (which is not in this file) will stop descending through
       the graph upon finding a cycle, and the effects of this will cause
       upstream nodes to also not be built. Hence, the cycles hidden behind
       other cycles are irrelevant.  

       However, if we want to make routine of more general use, then the
       discovered cycles should be pushed on to a cycle stack, and then each
       item on the cycle stack would be used as root nodes for a new cycle
       search. Note the leftmost cycle on each recursive search on the leftmost
       node, will be the original cycle.


  */

  /*
    Cycle detection is based on label compares. Lookup is done to mark the node.

    1. Given a path.

    2. Checks if the rightmost item, k_n, in the path occurs earlier in the path, say at k_i.
       If so, marks the nodes involved in the cycle.

    3. if there is no cycle, returns null, otherwise returns the Interval [i ,n]

  */
  private List<Integer>  path_find_cycle(List<LabelList> path_stack ,LabelList path ){
    if( path.size() <= 1 ) return null; 

    int rightmost_index = path.size() - 1;
    Label rightmost_node_label = path.get( rightmost_index );

    // if there is no cycle, searching for rightmost_node_label will find itself:
    int cycle_leftmost_index = path.indexOf(rightmost_node_label);
    Boolean has_cycle = cycle_leftmost_index < rightmost_index;
    if( ! has_cycle ) return null;

    List<Integer> result = new ArrayList<>();
    result.add(cycle_leftmost_index);
    result.add(rightmost_index);
    return result;
  }

  /*
    When descending down the tree, if we find a cycle, we reset the iterator
    to the top of the cycle. The caller will then return to its caller as though
    having found a leaf node.
  */
  private boolean graph_descend_cycle_case( List<Label> left_path ,List<LabelList> path_stack ,boolean verbose ){

    List<Integer> cycle_index_interval = path_find_cycle( left_path ,verbose );
    if( cycle_index_interval == null ){
      return false; // No cycle found
    }

    int cycle_i0 = cycle_index_interval.get(0); // Cycle leftmost, inclusive
    int cycle_n = cycle_index_interval.get(1);  // Cycle rightmost, inclusive

    if(verbose) Util.print_list(
      "Found cycle:" 
      ,left_path.subList( cycle_i0 ,n +1)
    );

    // Mark cycle members
    LabelList undefined_node_list = new LabelList();
    for( int i = cycle_i0; i <= cycle_n; i++ ){
      Label node_label = left_path.get(i);
      Node node = super.lookup( node_label );
      if( node != null ){
        if( node.get("mark") == null ){
          node.put( "mark" ,new HashTokenSet() );
        }
        ( (TokenSet)node.get("mark") ).add( "cycle_member" );
      }else{
        undefined_node_list.add( node_label );
      }
    }

    if(verbose) Util.print_list(
      "Each undefined node could not be marked as a cycle member:" 
      ,undefined_node_list
    );

    // Reset the graph iterator to the top of the cycle
    path_stack.subList( cycle_i0 + 1 ,cycle_n + 1 ).clear();

    return true; // cycle found
  }
  
  /*
    Given a path_stack initialized to the root nodes for the graph traversal.

    Perform depth first descent from each node in succession. Algorithm
    works from left to right in the child lists.

    Searches for cycles in the descent paths.

  */
  private static TokenSet graph_descend_set = new TokenSet();
  static {
    graph_descend_set.add( new Token("empty_path_stack") );
    graph_descend_set.add( new Token("cycle_found") );
    graph_descend_set.add( new Token("undefined_node") );
    graph_descend_set.add( new Token("leaf") );
    graph_descend_set.add( new Token("max_depth_exceeded") );
  }
  private TokenSet graph_descend( List<LabelList> path_stack ,int max_depth ,boolean verbose ){
    TokenSet ret_value = new HashTokenSet();

    if( path_stack.isEmpty() ){
      ret_value.add( "empty_path_stack" );
      return ret_value;
    }

    // a leftmost path through the neighbor lists of the leftmost nodes
    LabelList left_path = new ArrayList<>();
    for( LabelList neighbor_list : path_stack ){
      left_path.add( neighbor_list.get(0) );
    }

    // do descend
    do{

      // cycle case
      if( graph_descend_cycle_case( left_path ,path_stack ,verbose ) ){
        ret_value.add( "cycle_found" );
        return ret_value;
      }

      // Non-cycle case, descend further into the tree.
      // Increment the graph iterator (path_stack) to go down a level.
      Label it_pt_label = path_stack.get( path_stack.size() - 1 ).get(0);
      Node it_pt_node = super.lookup( it_pt_label );
      if( it_pt_node == null ){
        ret_value.add( "undefined_node" );
        return ret_value;
      }
        
      NodeList neighbor_list = it_pt_node.get("neighbor");
      if( neighbor_list == null || neighbor_list.isEmpty() ){
        ret_value.add( "leaf" );
        return ret_value;
      }

      path_stack.add( new ArrayList<>((LabelList) neighbor_list) );
      Label it_next_label = neighbor_list.get(0);
      left_path.add( it_next_label );  // Properly add the label to the left_path

      // bound the size of problem we are willing to work on
      // set max_depth <= 0 to have this test ignored
      if( max_depth > 0 ){
        max_depth--;  // Typo fixed
        if( max_depth == 0 ){
          ret_value.add( "max_depth_exceeded" );
          return ret_value;
        }
      }

    }while( true ); // while descend
  }

   
  /*
    Given root_node_label_list, marks up the graph and returns a set possibly
    containing 'all_wellformed' and 'cycles_exist'.

    Marks potentially added to each node include 'cycle_member' and 'wellformed'.
    Note that these marks are independent.
  */
  // Saigens updates:
  /*
  Given root_node_label_list, marks up the graph and returns a set possibly
  containing 'all_wellformed' and 'cycles_exist'.

  Marks potentially added to each node include 'cycle_member' and 'wellformed'.
  Note that these marks are independent.
*/
  /*

public TokenSet mark_cycle_graph( LabelList root_node_label_list ,boolean verbose ){
    TokenSet ret_value = new HashTokenSet();
    boolean exists_malformed = false;
    TokenSet result; // used variously

    if( root_node_label_list.isEmpty() ) return ret_value;

    // Initialize the DFS tree iterator.
    List<LabelList> path_stack = new ArrayList<>();
    path_stack.add( new ArrayList<>(root_node_label_list) );

    // Iterate over left side tree descent, avoids complexity in cycle detection logic.
    do{
        result = graph_descend( path_stack ,verbose );
        if( result.contains("cycle_found") ) ret_value.add("cycle_exists");
        if( result.contains("undefined_node") ) exists_malformed = true;  // Corrected flag

        // Increment the iterator to the next leftmost path
        LabelList top_list = path_stack.get( path_stack.size() - 1 );
        top_list.remove(0);
        if( top_list.isEmpty() ) path_stack.remove( path_stack.size() - 1 );

    }while( !path_stack.isEmpty() );

    if( !exists_malformed ) ret_value.add("all_wellformed");

    if( verbose ){
        if( exists_malformed ) System.out.println("One or more malformed nodes were found.");
        boolean exists_cycle = ret_value.contains("cycle_exists");
        if( exists_cycle ) System.out.println("One or more cyclic dependency loops found.");
        if( exists_malformed || exists_cycle ) System.out.println("Will attempt to build unaffected nodes.");
    }

    return ret_value;
}

public TokenSet mark_cycle_graph( LabelList root_node_label_list ){
    return mark_cycle_graph( root_node_label_list ,true );
}
//------------------

  public TokenSet mark_cycle_graph(LabelList root_node_label_list ,boolean verbose){
    TokenSet ret_value = new HashSet<>();
    boolean exists_malformed = false;
    TokenSet result; // used variously

    if( root_node_label_list.isEmpty() ) return ret_value;

    // Initialize the DFS tree iterator.
    List<LabelList> path_stack = new ArrayList<>();
    path_stack.add( new ArrayList<>(root_node_label_list) );

    // iterate over left side tree descent, not ideal as it starts at the
    // root each time, but avoids complexity in the cycle detection logic.
    do{
      result = graph_descend(path_stack ,verbose);
      if( result.contains("cycle_found") ) ret_value.add("cycle_exists");
      if( result.contains("undefined_node") ) exists_undefined = true;

      // increment the iterator to the next leftmost path
      LabelList top_list = path_stack.get( path_stack.size() - 1 );
      top_list.remove(0);
      if( top_list.isEmpty() ) path_stack.remove( path_stack.size() - 1 );

    }while( !path_stack.isEmpty() );

    if( !exists_malformed ) ret_value.add("all_wellformed");

    if( verbose ){
      if( exists_malformed ) System.out.println("one or more malformed nodes were found");
      boolean exists_cycle = ret_value.contains("cycle_exists");
      if( exists_cycle ) System.out.println("one or more cyclic dependency loop found");
      if( exists_malformed || exists_cycle ) System.out.println("will attempt to build unaffected nodes");
    }

    return ret_value;
  }
  public TokenSet mark_cycle_graph(LabelList root_node_label_list){
    return mark_cycle_graph(root_node_label_list ,true);
  }
  */
  /*--------------------------------------------------------------------------------
    Graph traversal
  */

  @Override
  public Node lookup(Label node_label, boolean verbose) {
    Node node = super.lookup( node_label ,verbose );

    // Ensure the node exists and the mark property exists
    if( node != null ){
      TokenSet mark = (TokenSet)node.get("mark");

      // Check if the mark property exists and contains "cycle_member"
      if( mark != null && mark.contains("cycle_member") ){
        if( verbose ){
          System.out.println("Node is part of a cycle and will not be returned: " + node_label);
        }
        return null;  // Exclude nodes in cycles
      }
    }

    return node;
  }

  // Overloaded lookup method with default verbosity (true)
  public Node lookup(Label node_label){
    return lookup(node_label ,this.debug);
  }



}
