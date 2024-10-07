import AriadneGraph

class TestGraph {

  static def get_node_map(){
    return [:]
  }

  // given label <x>.class returns node to build <x>.class from <x>.java
  static java_to_class( node_label ){
    println("java_to_class::")

    def target=AriadneGraph.unpack_file_path(node_label)
    println("java_to_class_f:: given target: ${target}")

    // this function recognizes <x>.class files:
    if( !target.fn || target.fn_ext != 'class' ) return [status: 'no_match']
    println("java_to_class_f:: node_label ${node_label} matched")

    def class_fp = node_label
    def java_fp = target.dp + target.fn_base + '.java'

    return [
      status: 'matched'
      ,label: class_fp
      ,type: 'path'
      ,neighbor: [java_fp]  // The corresponding .java file
      ,build: {
        def process="javac ${java_fp}".execute()
        process.waitFor()
        if( process.exitValue() == 0 ){
          return [status: 'success' ,output: class_fp]
        } else {
          return [status: 'failure' ,error: process.err.text]
        }
      }
    ]
  }

  static java_leaf( node_label ){
    println("java_to_leaf::")

    def target = AriadneGraph.unpack_file_path( node_label )
    println("java_to_class_f:: given target: ${target}")

    // This function recognizes <x>.java files:
    if( !target.fn || target.fn_ext != 'java' ) return [status: 'no_match']
    println("java_to_class_f:: node_label ${node_label} matched")

    def java_fp = node_label

    return [
      status: 'matched'
      ,label: java_fp
      ,type: 'leaf'
      ,neighbor: []  // Leaf nodes have no dependencies
    ]
  }

  // Static method to define the function list
  static def get_node_f_list(){
    return (
      [
        { node_label -> java_to_class(node_label) }
        ,{ node_label -> java_leaf(node_label) }
      ]
    )
  }

}
