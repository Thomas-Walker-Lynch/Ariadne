class BuildGraph {

  // Function to load the graph class dynamically
  static def include_a_class(String a_class_fp) {
    def class_loader = BuildGraph.class.classLoader
    def class_name = a_class_fp.replace('/', '.').replace('.class', '')
    try {
      return class_loader.loadClass(class_name)
    } catch (Exception e) {
      println "Error loading class '${class_name}': ${e.message}"
      return null
    }
  }

  // Build function
  static def build(String graph_definition_fp, List<String> root_node_labels) {

    // Print summary of what we are doing
    println "build:: Building targets for graph '${graph_definition_fp}.class'"
    if (root_node_labels.isEmpty()) {
      println "No build targets specified. Please provide root node labels to build."
      System.exit(0)
    }
    println "Building targets: ${root_node_labels.join(', ')}"

    // Load the dependency graph class from arg[1]
    def graph_definition_class = include_a_class(graph_definition_fp)
    if (graph_definition_class) {
      println "build:: loaded ${graph_definition_fp}.class"
    } else {
      println "build:: failed to load ${graph_definition_fp}.class"
      System.exit(1)
    }

    // Get the node_map and node_f_list from the graph class
    def node_map = graph_definition_class.get_node_map()
    def node_f_list = graph_definition_class.get_node_f_list()
    println "node_map: ${node_map}"
    println "node_f_list: ${node_f_list}"

    // Create an instance of AriadneGraph, and run the build scripts
    def graph = new AriadneGraph(node_map, node_f_list)
    graph.run_build_scripts_f(root_node_labels)
  }

  // Entry point when run as a script
  static void main(String[] args) {
    if (args.length == 0) {
      println "Usage: ./build <graph_definition.class> [root_node_labels...]"
      System.exit(1)
    }

    // Get graph definition file and root node labels
    def graph_definition_fp = args[0]
    def root_node_labels = args.length > 1 ? args[1..-1] : []
    build(graph_definition_fp, root_node_labels)
  }
}
