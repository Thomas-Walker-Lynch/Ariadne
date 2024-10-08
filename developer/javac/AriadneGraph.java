import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;


public class AriadneGraph {

  /*--------------------------------------------------------------------------------
   type aliases
  */

  public interface Token extends String{};
  public interface TokenSet extends Set<Token>{};
  public interface Label extends String{};
  public interface LabelList extends List<Label> {};
  public interface Node extends Map<Label, Object>{};
  public interface NodeList extends List<Node>{};

  /*--------------------------------------------------------------------------------
   instance data 
  */

  private static Boolean debug = true;
  private Map<Label, Node> node_map;
  private List<Function<Label, Node>>> node_f_list;


  /*--------------------------------------------------------------------------------
   constructors
  */

  /*--------------------------------------------------------------------------------
    constructors
  */

  public AriadneGraph(Map<Label, Node> node_map, List<Function<Label, Node>> node_f_list) {
    if (node_map == null && node_f_list == null) {
      System.err.println("AriadneGraph: requires one or both of 'node_map' as Map, and 'node_f_list' as List.");
      System.exit(1);
    }

    // Initialize node_map and node_f_list to empty collections if they are null
    this.node_map = (node_map != null) ? node_map : new HashMap<Label, Node>();
    this.node_f_list = (node_f_list != null) ? node_f_list : new ArrayList<Function<Label, Node>>();
  }

  /*--------------------------------------------------------------------------------
   file utilities
  */

  public static Map<String ,String> unpack_file_path(String file_fp){
    if(debug) System.out.println("unpack_file_path::file_fp: " + file_fp);

    File file = new File(file_fp);
    String parent_dp = (file.getParent() != null) ? file.getParent() : "";

    if( !parent_dp.isEmpty() && !parent_dp.endsWith(File.separator) ){
      parent_dp += File.separator;
    }

    String file_fn = file.getName();
    String file_fn_base = file_fn;
    String file_fn_ext = "";

    int last_index = file_fn.lastIndexOf('.');
    if(last_index > 0){
      file_fn_base = file_fn.substring(0 ,last_index);
      if( last_index + 1 < file_fn.length() ){
        file_fn_ext = file_fn.substring(last_index + 1);
      }
    }

    Map<String ,String> ret_val = new HashMap<>();
    ret_val.put("dp" ,parent_dp);
    ret_val.put("fn" ,file_fn);
    ret_val.put("fn_base" ,file_fn_base);
    ret_val.put("fn_ext" ,file_fn_ext);

    if(debug) System.out.println("unpack_file_path::ret_val: " + ret_val);

    return ret_val;
  }

  public static boolean file_exists_q(Label node_label){
    Path node_path = Paths.get(node_label);
    return Files.exists(node_path);
  }

  /*--------------------------------------------------------------------------------
    About nodes

    A leaf type node specifies a path to a file that should not be deleted by
    in clean operations. Typically this is the source code. We could add a
    tool to lock permissions on these before a build, so that the build
    scripts will also not mess with them (unless they change permissions).

    If the user has multiple dependency graphs defined, a node with no
    dependencies in one graph, might have dependencies in another.

    An error type node, is one that was found to not have a type, or
    was constructed by the tool to be a place older, perhaps for a
    node label that was not found.

  */
  public static TokenSet all_node_type_set = new HashSet<>(Arrays.asList(
    "symbol"  
    ,"path"   
    ,"leaf"   
    ,"error"  
  ));

  public static TokenSet persistent_node_mark_set = new HashSet<>(Arrays.asList(
    "cycle_member"
    ,"wellformed"
    ,"build_failed"
    ,"null_node"
  ));

  public static boolean leaf_q(Node node){
    return node != null && "leaf".equals(node.get("type"));
  }

  public static boolean has_mark(Node node){
    return node != null && node.get("mark") != null && !( (TokenSet)node.get("mark") ).isEmpty();
  }

  public static void set_mark(Node node ,Token mark){
    if( node.get("mark") == null ){
      node.put("mark" ,new HashTokenSet());
    }
    ( (TokenSet)node.get("mark") ).add(mark);
  }

  public static void clear_mark(Node node ,Token mark){
    if( node != null && node.get("mark") != null ){
      ( (TokenSet) node.get("mark") ).remove(mark);
    }
  }

