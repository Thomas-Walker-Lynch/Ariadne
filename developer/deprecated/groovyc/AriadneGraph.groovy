import java.nio.file.Files
import java.nio.file.Paths

class AriadneGraph {

  static Boolean debug = true
  Map node_map = [:]
  List node_f_list = []

  AriadneGraph(Map node_map ,List node_f_list){
    def accept_arg_list = true;
    if( !(node_map === null) && !(node_map instanceof Map) ) accept_arg_list = false
    if( !(node_f_list === null) && !(node_f_list instanceof List) ) accept_arg_list = false
    if(node_map === null && node_f_list === null) accept_arg_list = false
    if(!accept_arg_list){
      println "AriandreGraph: requiers one or both of 'node_map' as Map, and 'node_f_list as List.'"
      System.exit(1)
    }
    this.node_map = node_map ?: [:]
    this.node_f_list = node_f_list ?: []
  }

  /*--------------------------------------------------------------------------------
   File utility functions
  */
  static Map unpack_file_path(String file_fp) {
    if (debug) println("unpack_file_path::file_fp: ${file_fp}")

    def file = new File(file_fp)
    def parent_dp = file.getParent() ?: ""

    if (parent_dp && !parent_dp.endsWith(File.separator)) {
      parent_dp += File.separator
    }

    def file_fn = file.getName()
    def file_fn_base = file_fn
    def file_fn_ext = ''

    if (file_fn.lastIndexOf('.') > 0) {
      file_fn_base = file_fn[0..file_fn.lastIndexOf('.') - 1]
      if (file_fn.lastIndexOf('.') + 1 < file_fn.length()) {
        file_fn_ext = file_fn[file_fn.lastIndexOf('.') + 1..-1]
      }
    }

    def ret_val = [
      dp      : parent_dp,
      fn      : file_fn,
      fn_base : file_fn_base,
      fn_ext  : file_fn_ext
    ]
    if (debug) println("unpack_file_path::ret_val: ${ret_val}")

    return ret_val
  }

  static boolean file_exists_q( String node_label ){
    def node_path = Paths.get( node_label )
    return Files.exists( node_path )
  }

  /*--------------------------------------------------------------------------------
   Node type checks and marking
  */

  static Set all_node_type_set = [
    'symbol'  // label is a symbol
    ,'path'   // label is a path to a file, though it might not exist
    ,'leaf'   // label is a path to a file that has no dependencies
    ,'generator' // label is a path, but node has no neighbors
    ,'error'   // typically created by the system node has a message property
  ] as Set

  static Set persistent_node_mark_set = 
    [
    'cycle_member' 
     ,'wellformed' 
     ,'build_failed'
     ,'null_node'
     ] as Set

  static boolean leaf_q( Map node ){
    return node && node.type == 'leaf'
  }

  static boolean has_mark( Map node ){
    return node?.mark?.isNotEmpty()
  }

  static void set_mark( Map node ,String mark ){
    node.mark = node.mark ?: [] as Set
    node.mark << mark
  }

  static void clear_mark( Map node ,String mark ){
    node?.mark?.remove( mark )
  }

  static boolean marked_good_q( Map node ){
    return node && node.mark && ( 'wellformed' in node.mark ) && !( 'cycle_member' in node.mark ) && !( 'build_failed' in node.mark )
  }

  /*--------------------------------------------------------------------------------
   Well-formed Node Check
  */

  static Set all_form_error_set = [
    'no_node'
    ,'node_must_have_label'
    ,'label_must_be_string'
    ,'node_must_have_type'
    ,'bad_node_type'
    ,'neighbor_value_must_be_list'
    ,'neighbor_reference_must_be_string'
    ,'neighbor_label_not_in_graph'
    ,'mark_property_value_must_be_set'
    ,'unregistered_mark'
    ,'missing_required_build_code'
    ,'leaf_given_neighbor_property'
    ,'leaf_given_build_property'
  ] as Set

