import com.ReasoningTechnology.Ariadne.Ariadne_SRMT;
import com.ReasoningTechnology.Ariadne.Ariadne_SRMT_List;
import com.ReasoningTechnology.Ariadne.IndexTree_SRMT_Child;
import com.ReasoningTechnology.Ariadne.IndexTree_Graph;
import com.ReasoningTechnology.Ariadne.IndexTree_Node;

public class Example_IndexTree_4x4{

  public static void main(String[] args){

    System.out.println("Example_IndexTree_4x4");

    // Initialize graph and start at root
    IndexTree_Graph graph = IndexTree_Graph.make();
    IndexTree_SRMT_Child root = graph.start();

    System.out.println("root: " + root.read().toString());

    // Variables for traversal
    IndexTree_Label label = root.read();
    IndexTree_Node node;
    Ariadne_SRMT<IndexTree_Label> child_srm;

    // Descend 3 more levels
    int i = 1;
    do{
      node = graph.lookup(label);
      child_srm = node.neighbor();
      label = child_srm.read();
      System.out.println("Descend: " + label.toString());
      if(i == 3) break;
      i++;
    }while(true);

    // Move across three more nodes
    i = 1;
    do{
      child_srm.step();
      label = child_srm.read();
      System.out.println("Across: " + label.toString());
      if(i == 3) break;
      i++;
    }while(true);

  }
}