  public static boolean marked_good_q(Node node){
    return node != null && node.get("mark") != null
      && ( (TokenSet)node.get("mark") ).contains("wellformed")
      && !( (TokenSet)node.get("mark") ).contains("cycle_member")
      && !( (TokenSet)node.get("mark") ).contains("build_failed");
  }


  /*--------------------------------------------------------------------------------
   Well-formed Node Check
  */

  public static TokenSet form_condition_set = new HashSet<>(Arrays.asList(
    "no_node"
    ,"node_must_have_label"
    ,"label_must_be_string"
    ,"node_must_have_type"
    ,"bad_node_type"
    ,"neighbor_value_must_be_list"
    ,"neighbor_reference_must_be_string"
    ,"neighbor_label_not_in_graph"
    ,"mark_property_value_must_be_set"
    ,"unregistered_mark"
    ,"missing_required_build_code"
    ,"leaf_given_neighbor_property"
    ,"leaf_given_build_property"
  ));

  // given a node, collects a description of its form, returns a set form condition tokens
  public static TokenSet wellformed_node_q(Node node){
    TokenSet form_error_set = new HashSet<>();

    if(node == null){
      form_error_set.add("null_node");
      return form_error_set;
    }

    if( !node.containsKey("label") )
      form_error_set.add("node_must_have_label");
    else if( !(node.get("label") instanceof Label) )
      form_error_set.add("label_must_be_string");

    if( !node.containsKey("type") )
      form_error_set.add("node_must_have_type");
    else if( !(node.get("type") instanceof String) || !all_node_type_set.contains(node.get("type")) )
      form_error_set.add("bad_node_type");

    if( node.containsKey("neighbor") ){
      if( !(node.get("neighbor") instanceof List) )
        form_error_set.add("neighbor_value_must_be_list");
      else if( !((List<?>) node.get("neighbor")).stream().allMatch(it -> it instanceof Label) )
        form_error_set.add("neighbor_reference_must_be_string");
    }

    if( node.containsKey("mark") ){
      if( !(node.get("mark") instanceof Set) )
        form_error_set.add("mark_property_value_must_be_set");
      else if( !((Set<?>) node.get("mark")).stream().allMatch(it -> persistent_node_mark_set.contains(it)) )
        form_error_set.add("unregistered_mark");
    }

    if( "path".equals(node.get("type")) && (!node.containsKey("build") || !(node.get("build") instanceof Runnable)) )
      form_error_set.add("missing_required_build_code");

    if( "leaf".equals(node.get("type")) ){
      if( node.containsKey("neighbor") ) form_error_set.add("leaf_given_neighbor_property");
      if( node.containsKey("build") ) form_error_set.add("leaf_given_build_property");
    }

    return form_error_set;
  }

  // given a node, potentially marks it as wellformed, returns one of 'wellformed' or 'malformed'
  public static Token wellformed_mark_node(Node node ,boolean verbose){
    if(debug){
      if(node != null){
        System.out.println("wellformed_mark_node::node: " + node);
      }else{
        System.out.println("wellformed_mark_node given a null node");
      }
    }

    TokenSet form_errors = wellformed_node_q(node);
    if( form_errors.isEmpty() ){
      set_mark( node ,"wellformed" );
      return "wellformed";
    }

    // At this point we know that form_errors is not empty
    if(verbose){
      if( node != null && node.get("label") != null && ((Label)node.get("label")).length() > 0 ){
        System.out.print( "node " + node.get("label") + " is malformed due to:" );
      }else{
        System.out.print("anonymous node is malformed due to:");
      }
      for(Token error : form_errors){
        System.out.print(" " + error);
      }
      System.out.println("");
    }

    return "malformed";
  }
  public Token wellformed_mark_node(Node node){
    return wellformed_mark_node(node ,true);
  }

  // given a node_label, potentially marks the corresponding node as 'wellformed', returns a token set.
  // Tokens included "undefined_node", "malformed", and "defactor_leaf".
  public TokenSet wellformed_mark_node_label(Label node_label ,boolean verbose){
    TokenSet ret_value = new HashSet<>();
    Node node = lookup(node_label);
    if(node == null){
      ret_value.add("undefined_node");
      return ret_value;
    }
    if( "malformed".equals(wellformed_mark_node(node ,verbose)) ){
      ret_value.add("malformed");
    }
    if( ((List<?>)node.get("neighbor")).isEmpty() ){
      ret_value.add("defacto_leaf"); // might not be `type:leaf`
    }
    return ret_value;
  }
  public TokenSet wellformed_mark_node_label(Label node_label){
    return wellformed_mark_node_label(node_label, true)
  }


