import java.util.Arrays;
import com.ReasoningTechnology.Ariadne.Ariadne_Node;

public class Node extends Ariadne_Node{

  public static Node make(Label label){
    return new Node(label);
  }

  private final Label first_child_label;
 
  public Node(Label label){
    super(label);
    first_child_label = label.copy();
    first_child_label.inc_down();
  }

  @Override public ND_SR_TM_Child neighbor(){
    return ND_SR_TM_Child.make(first_child_label);
  }

}
