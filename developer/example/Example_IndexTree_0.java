import com.ReasoningTechnology.Ariadne.Ariadne_IndexTree_Child_SRM;
import com.ReasoningTechnology.Ariadne.Ariadne_IndexTree_Node;
import java.math.BigInteger;

public class Example_IndexTree_0{

  public static void traverse_index_tree(){
    System.out.println("Starting Index Tree Traversal:");

    Ariadne_IndexTree_Node root_node = Ariadne_IndexTree_Node.make(new BigInteger[0]);
    System.out.println("Root Node: " + format_label(root_node.label()));

    Ariadne_IndexTree_Node current_node = root_node;
    int depth_count = 0;
    do{
      Ariadne_IndexTree_Child_SRM depth_srm = current_node.neighbor();
      BigInteger[] depth_label = depth_srm.read();
      System.out.println("Step " + (depth_count + 1) + " Down: " + format_label(depth_label));
      current_node = Ariadne_IndexTree_Node.make(depth_label);

      // precise loop termination
      if(depth_count == 3) break; 
      depth_count++;
    }while(true);

    Ariadne_IndexTree_Child_SRM child_srm = current_node.neighbor();
    int child_count = 0;
    do{
      BigInteger[] child_label = child_srm.read();
      System.out.println("Step " + (child_count + 1) + " Across: " + format_label(child_label));

      // precise loop termination
      if(child_count == 3) break; // Mid-loop test for inclusive bound
      child_count++;
      child_srm.step();
    }while(true);
  }

  private static String format_label(BigInteger[] label){
    if(label.length == 0) return "[]";
    StringBuilder formatted = new StringBuilder("[");
    for(int i = 0 ;i < label.length ;i++){
      formatted.append(label[i].toString());
      if(i < label.length - 1) formatted.append(" ,");
    }
    formatted.append("]");
    return formatted.toString();
  }

  public static void main(String[] args){
    traverse_index_tree();
  }
}