  /*--------------------------------------------------------------------------------
   A well formed graph checker.  Traverses entire graph and marks nodes
   that are not well formed or that are part of a cycle.

   This must be run on the graph for `lookup_marked_good` to work.

   Each node_label must be a string and not empty.

   Subleties here because we have not yet determined if the nodes we are
   wellformed (after all ,that is what we are determining here).

   If we want to attempt to build 'islands' of things that might be located on
   the far side of cycles ,then modify the cycle finder to return a list of
   cycles (i.e. a list of lists) ,then use each of cycle definition (a list) as
   the root nodes for further search.

   `path_stack` is a stack of LabelList. The first entry is a clone of the list of
   root nodes, referenced by label. Each subsequent list is a clone of the
   neighbor list of the leftmost node of the prior entry.

   `path` is a list of the left most nodes, referenced by label, of the entries
   on the path stack. This is the path to our current location in the tree.
  */


  private boolean find_and_remove_cycle(List<LabelList> path_stack ,LabelList path ,boolean verbose){

    if( path.size() <= 1 ) return false; // 0 or 1 length path can't have a cycle

    // we want to know if the most recent node added to the path occurs at a point earlier
    // in the path.
    int rightmost_index = path.size() - 1;
    Label recent_node_label = path.get( rightmost_index );
    int cycle_start_index = path.indexOf(recent_node_label);
    if( cycle_start_index == -1 ){
      System.err.println("find_and_remove_cycle:: indexOf does not find index of known list member");
      return false;
    }
    Boolean has_cycle =  cycle_start_index < rightmost_index;
    if(!has_cycle) return false;

    if(verbose) System.out.print("mark_form_graph_descend:: dependency cycle found:");
    for( Label cycle_node_label : path.subList(cycle_start_index ,path.size()) ){
      if(verbose) System.out.print(" " + cycle_node_label);
      Node cycle_node = lookup(cycle_node_label);
      if( cycle_node.get("mark") == null ){
        cycle_node.put( "mark" ,new HashTokenSet() );
      }
      ( (TokenSet)cycle_node.get("mark") ).add("cycle_member");
    }
    if(verbose) System.out.println("");

    // We cannot continue searching after the loop, so we pop back to treat
    // the first node in the loop as though a leaf node.
    path_stack.subList( cycle_start_index + 1 ,path_stack.size() ).clear();

    return true;
  }

  private static TokenSet mark_form_graph_descend_set = new HashSet<>(Arrays.asList(
    "empty_path_stack"
    ,"cycle_found"
    ,"undefined_node"
    ,"exists_malformed"
    ,"defacto_leaf"
  ));

