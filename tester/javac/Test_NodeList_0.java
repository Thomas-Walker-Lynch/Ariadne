import com.ReasoningTechnology.Ariadne.Node;
import com.ReasoningTechnology.Ariadne.NodeList;
import com.ReasoningTechnology.Mosaic.*;


public class Test_NodeList_0 {

  public class TestSuite {

    public Boolean nodeList_creation_0(IO io) {
      Boolean[] conditions = new Boolean[2];
      int i = 0;

      // Test default constructor (expecting an empty NodeList)
      NodeList nodeList = new NodeList();
      conditions[i++] = nodeList.isEmpty(); // Expect true for empty list

      // Add a node and verify presence
      Node node = new Node();
      nodeList.add(node);
      conditions[i++] = nodeList.size() == 1 && nodeList.contains(node); // Expect true for correct size and content

      // Return true if all conditions are met
      return MU.all(conditions);
    }
  }

  public static void main(String[] args) {
    TestSuite suite = new Test_NodeList_0().new TestSuite();
    int result = TestBench.run(suite); 
    System.exit(result);
  }

}
