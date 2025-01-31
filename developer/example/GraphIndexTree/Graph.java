import com.ReasoningTechnology.Ariadne.Ariadne_Label;
import com.ReasoningTechnology.Ariadne.Ariadne_Node;
import com.ReasoningTechnology.Ariadne.Ariadne_Graph;

public class Graph extends Ariadne_Graph<Label ,Node>{

  @SuppressWarnings("unchecked")
  public static Graph make(){
    return new Graph();
  }

  protected Graph(){
  }

  @Override public TM_SR_NX_Child start(){
    Label root_label = Label.root();
    return TM_SR_NX_Child.make(root_label);
  }

  // no override, this graph does not lookup Ariadne_Label, only Label
  @Override public Node node(Label label){
    return Node.make(label);
  }

}

