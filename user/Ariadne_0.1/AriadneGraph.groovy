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
   Graph traversal and build functions
  */

  def lookup( String node_label ,boolean verbose = false ){
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

  def run_build_scripts_f( List root_node_labels ,boolean verbose = true ){
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
