
import com.ReasoningTechnology.Mosaic.Mosaic_IO;
import com.ReasoningTechnology.Mosaic.Mosaic_Util;
import com.ReasoningTechnology.Mosaic.Mosaic_Testbench;

import com.ReasoningTechnology.Ariadne.Ariadne_Node;
import com.ReasoningTechnology.Ariadne.Ariadne_Label;
import com.ReasoningTechnology.Ariadne.Ariadne_LabelList;
import com.ReasoningTechnology.Ariadne.Ariadne_Token;
import com.ReasoningTechnology.Ariadne.Ariadne_TokenSet;


public class Node_0 {

  public class TestSuite {

    public Boolean node_creation_0(Mosaic_IO io) {
      Boolean[] conditions = new Boolean[2];
      int i = 0;

      // Test that neighbor list is initialized
      Ariadne_Node node = new Ariadne_Node();
      conditions[i++] = node.neighbor_LabelList() != null && node.neighbor_LabelList().isEmpty(); // Expect true

      // Test that the mark property is not initialized until used
      conditions[i++] = node.get("mark") == null; // Expect true

      // Return true if all conditions are met
      return Mosaic_Util.all(conditions);
    }

    public Boolean node_marking_0(Mosaic_IO io) {
      Boolean[] conditions = new Boolean[3];
      int i = 0;

      Ariadne_Node node = new Ariadne_Node();
      Ariadne_Token token1 = new Ariadne_Token("token1");
      Ariadne_Token token2 = new Ariadne_Token("token2");

      // Test marking the node with token1
      node.mark(token1);
      conditions[i++] = node.has_mark(token1); // Expect true
      conditions[i++] = !node.has_mark(token2); // Expect false for unmarked token

      // Test that mark property is now initialized and contains token1
      Ariadne_TokenSet markSet = (Ariadne_TokenSet) node.get("mark");
      conditions[i++] = markSet != null && markSet.contains(token1); // Expect true

      // Return true if all conditions are met
      return Mosaic_Util.all(conditions);
    }

    public Boolean node_neighbor_0(Mosaic_IO io) {
      Boolean[] conditions = new Boolean[1];
      int i = 0;

      // Test adding and retrieving neighbors
      Ariadne_Node node = new Ariadne_Node();
      Ariadne_LabelList neighbors = node.neighbor_LabelList();
      neighbors.add(new Ariadne_Label("neighbor1"));
      neighbors.add(new Ariadne_Label("neighbor2"));
      conditions[i++] = neighbors.size() == 2 && neighbors.get(0).get().equals("neighbor1") && neighbors.get(1).get().equals("neighbor2"); // Expect true

      // Return true if all conditions are met
      return Mosaic_Util.all(conditions);
    }
  }

  public static void main(String[] args) {
    TestSuite suite = new Node_0().new TestSuite();
    int result = Mosaic_Testbench.run(suite); 
    System.exit(result);
  }
}
