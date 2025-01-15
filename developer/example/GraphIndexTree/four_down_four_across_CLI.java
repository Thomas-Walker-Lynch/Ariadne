import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_ND;
import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_ND_List;

public class four_down_four_across_CLI{

  public static void main(String[] args){

    System.out.println("Example_4x4");

    // Initialize graph and start at root
    Graph graph = Graph.make();
    TM_SR_ND_Child start = graph.start();
    Label label = start.read();
    Node node;
    TM_SR_ND_Child child_srm;

    System.out.println("starting at: " + start.read());

    // Descend 3 more levels
    int i = 1;
    do{
      node = graph.lookup(label);
      child_srm = node.neighbor();
      label = child_srm.read();
      System.out.println("Descended to: " + label.toString());
      if(i == 3) break;
      i++;
    }while(true);

    // Move across three more nodes
    i = 1;
    do{
      child_srm.step();
      label = child_srm.read();
      System.out.println("Across to: " + label.toString());
      if(i == 3) break;
      i++;
    }while(true);

  }
}
