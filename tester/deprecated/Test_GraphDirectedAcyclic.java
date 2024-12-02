import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ReasoningTechnology.Mosaic.Mosaic_IO;
import com.ReasoningTechnology.Mosaic.Mosaic_Testbench;
import com.ReasoningTechnology.Mosaic.Mosaic_Util;

import com.ReasoningTechnology.Ariadne.Ariadne_GraphDirectedAcyclic;
import com.ReasoningTechnology.Ariadne.Ariadne_Label;
import com.ReasoningTechnology.Ariadne.Ariadne_LabelList;
import com.ReasoningTechnology.Ariadne.Ariadne_Node;
import com.ReasoningTechnology.Ariadne.Ariadne_ProductionList;
import com.ReasoningTechnology.Ariadne.Ariadne_Token;
import com.ReasoningTechnology.Ariadne.Ariadne_TokenSet;

public class Test_GraphDirectedAcyclic {

  public class TestSuite {

    public Boolean path_find_cycle_0(Mosaic_IO io){
      Boolean[] conditions = new Boolean[2];
      int i = 0;

      Ariadne_GraphDirectedAcyclic graph = new Ariadne_GraphDirectedAcyclic(new HashMap<>(), new Ariadne_ProductionList(), new Ariadne_LabelList());
      Ariadne_LabelList path = new Ariadne_LabelList();
      path.add(new Ariadne_Label("A"));
      path.add(new Ariadne_Label("B"));
      path.add(new Ariadne_Label("A")); // Introduces a cycle

      List<Integer> cycleIndices = graph.path_find_cycle(path);
      conditions[i++] = (cycleIndices != null); // Expect cycle found
      conditions[i++] = (cycleIndices.size() == 2); // Expect two indices indicating cycle

      return Mosaic_Util.all(conditions);
    }

    public Boolean graph_descend_cycle_case_0(Mosaic_IO io){
      Boolean[] conditions = new Boolean[1];
      int i = 0;

      // Setup for testing
      Ariadne_GraphDirectedAcyclic graph = new Ariadne_GraphDirectedAcyclic(new HashMap<>(), new Ariadne_ProductionList(), new Ariadne_LabelList());
      Ariadne_LabelList path = new Ariadne_LabelList();
      path.add(new Ariadne_Label("A"));
      List<Ariadne_LabelList> pathStack = new ArrayList<>();
      pathStack.add(path);

      boolean cycleFound = graph.graph_descend_cycle_case(path, pathStack, true);
      conditions[i++] = !cycleFound; // Expect no cycle with a single-node path

      return Mosaic_Util.all(conditions);
    }

    public Boolean graph_descend_set_0(Mosaic_IO io){
      Boolean[] conditions = new Boolean[1];
      int i = 0;

      Ariadne_TokenSet expectedSet = Ariadne_GraphDirectedAcyclic.graph_descend_set;
      conditions[i++] = expectedSet != null && expectedSet.size() == 5; // Expect set of 5 termination tokens

      return Mosaic_Util.all(conditions);
    }

    public Boolean graph_descend_0(Mosaic_IO io){
      Boolean[] conditions = new Boolean[1];
      int i = 0;

      // Setup a graph with a simple path to test descent
      Map<Ariadne_Label, Ariadne_Node> nodeMap = new HashMap<>();
      Ariadne_Label rootLabel = new Ariadne_Label("root");
      Ariadne_Node rootNode = new Ariadne_Node();
      nodeMap.put(rootLabel, rootNode);

      Ariadne_GraphDirectedAcyclic graph = new Ariadne_GraphDirectedAcyclic(nodeMap, new Ariadne_ProductionList(), new Ariadne_LabelList());
      List<Ariadne_LabelList> pathStack = new ArrayList<>();
      pathStack.add(new Ariadne_LabelList(List.of(rootLabel)));

      Ariadne_TokenSet result = graph.graph_descend(pathStack, 10, true);
      conditions[i++] = result != null; // Expect valid TokenSet result

      return Mosaic_Util.all(conditions);
    }

    public Boolean graph_mark_cycles_0(Mosaic_IO io){
      Boolean[] conditions = new Boolean[1];
      int i = 0;

      Map<Ariadne_Label, Ariadne_Node> nodeMap = new HashMap<>();
      Ariadne_Label labelA = new Ariadne_Label("A");
      Ariadne_Node nodeA = new Ariadne_Node();
      nodeMap.put(labelA, nodeA);

      Ariadne_GraphDirectedAcyclic graph = new Ariadne_GraphDirectedAcyclic(nodeMap, new Ariadne_ProductionList(), new Ariadne_LabelList());
      Ariadne_LabelList rootNodes = new Ariadne_LabelList(List.of(labelA));
      Ariadne_TokenSet result = graph.graph_mark_cycles(rootNodes, 10, true);

      conditions[i++] = result != null; // Expect valid TokenSet result

      return Mosaic_Util.all(conditions);
    }

    public Boolean lookup_0(Mosaic_IO io){
      Boolean[] conditions = new Boolean[1];
      int i = 0;

      // Setup node map with a single node
      Map<Ariadne_Label, Ariadne_Node> nodeMap = new HashMap<>();
      Ariadne_Label label = new Ariadne_Label("node");
      Ariadne_Node node = new Ariadne_Node();
      nodeMap.put(label, node);

      Ariadne_GraphDirectedAcyclic graph = new Ariadne_GraphDirectedAcyclic(nodeMap, new Ariadne_ProductionList(), new Ariadne_LabelList());

      Ariadne_Node foundNode = graph.lookup(label, true);
      conditions[i++] = foundNode == node; // Expect to find the node

      return Mosaic_Util.all(conditions);
    }
  }

  public static void main(String[] args){
    TestSuite suite = new Test_GraphDirectedAcyclic().new TestSuite();
    int result = Mosaic_Testbench.run(suite);
    System.exit(result);
  }
}
