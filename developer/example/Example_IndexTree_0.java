import java.math.BigInteger;
import java.util.List;
import com.ReasoningTechnology.Ariadne.Ariadne_IndexTree_Graph;
import com.ReasoningTechnology.Ariadne.Ariadne_IndexTree_Node;
import com.ReasoningTechnology.Ariadne.Ariadne_SRM;

public class Example_IndexTree_0 {

  public static void main(String[] args){
    // Create the IndexTree graph
    Ariadne_IndexTree_Graph graph = new Ariadne_IndexTree_Graph();
    Ariadne_SRM<BigInteger[]> srm = graph.start();

    System.out.println("Starting Depth-First Traversal:");
    // Depth-First Traversal: Visit the leftmost child at each level
    for (int i = 0; i < 4; i++){
      System.out.println("Step " + i + ": " + srm.read());
      srm = srm.neighbor(); // Move to the leftmost child
    }

    System.out.println("\nStarting Breadth-First Traversal:");
    // Breadth-First Traversal: Visit all siblings before descending
    for (int i = 0; i < 4; i++){
      System.out.println("Step " + i + ": " + srm.read());
      srm.step_right(); // Move to the next sibling
    }

    System.out.println("\nStarting Diagonal Walk:");
    // Diagonal Walk: Alternate between descending and stepping right
    for (int i = 0; i < 4; i++){
      System.out.println("Step " + i + ": " + srm.read());
      srm = srm.neighbor(); // Move to the first child
      srm.step_right(); // Step to the sibling
    }
  }
}
