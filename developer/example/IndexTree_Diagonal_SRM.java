import java.math.BigInteger;
import com.ReasoningTechnology.Ariadne.Ariadne_Test;
import com.ReasoningTechnology.Ariadne.Ariadne_SRM;
import com.ReasoningTechnology.Ariadne.Ariadne_SRM_List;
import com.ReasoningTechnology.Ariadne.Ariadne_IndexTree_Node;

public class IndexTree_Diagonal_SRM extends Ariadne_SRM<BigInteger[]> {

  private final Ariadne_SRM_List<Ariadne_IndexTree_Node> list_of__unopened_node;
  private final Ariadne_SRM_List<Ariadne_SRM_List<Ariadne_IndexTree_Node>> list_of__opened_incomplete_child_list;
  private final Ariadne_SRM_List<BigInteger[]> read_list;
  private final Ariadne_Test tester;

  public static IndexTree_Diagonal_SRM make(){
    return new IndexTree_Diagonal_SRM();
  }

  protected IndexTree_Diagonal_SRM(){
    this.list_of__unopened_node = new Ariadne_SRM_List<>();
    this.list_of__opened_incomplete_child_list = new Ariadne_SRM_List<>();
    this.read_list = new Ariadne_SRM_List<>();
    this.tester = Ariadne_Test.make("IndexTree_Diagonal_SRM: ");
    enqueue_root();
  }

  @Override
  public void step(){
    super.step();
    
    read_list.clear(); // Clear the current read list for the new diagonal

    // Process unopened nodes
    while(!list_of__unopened_node.is_empty()){
      Ariadne_IndexTree_Node node = list_of__unopened_node.read();
      list_of__unopened_node.step(); // Remove node from unopened list
      Ariadne_SRM_List<Ariadne_IndexTree_Node> child_list = node.open();
      if(child_list != null){
        list_of__opened_incomplete_child_list.add(child_list);
      }
    }

    // Process incomplete child lists
    while(!list_of__opened_incomplete_child_list.is_empty()){
      Ariadne_SRM_List<Ariadne_IndexTree_Node> child_list = list_of__opened_incomplete_child_list.read();
      if(!child_list.is_empty()){
        Ariadne_IndexTree_Node node = child_list.read();
        child_list.step(); // Step to the next node in the child list
        BigInteger[] label = node.label();
        read_list.add(label); // Queue the label on the read list
        tester.print("Queued label: " + format_label(label));
        if(node.has_children()){
          list_of__unopened_node.add(node); // Add node to unopened list if it has children
        }
        if(child_list.is_empty()){
          list_of__opened_incomplete_child_list.step(); // Remove empty child lists
        }
      }
    }
  }

  private void enqueue_root(){
    Ariadne_IndexTree_Node root = Ariadne_IndexTree_Node.make(new BigInteger[0]);
    read_list.add(root.label());
    tester.print("Queued root label: " + format_label(root.label()));
    if(root.has_children()){
      list_of__unopened_node.add(root);
    }
  }

  private String format_label(BigInteger[] label){
    if(label.length == 0) return "[]";
    StringBuilder formatted = new StringBuilder("[");
    for(int i = 0; i < label.length; i++){
      formatted.append(label[i].toString());
      if(i < label.length - 1) formatted.append(" ,");
    }
    formatted.append("]");
    return formatted.toString();
  }
}
