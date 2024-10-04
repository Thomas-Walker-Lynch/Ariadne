import java.nio.file.Files
import java.nio.file.Paths

class AriadneGraph {

  // Instance variables for graph data if needed
  Map node_map = [:]
  List node_f_list = []

  // Constructor to accept a graph definition (node_map and node_f_list)
  AriadneGraph( Map node_map ,List node_f_list ){
    this.node_map = node_map ?: [:]
    this.node_f_list = node_f_list ?: []
  }

  /*--------------------------------------------------------------------------------
   File utility functions
  */

  static Map unpack_file_path( String file_fp ){
    def file = new File( file_fp )

    def parent_dp = file.getParent()
    def file_fn = file.getName()
    def file_fn_base = file_fn.lastIndexOf('.') > 0 ? file_fn[ 0..file_fn.lastIndexOf('.') - 1 ] : file_fn
    def file_fn_ext = file_fn.lastIndexOf('.') > 0 ? file_fn[ file_fn.lastIndexOf('.') + 1..-1 ] : ''

    return [
      parent_dp: parent_dp
      ,file_fn: file_fn
      ,file_fn_base: file_fn_base
      ,file_fn_ext: file_fn_ext
    ]
  }

  static boolean file_exists_q( String node_label ){
    def node_path = Paths.get( node_label )
    return Files.exists( node_path )
  }

  /*--------------------------------------------------------------------------------
   Node type checks and marking
  */