  static Set wellformed_q( Map node ){
    def form_error_set = [] as Set

    if( !node ){
      form_error_set << 'null_node'
      return form_error_set
    }

    if( !node.label )
      form_error_set << 'node_must_have_label'
    else if( !( node.label instanceof String ) )
      form_error_set << 'label_must_be_string'

    if( !node.type )
      form_error_set << 'node_must_have_type'
    else if( !( node.type instanceof String ) || !( node.type in all_node_type_set ) )
      form_error_set << 'bad_node_type'

    if( node.neighbor ){
      if( !( node.neighbor instanceof List ) )
        form_error_set << 'neighbor_value_must_be_list'
      else if( !( node.neighbor.every { it instanceof String } ) )
        form_error_set << 'neighbor_reference_must_be_string'
    }

    if( node.mark ){
      if( !( node.mark instanceof Set ) )
        form_error_set << 'mark_property_value_must_be_set'
      else if( !( node.mark.every { it in persistent_node_mark_set } ) )
        form_error_set << 'unregistered_mark'
    }

    if( node.type == 'path' && ( !node.build || !( node.build instanceof Closure ) ) )
      form_error_set << 'missing_required_build_code'

    if( node.type == 'leaf' ){
      if( node.neighbor ) form_error_set << 'leaf_given_neighbor_property'
      if( node.build ) form_error_set << 'leaf_given_build_property'
    }

    return form_error_set
  }

  /*--------------------------------------------------------------------------------
   A well formed graph checker.  Traverses entire graph and marks nodes
   that are not well formed or that are part of a cycle.

   This must be run on the graph for `lookup_marked_good` to work.
  */

  def mark_node_form(node ,verbose = true){
    if(debug){
      if(node)
        println("mark_node_form::node: ${node}")
      else
        println("mark_node_form given a null node")
    }
    
    def form_errors = wellformed_q(node)
    if( form_errors.isEmpty() ){
      set_mark(node ,'wellformed');
      return 'wellformed'
    }
    // at this point we know that form_errors is not empty
    
    if(verbose){
      if(node && node.label && node.label.length() > 0)
        print("node ${neighbor_node.label} is malformed due to:")
      else
        print("anonymous node is malformed due to:")
      form_errors.each { error -> print(" ${error}") }
      println("")
    }

    return 'malformed'
  }


  static Set markup_graph_f_descend_set = [
    'empty_path_stack'
    ,'cycle_found'
    ,'undefined_node'
    ,'exists_malformed'
    ,'defacto_leaf'
  ] as Set
  def markup_graph_f_descend(path_stack ,boolean verbose = true){
    def ret_value = [] as Set
    if( path_stack.isEmpty() ){
      if(verbose) println( "markup_graph_f_descend:: given empty path_stack to descend from")
      ret_value << 'empty_path_stack'
      return ret_value
    }
    def local_path = path_stack.collect{ it[0] }
    def local_node_label = local_path[-1]
    def cycle_start_index

    do{

      // Check for a cycle in the local path, if found marks cycle members
      if( local_path.size() > 1){
        cycle_start_index = local_path[0..-2].findIndexOf{ it == local_node_label }
        if(cycle_start_index != -1){ // Cycle detected
          ret_value << 'cycle_found'
          if(verbose) print "markup_graph_f_descend:: dependency cycle found:"
          local_path[cycle_start_index..-1].each{ cycle_node_label ->
            if(verbose) print " ${cycle_node_label}"
            def cycle_node = lookup(cycle_node_label)
            cycle_node.mark = cycle_node.mark ?: [] as Set // Initialize mark set if needed
            cycle_node.mark << 'cycle_member'
          }
          if(verbose) println ""
          // we can not continue searching after the loop so ,we pop back to treat
          // the first node in the loop as though a leaf node.
          path_stack = path_stack[0..cycle_start_index]
          return ret_value
        }
      }

      def local_node = lookup(local_node_label)
      if( !local_node ){
        ret_value << 'undefined_node' 
        return ret_value
      }
      if( mark_node_form(local_node) == 'malformed' ){
        ret_value << 'exists_malformed'
      }
      if( local_node.neighbor.isEmpty() ){
        ret_value << 'defacto_leaf' // might not be `type:leaf`
        return ret_value
      }

      // Descend further into the tree.
      path_stack << local_node.neighbor.clone()
      local_node_label = local_node.neighbor[0]
      local_path << local_node_label
    }while(true)
  }

