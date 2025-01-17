import com.ReasoningTechnology.Ariadne.Ariadne_Graph;

public class Graph extends Ariadne_Graph<Label>{

  public static Graph make(){
    return new Graph();
  }
  protected Graph(){
  }

  @Override public TM_SR_ND_Child start(){
    Label root_label = Label.root();
    return TM_SR_ND_Child.make(root_label);
  }

  // no override, this graph does not lookup Ariadne_Label, only Label
  @Override public Node lookup_node(Label label){
    return Node.make(label);
  }

}

