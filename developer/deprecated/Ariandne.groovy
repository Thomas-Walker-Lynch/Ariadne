/*
 Some terms:

 a node is either malformed or 'wellformed'.  A wellformed node meets
 the criteria set forth by `well_formed_node_q`.

 'node': a dictionary, hopefully a wellformed one
 'file node': a 'path' or 'leaf' type node.
 'node file': for 'path' or 'leaf' nodes, path relative to the developer's directory.

 This code makes use of the following node properties:
 - label: Unique label string. Used to reference nodes.
 - type: The type of the node: 'leaf' ,'symbol' ,or 'path'.
 - neighbor: list of the neighbor nodes labels
 - must_have: list of node labels
 - build: Code that builds the node.
 - mark: a set of 'mark' tokens optionally placed on a node

 For 'path' and 'leaf' type nodes, the node label is a file path.  Relative paths
 are relative to the developer's directory.

 The dependency graph is defined by the `lookup` function. `lookup` when given
 a node label returns the node, if found, or null.

*/

/*--------------------------------------------------------------------------------
 file utility functions
*/

def unpack_file_path = { file_fp ->
  def file = new File(file_fp)  // Renamed to 'file' to avoid overwriting 'file_fn'

  // Get the parent directory path
  def parent_dp = file.getParent()

  // Get the file name (with extension)
  def file_fn = file.getName()

  // Split the file name into base and extension
  def file_fn_base = file_fn.lastIndexOf('.') > 0 ? file_fn[0..file_fn.lastIndexOf('.') - 1] : file_fn
  def file_fn_ext = file_fn.lastIndexOf('.') > 0 ? file_fn[file_fn.lastIndexOf('.') + 1..-1] : ''

  // Return the components as a map for easier use
  return [
    parent_dp: parent_dp
    ,file_fn: file_fn
    ,file_fn_base: file_fn_base
    ,file_fn_ext: file_fn_ext
  ]
}

def file_exists_q(node_label) {
  def node_path = Paths.get(node_label)
  return Files.exists(node_path)
}


/*-------------------------------------------------------------------------------
  DAG adjectives

  node undefined for undefined node?
*/

// A node is type 'leaf' when the node file can not be deleted or replaced.
// This type used to be called 'primary', which might be a better term.

def all_node_type_set = ['symbol' ,'path' ,'leaf' ,'generator'] as Set

def persistent_node_mark_set = ['cycle_member' ,'wellformed' ,'build_failed'] as Set
def leaf_q = { node -> node && node.type && node.type == 'leaf' }

// mark 
def has_mark = { node -> node.mark && !node.mark.isEmpty() }
def set_mark(node ,mark){
  node.mark = node.mark ?: [] as Set
  node.mark << mark
}
def clear_mark(node ,mark){
  if( node.mark ) node.mark.remove(mark)
}
def marked_good_q(node){
  return (
    node 
    && node.mark 
    && ('wellformed' in node.mark) 
    && !('cycle_member' in node.mark)
    && !('build_failed' in node.mark)
  )
}



/*--------------------------------------------------------------------------------
 Wellformed Node Check

 The developer defines the nodes, so their form needs to be checked.

 early bail on node not defined
 legal path in the build directory makedir -p ... not really a acquirement 

*/
def all_form_error_set = [
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
]
def wellformed_q = { node -> 
  def form_error_set = [] as Set

  if( !node ){
    form_error_set << 'no_node'
    return form_error_set
  }

  if( !node.label )
    form_error_set << 'node_must_have_label'
  else if( !(node.label instanceof String) )
    form_error_set << 'label_must_be_string'

  if( !node.type )
    form_error_set << 'node_must_have_type'
  else if( !(node.type instanceof String) || !(node.type in all_node_type_set) )
    form_error_set << 'bad_node_type'
    
  if( node.neighbor ){
    if( !(node.neighbor instanceof List) )
      form_error_set << 'neighbor_value_must_be_list'
    else if( !(node.neighbor.every { it instanceof String }) )
      form_error_set << 'neighbor_reference_must_be_string'
  }

  if( node.mark ){
    if( !(node.mark instanceof Set) ) 
      form_error_set << 'mark_property_value_must_be_set'
    else if( !(node.mark.every { it in persistent_node_mark_set }) )
      form_error_set << 'unregistered_mark'
  }

  // Removed test about symbol build needing neighbors

  if( 
    node.type == "path" 
    && (!node.build || !(node.build instanceof Closure))
  )
    form_error_set << 'missing_required_build_code'

  // Removed test about path needing neighbors

  // Can a primary file have neighbors or a build script? Our build model
  // has the node file as a target, the build script as a means of building
  // the target (hence its name), and the dependencies as inputs to the build
  // script. So in the spirit of this model, we will say "no".
  if( node.type == "leaf" ){
    if( node.neighbor ) form_error_set << 'leaf_given_neighbor_property'
    if( node.build ) form_error_set << 'leaf_given_build_property'
  }

  return form_error_set
}



/*--------------------------------------------------------------------------------
 A well formed graph checker.  Traverses entire graph and marks nodes
 that are not well formed or that are part of a cycle.

*/

// given a node label list, adds the 'wellformed' mark to wellformed nodes.
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

// descends un-visited leftmost path, while marking nodes as wellformed and if
// they are part of a cycle.
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



/*--------------------------------------------------------------------------------
 This function defines the graph.

 Lookup attempts to lookup a node label in the node_map, and failing that, it
 tries each label pattern recognition function in order.

 lookup_marked_good can be run after `wellformed_graph_f` has marked up the
 graph.  It will only return a node if a) found b) that is 'good'.
 Note the `marked_good_q` adjective above.

*/
def lookup(node_label ,verbose = false){
  def lookup_node = node_map[node_label]
  if( lookup_node ){
    lookup_node.label = node_label
  } else {
    def match_result
    for(func in node_f_list){
      match_result = func(node_label)
      if(match_result.status == "matched"){
        lookup_node = match_result
        break
      }
    }
  }
  if( !lookup_node ){
    if( verbose ) println "lookup:: Node ${node_label} could not be found."
    return null
  }
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
*/

import java.nio.file.Files
import java.nio.file.Paths

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

/*
 Runs build scripts for the given root_node_labels in a depth-first traversal order.
 Uses `all_DAG_DF` to traverse the graph and applies the build function to each node.

 Be sure that `wellformed_graph_q` has been run on DAG.

 Be sure that the 'build_failed' marks have been cleared if this is not the
 first build attempt.
 
*/
def run_build_scripts_f(root_node_labels ,boolean verbose = true){
  if( root_node_labels.isEmpty() ) return

  // Define the function to be applied to each node
  def node_function = { node ,error_token_set ->

    if( !can_be_built_q(node) ){
      println("Skipping build for ${node.label} due to dependency problems or found leaf is missing")
      return
    }

    if( !should_be_built_q(node) ){
      if(verbose) println("${node.label} already up to date")
      return
    }

    // if we get here, node can and should be built

    println("Running build script for ${node.label}")
    node.build(node ,node.neighbor)

    // Check if the build updated the target file
    if( should_be_built_q(node) ){
      println("Build failed for ${node.label}")
      set_mark(node ,'build_failed')
    }

  }

  println("run_build_scripts_f:: running ...")
  // Apply the function to all nodes in a depth-first manner
  all_DAG_DF(root_node_labels ,node_function ,verbose)
}


// LocalWords:  FN FPL DN DNL RuleNameListRegx RuleNameList PrintVisitorMethod
// LocalWords:  PrintVisitor SyntaxAnnotate wellformed defacto acyclic
// LocalWords:  wellformed unvisited