  /*
   Given root_node_label_list ,marks up the graph and returns a set possibly
   containing 'all_wellformed' and 'cycles_exist'.

   Marks potentially added to each node include  'cycle_member' ,'wellformed'.
   Note that these marks are independent.
  */
  def wellformed_graph_q(root_node_label_list ,boolean verbose = true){
    def ret_value = [] as Set
    def exists_malformed = false;
    def result // used variously

    if( root_node_label_list.isEmpty() ) return ret_value
    
    // Initialize the DFS tree iterator.
    def path_stack = []
    path_stack << root_node_label_list.clone()

    // iterate over left side tree descent ,not ideal as it starts at the
    // root each time ,but avoids complexity in the cycle detection logic.
    do{
      result = markup_graph_f_descend(path_stack ,verbose)
      if('cycle_found' in result) ret_value << 'cycle_exists'
      if('undefined_node' in result) exists_malformed = true;
      if('exists_malformed' in result) exists_malformed = true;

      // increment the iterator to the next leftmost path
      def top_list = path_stack[-1]
      top_list.remove(0)
      if(top_list.isEmpty()) path_stack.pop()

    }while(!path_stack.isEmpty())

    if(!exists_malformed) ret_value << 'all_wellformed'
    if( verbose ){
      if(exists_malformed) println("one or more malformed nodes were found")
      def exists_cycle = 'cycle_found' in ret_value
      if(exists_cycle) println("one or more cyclic dependency loop found")
      if( exists_malformed || exists_cycle ) println("will attempt to build unaffected nodes")
    }

    return ret_value
  }

  /*--------------------------------------------------------------------------------
     Graph traversal
  */

  // given a node label, looks it up on the dependency graph, returns the node or null
  Map lookup(String node_label ,boolean verbose = true){

    if(!node_label){
      if(verbose) println("lookup:: given node_label is null or an empty string")
      return null
    }

    // try the map
    def node = this.node_map[node_label]
    if(node){
      node.label = node_label
      if(verbose) println("lookup:: found from map: ${node}")
      return node
    }
    // at this point node will be null

    // The map lookup failed, lets try the function recognizer list ..
    def match_result
    for( func in this.node_f_list ){
      match_result = func(node_label)
      if( match_result.status == 'matched' ){
        node = match_result
        break
      }
    }

    if(verbose)
      if(node) println("lookup:: found from recognizer function: ${node}")
      else println("lookup:: failed to find label: ${node_label}")

    return node
  }

  // mark aware lookup function
  def lookup_marked_good(node_label ,verbose = true){
    def node = lookup(node_label ,verbose)
    if( node && marked_good_q(node) ) return node;
    return null;
  }


  /*
   Given `root_node_label_list` of a DAG. Applies `node_function` to each node in a
   depth-first traversal order.  Returns a set of error tokens encountered
   during traversal.

   `wellformed_graph_q` must be run on the DAG before this function is called ,or
   `lookup_marked_good` will not function correctly.
  */
  def all_DAG_DF(root_node_label_list ,node_function ,boolean verbose = true) {
    if(verbose) println("all_DAG_DF::")

    def error_token_set = [] as Set

    def accept_arg_list = true
    if( !node_function ){
      error_token_set << 'null_node_function'
      accept_arg_list = false
    }
    if( !(node_function instanceof Closure) ){
      error_token_set << 'nod_function_not_a_function'
      accept_arg_list = false
    }
    if( !root_node_label_list  ){
      error_token_set << 'null_root_node_label_list'
      accept_arg_list = false
    }
    if( root_node_label_list.isEmpty() ){
      error_token_set << 'empty_root_node_label_list'
      accept_arg_list = false
    }
    if( !accept_arg_list ) return error_token_set

    def visited = [] as Set
    def in_traversal_order = []

    def stack = []
    root_node_label_list.each { root_label ->
      stack << root_label
    }

    do {
      if( stack.isEmpty() ) break
      def node_label = stack.pop()

      def node = lookup_marked_good(node_label ,verbose)
      if(!node){
        error_token_set << 'lookup_fail'
        continue
      }

      if(node.label in visited) continue
      visited << node.label

      in_traversal_order << node

      node.neighbor.each { neighbor_label ->
        stack << neighbor_label
      }
    } while(true)

    in_traversal_order.reverse().each { node ->
      node_function(node ,error_token_set)
    }

    return error_token_set
  }

