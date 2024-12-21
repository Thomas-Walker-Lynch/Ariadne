import com.ReasoningTechnology.Mosaic.Mosaic_IO;
import com.ReasoningTechnology.Mosaic.Mosaic_Util;
import com.ReasoningTechnology.Mosaic.Mosaic_Testbench;

import com.ReasoningTechnology.Ariadne.Ariadne_Node;
import com.ReasoningTechnology.Ariadne.Ariadne_NodeList;

public class NodeList_0 {

  public class TestSuite {

    public Boolean nodeList_creation_0(Mosaic_IO io) {
      Boolean[] conditions = new Boolean[2];
      int i = 0;

      // Test default constructor (expecting an empty NodeList)
      Ariadne_NodeList nodeList = new Ariadne_NodeList();
      conditions[i++] = nodeList.isEmpty(); // Expect true for empty list

      // Add a node and verify presence
      Ariadne_Node node = new Ariadne_Node();
      nodeList.add(node);
      conditions[i++] = nodeList.size() == 1 && nodeList.contains(node); // Expect true for correct size and content

      // Return true if all conditions are met
      return Mosaic_Util.all(conditions);
    }
  }

  public static void main(String[] args) {
    TestSuite suite = new NodeList_0().new TestSuite();
    int result = Mosaic_Testbench.run(suite); 
    System.exit(result);
  }
}
