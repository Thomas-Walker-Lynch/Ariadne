/*
An index Tree is infinite.

A tree diagonal consists of
a) a node descending from each child discovered thus far
b> a node extending each child list discovered thus far.

Hence, each diagonal extends the tree down one, and over one.

*/

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import com.ReasoningTechnology.Ariadne.Ariadne_Test;
import com.ReasoningTechnology.Ariadne.Ariadne_SRMT;
import com.ReasoningTechnology.Ariadne.Ariadne_IndexTree_Node;

public class IndexTree_Diagonal_SRMT extends Ariadne_SRMT_Label>{

  // Static
  //

  public static IndexTree_Diagonal_SRMT make(){
    return new IndexTree_Diagonal_SRMT();
  }

  //Instance data
  //

  private final List<Ariadne_Label> list_of__unopened_node;
  // each node has a child list,  this is a list of child lists
  private final List<Ariadne_SRMT> list_of__opened_incomplete_child_list;
  // Each diagonal is a list of nodes, referenced by their label
  private final List<Ariadne_Label> read_list;

  // Constructor(s)
  //

  protected IndexTree_Diagonal_SRMT(){
    list_of__unopened_node = new ArrayList<>();
    list_of__opened_incomplete_child_list = new ArrayList<>();
    read_list = new ArrayList<>();
    enqueue_root();
  }

  // Implementation of instance interface.
  //

  private void enqueue_root(){
    Ariadne_IndexTree_Label root_label = Ariadne_IndexTree_Label.root();
    read_list.add(root_label);

    Ariadne_IndexTree_Node root_node = lookup(root_label);
    if( !fetch_child_labels(root_node).isEmpty() ){
      list_of__unopened_node.add(root_label);
    }
  }

  // lol! This can not be done on an infinite list!
  private List<BigInteger[]> fetch_child_labels(Ariadne_IndexTree_Node node){
    List<BigInteger[]> child_labels = new ArrayList<>();
    if(node != null){
      IndexTree_Diagonal_SRMT child_srm = node.neighbor();
      if( child_srm.can_read() ){
        do{
          child_labels.add(child_srm.read());
          if( !srm.can_step ) break;
          child_srm.step();
        }while(true);
      }
    }
    return child_labels;
  }

  @Override public List<BigInteger[]> read(){
    return read_list;
  }

  @Override public void step(){
    read_list.clear();

    // Process unopened nodes
    while( !list_of__unopened_node.isEmpty() ){
      BigInteger[] label = list_of__unopened_node.remove(0);

      // Retrieve the node using lookup
      Ariadne_IndexTree_Node node = lookup(label);

      // Descend by getting neighbors
      List<BigInteger[]> child_labels = fetch_child_labels(node);
      if( !child_labels.isEmpty() ){
        list_of__opened_incomplete_child_list.add(child_labels);
      }
    }

    // Process incomplete child lists
    while( !list_of__opened_incomplete_child_list.isEmpty() ){
      List<BigInteger[]> child_labels = list_of__opened_incomplete_child_list.remove(0);
      if( !child_labels.isEmpty() ){
        BigInteger[] label = child_labels.remove(0);
        read_list.add(label);

        // Retrieve node and check its neighbors
        Ariadne_IndexTree_Node node = lookup(label);
        if( !fetch_child_labels(node).isEmpty() ){
          list_of__unopened_node.add(label);
        }
      }
    }
  }

  private Ariadne_IndexTree_Node lookup(BigInteger[] label){
    // Perform a lookup to retrieve the node corresponding to the label
    return Ariadne_IndexTree_Node.make(label);
  }

}
