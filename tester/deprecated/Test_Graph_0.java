

public class Test_Graph_0 {

  public class TestSuite {

    // Test constructor with null parameters (expecting error message)
    public Boolean graph_constructor_null_params(IO io) {
      Boolean[] conditions = new Boolean[1];
      int i = 0;

      // Attempt to initialize Graph with both parameters null
      new Graph(null, null);

      // Capture stderr to check for error message
      String stderrContent = io.get_err_content();
      conditions[i++] = stderrContent.contains("AriadneGraph: At least one of 'node_map' (Map) or 'recognizer_f_list' (List) must be provided.");
      io.clear_buffers();  // Clear after validation
      return MU.all(conditions);
    }

    // Test lookup with populated node_map
    public Boolean graph_lookup_populated_map(IO io) {
      Boolean[] conditions = new Boolean[2];
      int i = 0;

      // Setup node_map with labeled nodes
      Map<Label, Node> nodeMap = new HashMap<>();
      Label label1 = new Label("node1");
      Label label2 = new Label("node2");
      Node node1 = new Node();
      Node node2 = new Node();
      nodeMap.put(label1, node1);
      nodeMap.put(label2, node2);

      Graph graph = new Graph(nodeMap, new ProductionList());

      // Test lookup for existing and non-existing labels
      conditions[i++] = graph.lookup(label1, true) == node1;
      conditions[i++] = graph.lookup(new Label("nonexistent"), true) == null;

      io.clear_buffers(); // Clear after each case
      return MU.all(conditions);
    }

    // Test lookup without verbosity
    public Boolean graph_lookup_non_verbose(IO io) {
      Boolean[] conditions = new Boolean[1];
      int i = 0;

      // Initialize node_map with one node
      Map<Label, Node> nodeMap = new HashMap<>();
      Label label = new Label("singleNode");
      Node node = new Node();
      nodeMap.put(label, node);

      Graph graph = new Graph(nodeMap, new ProductionList());

      // Perform lookup without verbosity
      Node result = graph.lookup(label, false);
      conditions[i++] = result == node;  // Expected to find node without verbose output

      return MU.all(conditions);
    }
  }

  public static void main(String[] args) {
    TestSuite suite = new Test_Graph_0().new TestSuite();
    int result = TestBench.run(suite); 
    System.exit(result);
  }
}
