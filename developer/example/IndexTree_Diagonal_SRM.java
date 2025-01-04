import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import com.ReasoningTechnology.Ariadne.Ariadne_Test;
import com.ReasoningTechnology.Ariadne.Ariadne_SRM;
import com.ReasoningTechnology.Ariadne.Ariadne_IndexTree_Node;

public class IndexTree_Diagonal_SRM extends Ariadne_SRM<List<BigInteger[]>> {

  private final List<BigInteger[]> list_of__unopened_node;
  private final List<List<BigInteger[]>> list_of__opened_incomplete_child_list;
  private final List<BigInteger[]> read_list;
  private final Ariadne_Test tester;

  public static IndexTree_Diagonal_SRM make() {
    return new IndexTree_Diagonal_SRM();
  }

  protected IndexTree_Diagonal_SRM() {
    this.list_of__unopened_node = new ArrayList<>();
    this.list_of__opened_incomplete_child_list = new ArrayList<>();
    this.read_list = new ArrayList<>();
    this.tester = Ariadne_Test.make("IndexTree_Diagonal_SRM: ");
    enqueue_root();
  }

  @Override
  public List<BigInteger[]> access() {
    return read_list;
  }

  @Override
  public void step() {
    read_list.clear();

    while (!list_of__unopened_node.isEmpty()) {
      BigInteger[] label = list_of__unopened_node.remove(0);

      // Retrieve the node using lookup
      Ariadne_IndexTree_Node node = lookup(label);

      // Descend by getting neighbors
      List<BigInteger[]> child_labels = fetch_child_labels(node);
      if (!child_labels.isEmpty()) {
        list_of__opened_incomplete_child_list.add(child_labels);
      }
    }

    while (!list_of__opened_incomplete_child_list.isEmpty()) {
      List<BigInteger[]> child_labels = list_of__opened_incomplete_child_list.remove(0);
      if (!child_labels.isEmpty()) {
        BigInteger[] label = child_labels.remove(0);
        read_list.add(label);

        tester.print("Queued label: " + format_label(label));

        // Retrieve node and check its neighbors
        Ariadne_IndexTree_Node node = lookup(label);
        if (!fetch_child_labels(node).isEmpty()) {
          list_of__unopened_node.add(label);
        }
      }
    }
  }

  private void enqueue_root() {
    BigInteger[] root_label = new BigInteger[0];
    read_list.add(root_label);

    tester.print("Queued root label: " + format_label(root_label));

    Ariadne_IndexTree_Node root_node = lookup(root_label);
    if (!fetch_child_labels(root_node).isEmpty()) {
      list_of__unopened_node.add(root_label);
    }
  }

  private Ariadne_IndexTree_Node lookup(BigInteger[] label) {
    // Perform a lookup to retrieve the node corresponding to the label
    return Ariadne_IndexTree_Node.make(label);
  }

  private List<BigInteger[]> fetch_child_labels(Ariadne_IndexTree_Node node) {
    List<BigInteger[]> child_labels = new ArrayList<>();

    if (node != null) {
      Ariadne_SRM<BigInteger[]> neighbor_srm = node.neighbor();
      while (neighbor_srm.can_step()) {
        child_labels.add(neighbor_srm.read());
        neighbor_srm.step();
      }
    }

    return child_labels;
  }

  private String format_label(BigInteger[] label) {
    StringBuilder formatted = new StringBuilder("[");
    for (int i = 0; i < label.length; i++) {
      formatted.append(label[i].toString());
      if (i < label.length - 1) formatted.append(",");
    }
    formatted.append("]");
    return formatted.toString();
  }
}
