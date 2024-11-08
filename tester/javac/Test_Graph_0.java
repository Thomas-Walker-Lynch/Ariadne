import java.util.HashMap;
import java.util.Map;

import com.ReasoningTechnology.Mosaic.Mosaic_Util;
import com.ReasoningTechnology.Mosaic.Mosaic_IO;
import com.ReasoningTechnology.Mosaic.Mosaic_Testbench;

import com.ReasoningTechnology.Ariadne.Ariadne_Graph;
import com.ReasoningTechnology.Ariadne.Ariadne_Label;
import com.ReasoningTechnology.Ariadne.Ariadne_Node;
import com.ReasoningTechnology.Ariadne.Ariadne_ProductionList;

public class Test_Graph_0 {

  public class TestSuite {

    // Test constructor with null parameters (expecting error message)
    public Boolean graph_constructor_null_params(Mosaic_IO io) {
      Boolean[] conditions = new Boolean[1];
      int i = 0;

      // Attempt to initialize Graph with both parameters null
      new Ariadne_Graph(null, null);

      // Capture stderr to check for error message
      String stderrContent = io.get_err_content();
      conditions[i++] = stderrContent.contains("AriadneGraph: At least one of 'node_map' (Map) or 'recognizer_f_list' (List) must be provided.");
      io.clear_buffers();  // Clear after validation
      return Mosaic_Util.all(conditions);
    }

    // Test lookup with populated node_map
    public Boolean graph_lookup_populated_map(Mosaic_IO io) {
      Boolean[] conditions = new Boolean[2];
      int i = 0;

      // Setup node_map with labeled nodes
      Map<Ariadne_Label, Ariadne_Node> nodeMap = new HashMap<>();
      Ariadne_Label label1 = new Ariadne_Label("node1");
      Ariadne_Label label2 = new Ariadne_Label("node2");
      Ariadne_Node node1 = new Ariadne_Node();
      Ariadne_Node node2 = new Ariadne_Node();
      nodeMap.put(label1, node1);
      nodeMap.put(label2, node2);

      Ariadne_Graph graph = new Ariadne_Graph(nodeMap, new Ariadne_ProductionList());

      // Test lookup for existing and non-existing labels
      conditions[i++] = graph.lookup(label1, true) == node1;
      conditions[i++] = graph.lookup(new Ariadne_Label("nonexistent"), true) == null;

      io.clear_buffers(); // Clear after each case
      return Mosaic_Util.all(conditions);
    }

    // Test lookup without verbosity
    public Boolean graph_lookup_non_verbose(Mosaic_IO io) {
      Boolean[] conditions = new Boolean[1];
      int i = 0;

      // Initialize node_map with one node
      Map<Ariadne_Label, Ariadne_Node> nodeMap = new HashMap<>();
      Ariadne_Label label = new Ariadne_Label("singleNode");
      Ariadne_Node node = new Ariadne_Node();
      nodeMap.put(label, node);

      Ariadne_Graph graph = new Ariadne_Graph(nodeMap, new Ariadne_ProductionList());

      // Perform lookup without verbosity
      Ariadne_Node result = graph.lookup(label, false);
      conditions[i++] = result == node;  // Expected to find node without verbose output

      return Mosaic_Util.all(conditions);
    }
  }

  public static void main(String[] args) {
    TestSuite suite = new Test_Graph_0().new TestSuite();
    int result = Mosaic_Testbench.run(suite); 
    System.exit(result);
  }
}