  private TokenSet mark_form_graph_descend( List<LabelList> path_stack ,boolean verbose ){
    TokenSet ret_value = new HashSet<>();
    if(path_stack.isEmpty()){
      if(verbose) System.out.println( "mark_form_graph_descend:: given empty path_stack to descend from" );
      ret_value.add( "empty_path_stack" );
      return ret_value;
    }

    LabelList local_path = new ArrayList<>();
    for(LabelList path : path_stack){
      local_path.add( path.get(0) );
    }
    Label local_node_label = local_path.get( local_path.size() - 1 );
  
    do{
      
      if( find_and_remove_cycle(path_stack ,local_path ,verbose) ){
        ret_value.add("cycle_found");
        return ret_value;
      }

      TokenSet wellformed_mark_node_label_result = wellformed_mark_node_label(local_node_label ,verbose);
      ret_value.addAll( wellformed_mark_node_label_result );
      if( 
         wellformed_mark_node_label_result.contains("undefined_node") 
         || wellformed_mark_node_label_result.contains("defacto_leaf") 
      ){
        return ret_value;
      }

      // Descend further into the tree.
      path_stack.add( new ArrayList<>((LabelList) lookup(local_node_label).get("neighbor")) );
      local_node_label = (LabelList)lookup(local_node_label).get("neighbor").get(0);
      local_path.add(local_node_label);

    }while(true);
  }

   
  /*
    Given root_node_label_list, marks up the graph and returns a set possibly
    containing 'all_wellformed' and 'cycles_exist'.

    Marks potentially added to each node include 'cycle_member' and 'wellformed'.
    Note that these marks are independent.
  */
  public TokenSet mark_form_graph(LabelList root_node_label_list, boolean verbose){
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
      result = mark_form_graph_descend(path_stack, verbose);
      if( result.contains("cycle_found") ) ret_value.add("cycle_exists");
      if( result.contains("undefined_node") ) exists_malformed = true;
      if( result.contains("exists_malformed") ) exists_malformed = true;

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
  public TokenSet mark_form_graph(LabelList root_node_label_list){
    return mark_form_graph(root_node_label_list, true);
  }


  /*--------------------------------------------------------------------------------
    Graph traversal
  */

  // Given a node label, looks it up in the dependency graph, returns the node or null
  public Node lookup(Label node_label, boolean verbose){
    if(node_label == null || node_label.isEmpty()){
      if(verbose) System.out.println("lookup:: given node_label is null or an empty string");
      return null;
    }

    // Try the map
    Node node = this.node_map.get(node_label);
    if(node != null){
      node.put("label", node_label);
      if(verbose) System.out.println("lookup:: found from map: " + node);
      return node;
    }
    // At this point, node will be null

    // The map lookup failed, let's try the function recognizer list
    Node match_result = null;
    for (Function<Label, Node> func : this.node_f_list) {
      Node match_result = func.apply(node_label); 
      if("matched".equals(match_result.get("status"))){
        node = match_result;
        break;
      }
    }

    if(verbose){
      if(node != null) System.out.println("lookup:: found from recognizer function: " + node);
      else System.out.println("lookup:: failed to find label: " + node_label);
    }

    return node;
  }
  public Node lookup(Label node_label){
    return lookup(node_label, true);
  }

  // Mark aware lookup function
  public Node lookup_marked_good(Label node_label, boolean verbose){
    Node node = lookup(node_label, verbose);
    if(node != null && marked_good_q(node)) return node;
    return null;
  }
  public Node lookup_marked_good(Label node_label){
    return lookup_marked_good(node_label, true);
  }

  /*
    Given `root_node_label_list` of a DAG, applies `node_function` to each node in a
    depth-first traversal order. Returns a set of error tokens encountered
    during traversal.

    `mark_form_graph` must be run on the DAG before this function is called, or
    `lookup_marked_good` will not function correctly.
  */
  public TokenSet all_DAG_DF(LabelList root_node_label_list, BiConsumer<Node ,TokenSet> node_function, boolean verbose) {
    if(verbose) System.out.println("all_DAG_DF::");

    TokenSet error_token_set = new HashSet<>();

    boolean accept_arg_list = true;
    if(node_function == null) {
      error_token_set.add("null_node_function");
      accept_arg_list = false;
    }
    if(!(node_function instanceof BiFunction)) {
      error_token_set.add("node_function_not_a_function");
      accept_arg_list = false;
    }
    if(root_node_label_list == null) {
      error_token_set.add("null_root_node_label_list");
      accept_arg_list = false;
    }
    if(root_node_label_list.isEmpty()) {
      error_token_set.add("empty_root_node_label_list");
      accept_arg_list = false;
    }
    if(!accept_arg_list) return error_token_set;

    TokenSet visited = new HashSet<>();
    List<Node> in_traversal_order = new ArrayList<>();

    Stack<Label> stack = new Stack<>();
    root_node_label_list.forEach(stack::push);

    while(!stack.isEmpty()) {
      Label node_label = stack.pop();

      Node node = lookup_marked_good(node_label, verbose);
      if(node == null) {
        error_token_set.add("lookup_fail");
        continue;
      }

      if(visited.contains(node.get("label"))) continue;
      visited.add((Label)node.get("label"));

      in_traversal_order.add(node);

      stack.addAll(LabelList)node.get("neighbor"));
    }

    Collections.reverse(in_traversal_order);
    for(Node node : in_traversal_order) {
      node_function.apply(node, error_token_set);
    }

    return error_token_set;
  }
  public TokenSet all_DAG_DF(LabelList root_node_label_list, BiConsumer<Node ,TokenSet> node_function){
    return all_DAG_DF(root_node_label_list, node_function, true);
  }

  /*--------------------------------------------------------------------------------
    run the build scripts
    depends upon is_acyclic having already marked up the graph.
  */

