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
  /*
   import java.util.HashMap;
   import java.util.Map;
   import java.io.IOException;

   public class AriadneGraph {

    public static Map<String, Object> java_to_class(String node_label) {
        System.out.println("java_to_class::");

        Map<String, String> target = AriadneGraph.unpack_file_path(node_label);
        System.out.println("java_to_class_f:: given target: " + target);

        // This function recognizes <x>.class files
        if (target.get("fn") == null || !target.get("fn_ext").equals("class")) {
            Map<String, Object> noMatch = new HashMap<>();
            noMatch.put("status", "no_match");
            return noMatch;
        }

        System.out.println("java_to_class_f:: node_label " + node_label + " matched");

        String class_fp = node_label;
        String java_fp = target.get("dp") + target.get("fn_base") + ".java";

        // Create the node to return
        Map<String, Object> matchedNode = new HashMap<>();
        matchedNode.put("status", "matched");
        matchedNode.put("label", class_fp);
        matchedNode.put("type", "path");

        // List of neighbors
        matchedNode.put("neighbor", List.of(java_fp)); // The corresponding .java file

        // Define the build function as a lambda
        matchedNode.put("build", (Runnable) () -> {
            try {
                Process process = Runtime.getRuntime().exec("javac " + java_fp);
                process.waitFor();

                if (process.exitValue() == 0) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("status", "success");
                    result.put("output", class_fp);
                    return result;
                } else {
                    Map<String, Object> result = new HashMap<>();
                    result.put("status", "failure");
                    result.put("error", new String(process.getErrorStream().readAllBytes()));
                    return result;
                }
            } catch (IOException | InterruptedException e) {
                Map<String, Object> result = new HashMap<>();
                result.put("status", "failure");
                result.put("error", e.getMessage());
                return result;
            }
        });

        return matchedNode;
    }

    public static Map<String, String> unpack_file_path(String node_label) {
        // Stub implementation to mimic the unpack_file_path method
        // This should return a Map containing keys like "fn", "fn_ext", "dp", and "fn_base"
        Map<String, String> filePathMap = new HashMap<>();
        filePathMap.put("fn", "ExampleFile");
        filePathMap.put("fn_ext", "class");
        filePathMap.put("dp", "/path/to/");
        filePathMap.put("fn_base", "ExampleFileBase");

        return filePathMap;
    }

    public static void main(String[] args) {
        // Example usage
        Map<String, Object> node = java_to_class("ExampleFile.class");
        System.out.println(node);
    }
}
*/
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