  /*--------------------------------------------------------------------------------
   run the build scripts
     depends upon is_acyclic having already marked up the graph.

  import java.nio.file.Files
  import java.nio.file.Paths
  */

  // a symbol dependency is good ,as long as it is built before the node in question
  def good_dependency_q(node_labels) {
    return node_labels.every { node_label ->
      def node = lookup_marked_good(node_label)
      if (!node) return false
      if (node.type in ['path' ,'leaf'] && !file_exists_q(node.label)) return false
      return true
    }
  }

  /* 
   Given a node label and a list of node labels ,returns true if the file at the
   node label in the first argument is newer than all the files at the
   corresponding node labels in the second list.
  */
  def newer_than_all(node_label ,node_label_list) {
    def node_path = Paths.get(node_label)
    if (!Files.exists(node_path)) return false

    def node_last_modified = Files.getLastModifiedTime(node_path).toMillis()

    return node_label_list.every { label ->
      def path = Paths.get(label)
      if (!Files.exists(path)) return false
      def last_modified = Files.getLastModifiedTime(path).toMillis()
      return node_last_modified > last_modified
    }
  }

  def can_be_built_q(node){
    if( !marked_good_q(node) ) return false;
    if( 
      (node.type == 'symbol' || type == 'path')
      && !good_dependency_q( node.neighbor )
    ){
      return false
    }
    if(
      node.type == 'leaf'
      && !file_exists_q(node.label)
    ){ 
      return false;
    }
    return true
  }

  // `can_be_build_q` must be true for this to be meaningful:
  def should_be_built_q(node ,verbose = true) {
    if(node.type == 'leaf') return false
    if(node.type == 'symbol') return true
    if( node.type == 'path') return !newer_than_all(node.label ,node.neighbor)
    println("should_be_build_q:: unrecognized node type ,so assuming it should not be built.")
    return false
  }

  void run_build_scripts_f( List root_node_label_list ,boolean verbose = true ){

    if( root_node_label_list.isEmpty() ) return
    Set error_token_set // used to catch return values

    println( "run_build_script:: Checking if graph is well formed." )
    error_token_set = wellformed_graph_q(root_node_label_list)
    if( error_token_set && !error_token_set.isEmpty() ){
      println( "Graph is not well-formed. Expect build problems. Errors:" )
      error_token_set.each { token ->
        println( "  - ${token}" )
      }
    } else {
      println( "Graph is well-formed. Proceeding with build." )
    }

    def node_function = { node ,error_token_set_2 ->

      if( !can_be_built_q( node ) ){
        println( "run_build_scripts_f:: Skipping build for ${node.label} due to problems with dependencies." )
        return
      }
      if( !should_be_built_q( node ) ){
        if( verbose ) println( "run_build_scripts_f:: ${node.label} already up to date" )
        return
      }

      // build the target
      println( "run_build_scripts_f:: Running build script for ${node.label}" )
      node.build()

      // for path nodes, check if the build updated the target at path
      if( node.type == 'path' && should_be_built_q( node ) ){
        println( "run_build_scripts_f:: Build failed for ${node.label}" )
        set_mark(node ,'build_failed')
      }

    }

    println("run_build_scripts_f:: running ...")
    error_token_set = all_DAG_DF(root_node_label_list, node_function, verbose)
    if( error_token_set ){
      error_token_set.each { error ->
        println("run_build_scripts_f::all_DAG_DF:: ${error}")
      }
   }

  }


}


/*
 def clean(nodes_to_clean) {
  def all_dependencies = this.node_map["all"].neighbor.clone()
  nodes_to_clean.each { node ->
    all_dependencies.remove(node)
  }

  def must_have_nodes = []
  all_dependencies.each { node ->
    def node_info = this.node_map[node]
    if (node_info.must_have) {
      must_have_nodes += node_info.must_have
    }
  }

  def to_clean_list = []
  nodes_to_clean.each { node ->
    if (!must_have_nodes.contains(node) && this.node_map[node].type == "path") {
      to_clean_list += node
    }
  }

  to_clean_list.each { node ->
    def file_path = this.node_map[node].label
    def file = new File(file_path)
    if (file.exists()) {
      file.delete()
      println "Deleted file: ${file_path}"
    }
  }
}
*/