  // A dependency is "good" if it is marked good, and for leaf or path, if the
  // corresponding file exists
  public boolean good_dependency_q(LabelList node_labels){
    return node_labels.stream().allMatch(node_label -> {
        Node node = lookup_marked_good(node_label);
        if( node == null ) return false;
        if(
          ("path".equals(node.get("type")) || "leaf".equals(node.get("type")) )
          && !file_exists_q( (Label) node.get("label") )
        ){
          return false;
        }
        return true;
      });
  }

  /*
    Given a node label and a list of node labels, returns true if the file at the
    node label in the first argument is newer than all the files at the
    corresponding node labels in the second list.
  */
  public boolean newer_than_all(Label node_label, LabelList node_label_list) throws IOException {
    Path node_path = Paths.get(node_label);
    if (!Files.exists(node_path)) return false;

    long node_last_modified = Files.getLastModifiedTime(node_path).toMillis();

    return node_label_list.stream().allMatch(label -> {
        try {
          Path path = Paths.get(label);
          if (!Files.exists(path)) return false;
          long last_modified = Files.getLastModifiedTime(path).toMillis();
          return node_last_modified > last_modified;
        } catch (IOException e) {
          return false;
        }
      });
  }

  public boolean can_be_built_q(Node node) {
    if( !marked_good_q(node) ) return false;
    if(
       ( "symbol".equals(node.get("type")) || "path".equals(node.get("type")) )
        && !good_dependency_q( (LabelList)node.get("neighbor") ) 
    ){
      return false;
    }
    if( 
       "leaf".equals( node.get("type") ) 
       && !file_exists_q( (Label)node.get("label") )
    ){
      return false;
    }
    return true;
  }

  // `can_be_build_q` must be true for this to be meaningful:
  public boolean should_be_built_q(Node node, boolean verbose) throws IOException {
    if ("leaf".equals(node.get("type"))) return false;
    if ("symbol".equals(node.get("type"))) return true;
    if ("path".equals(node.get("type"))) return !newer_than_all((Label) node.get("label"), (NodeLabelList) node.get("neighbor"));
    
    if (verbose) {
      System.out.println("should_be_build_q:: unrecognized node type, so assuming it should not be built.");
    }
    return false;
  }
  public boolean should_be_built_q(Node node) throws IOException {
    return should_be_built_q(node, true);
  }

  /*
    Runs the build scripts, assuming the graph has been marked up already.
  */
  public void run_build_scripts_f(NodeLabelList root_node_label_list, boolean verbose) throws IOException {

    if(root_node_label_list.isEmpty()) return;

    TokenSet error_token_set = new HashSet<>(); // used to catch return values

    System.out.println("run_build_script:: Checking if graph is well formed.");
    error_token_set = mark_form_graph(root_node_label_list);
    if(error_token_set != null && !error_token_set.isEmpty()) {
      System.out.println("Graph is not well-formed. Expect build problems. Errors:");
      error_token_set.forEach(token -> System.out.println("  - " + token));
    } else {
      System.out.println("Graph is well-formed. Proceeding with build.");
    }

    // Define the node function
    BiConsumer<Node, TokenSet> node_function = (node, error_token_set_2) -> {
      if(!can_be_built_q(node)) {
        System.out.println("run_build_scripts_f:: Skipping build for " + node.get("label") + " due to problems with dependencies.");
        return;
      }
      if(!should_be_built_q(node)) {
        if(verbose) System.out.println("run_build_scripts_f:: " + node.get("label") + " already up to date");
        return;
      }

      // Build the target
      System.out.println("run_build_scripts_f:: Running build script for " + node.get("label"));
      // Assuming node.build() is a method in the Map or a related object
      // Replace this with the actual build function for the node
      // node.build();

      // For path nodes, check if the build updated the target path
      if("path".equals(node.get("type")) && should_be_built_q(node)) {
        System.out.println("run_build_scripts_f:: Build failed for " + node.get("label"));
        set_mark(node, "build_failed");
      }
    };

    System.out.println("run_build_scripts_f:: running ...");
    error_token_set = all_DAG_DF(root_node_label_list, node_function, verbose);
    if(error_token_set != null) {
      error_token_set.forEach(error -> System.out.println("run_build_scripts_f::all_DAG_DF:: " + error));
    }
  }
  public void run_build_scripts_f(NodeLabelList root_node_label_list) throws IOException {
    run_build_scripts_f(root_node_label_list, true);
  }

}