  static Set all_node_type_set = ['symbol' ,'path' ,'leaf' ,'generator'] as Set
  static Set persistent_node_mark_set = ['cycle_member' ,'wellformed' ,'build_failed'] as Set

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
    ,'mark_property_value_must_be_set'
    ,'unregistered_mark'
    ,'missing_required_build_code'
    ,'leaf_given_neighbor_property'
    ,'leaf_given_build_property'
  ] as Set

  static Set wellformed_q( Map node ){
    def form_error_set = [] as Set

    if( !node ){
      form_error_set << 'no_node'
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

  /*
   Given a node label list. Applies well_formed_q to each node and marks the
   node accordingly. Returns 'all_wellformed' or 'exists_malformed'.
  */
  def mark_the_wellformed_f(node_label_list ,boolean verbose = true){
    def all_wellformed = true

    def neighbors = node_label_list.collect{ neighbor_label ->
      def neighbor_node = lookup(neighbor_label)
      def form_errors = wellformed_q(neighbor_node)
      if(form_errors.isEmpty()){
        neighbor_node.mark = neighbor_node.mark ?: [] as Set
        neighbor_node.mark << 'wellformed'
      } else {
        all_wellformed = false
        if(verbose){
          if(neighbor_node.label && neighbor_node.label.length() > 0){
            print("node ${neighbor_node.label} is malformed due to:")
          } else {
            print("anonymous node is malformed due to:")
          }
          form_errors.each { error -> print(" ${error}") }
          println("")
        }
      }
      neighbor_label
    }

    return all_wellformed ? 'all_wellformed' : 'exists_malformed'
  }

  /*
   Given a path stack initialized with the path root ,descends to a leaf node
   while looking for cycles. Marks nodes as 'cycle_member' if a cycle is
   detected. Marks nodes as `wellformed` if `wellformed_q`.  Returns a set of
   tokens indicating the status: 'cycle_found' ,'defacto_leaf_node' ,and
   'exists_malformed'.
  */
  def markup_graph_f_descend(path_stack ,boolean verbose = true){
    def ret_value = [] as Set
    def local_path = path_stack.collect{ it[0] }
    def local_node_label = local_path[-1]
    def cycle_start_index

    do{
      // Check for a cycle in the local path
      cycle_start_index = local_path[0..-2].findIndexOf{ it == local_node_label }
      if(cycle_start_index != -1){ // Cycle detected
        ret_value << 'cycle_found'
        if(verbose) print "markup_graph_f_descend:: dependency cycle found:"
        local_path[cycle_start_index..-1].each{ cycle_node_label ->
          def cycle_node = lookup(cycle_node_label)
          if(verbose) print " ${cycle_node.label}"
          cycle_node.mark = cycle_node.mark ?: [] as Set // Initialize mark set if needed
          cycle_node.mark << 'cycle_member'
        }
        if(verbose) println ""
        // we can not continue searching after the loop so ,we pop back to treat
        // the first node in the loop as though a leaf node.
        path_stack = path_stack[0..cycle_start_index]
        return ret_value
      }

      // a 'de-facto' leaf node test subtleties here because we have not yet
      // determined if the nodes we are wellformed. This is purposeful ,as
      // this function does not know about the relationships between the 
      // possible error marks.
      def local_node = lookup(local_node_label)
      if(local_node.neighbor.isEmpty()){
        ret_value << 'defacto_leaf_node'
        return ret_value
      }

      // Mark the wellformed nodes and get the result
      def result = mark_the_wellformed_f(local_node.neighbor ,verbose)
      if(result == 'exists_malformed'){
        ret_value << 'exists_malformed'
      }

      // Descend further into the tree.
      path_stack << local_node.neighbor.clone()
      local_node_label = local_node.neighbor[0]
      local_path << local_node_label
    }while(true)
  }

  /*
   Given root_node_labels ,marks up the graph and returns a set possibly
   containing 'all_wellformed' and 'cycles_exist'.

   Marks potentially added to each node include  'cycle_member' ,'wellformed'.
   Note that these marks are independent.
  */
  def wellformed_graph_q(root_node_labels ,boolean verbose = true){
    def ret_value = [] as Set
    def exists_malformed = false;

    // check the root nodes
    def result = mark_the_wellformed_f(root_node_labels ,verbose)
    if(result == 'exists_malformed'){
      ret_value << 'exists_malformed'
    }

    // Initialize the DFS tree iterator.
    def path_stack = []
    path_stack << root_node_labels.clone()

    // iterate over left side tree descent ,not ideal as it starts at the
    // root each time ,but avoids complexity in the cycle detection logic.
    do{
      def result = markup_graph_f_descend(path_stack ,verbose)
      if('cycle_found' in result) ret_value << 'cycle_exists'
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

  Map lookup( String node_label ,boolean verbose = false ){
    def lookup_node = node_map[ node_label ]
    if( !lookup_node ){
      def match_result
      for( func in node_f_list ){
        match_result = func( node_label )
        if( match_result.status == "matched" ){
          lookup_node = match_result
          break
        }
      }
    }

    if( !lookup_node && verbose ) println "lookup:: Node ${node_label} could not be found."
    return lookup_node
  }

  // mark aware lookup function
  def lookup_marked_good(node_label ,verbose = false){
    def node = lookup(node_label ,verbose)
    if( node && marked_good_q(node) ) return node;
    return null;
  }


  /*
   Given `root_node_labels` of a DAG. Applies `node_function` to each node in a
   depth-first traversal order.  Returns a set of error tokens encountered
   during traversal.

   `wellformed_graph_q` must be run on the DAG before this function is called ,or
   `lookup_marked_good` will not function correctly.
  */
  def all_DAG_DF(root_node_labels ,node_function ,boolean verbose = true) {
    def error_token_set = [] as Set

    if (root_node_labels.isEmpty()) return error_token_set

    def visited = [] as Set
    def in_traversal_order = []
    def stack = []

    root_node_labels.each { root_label ->
      stack << root_label
    }

    do {
      def node_label = stack.pop()

      def node = lookup_marked_good(node_label ,verbose)
      if (!node) {
        error_token_set << 'lookup_fail'
        continue
      }

      if (node.label in visited) continue
      visited << node.label

      in_traversal_order << node

      node.neighbor.each { neighbor_label ->
        stack << neighbor_label
      }
    } while (!stack.isEmpty())

    in_traversal_order.reverse().each { node ->
      node_function(node ,error_token_set ,verbose)
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

  void run_build_scripts_f( List root_node_labels ,boolean verbose = true ){
    if( root_node_labels.isEmpty() ) return

    def node_function = { node ,error_token_set ->

      if( !can_be_built_q( node ) ){
        println( "Skipping build for ${node.label} due to dependency problems" )
        return
      }
      if( !should_be_built_q( node ) ){
        if( verbose ) println( "${node.label} already up to date" )
        return
      }

      println( "Running build script for ${node.label}" )
      node.build( node ,node.neighbor )

      if( should_be_built_q( node ) ){
        println( "Build failed for ${node.label}" )
        set_mark( node ,'build_failed' )
      }
    }

    println( "run_build_scripts_f:: running ..." )
    all_DAG_DF( root_node_labels ,node_function ,verbose )
  }

  // Add the rest of your methods here as instance/static methods based on whether they depend on the graph instance

}


/*
 def clean(nodes_to_clean) {
  def all_dependencies = node_map["all"].neighbor.clone()
  nodes_to_clean.each { node ->
    all_dependencies.remove(node)
  }

  def must_have_nodes = []
  all_dependencies.each { node ->
    def node_info = node_map[node]
    if (node_info.must_have) {
      must_have_nodes += node_info.must_have
    }
  }

  def to_clean_list = []
  nodes_to_clean.each { node ->
    if (!must_have_nodes.contains(node) && node_map[node].type == "path") {
      to_clean_list += node
    }
  }

  to_clean_list.each { node ->
    def file_path = node_map[node].label
    def file = new File(file_path)
    if (file.exists()) {
      file.delete()
      println "Deleted file: ${file_path}"
    }
  }
}
*/
