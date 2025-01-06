
import com.ReasoningTechnology.Ariadne.Ariadne_SRM;
import com.ReasoningTechnology.Ariadne.Ariadne_SRM_List;

import java.math.BigInteger;

public class Example_IndexTree_4x4{

  public static void main(String[] args){

    Ariadne_IndexTree_Graph graph = new Ariadne_IndexTree_Graph();
    Ariadne_SRM<BigInteger[]> root = graph.start();

    Ariadne_IndexTree_Node label;
    Ariadne_IndexTree_Node node;
    Ariadne_SRM<BigInteger[]> child_srm;

    // descend 3 more levels
    label = root.read();
    System.out.println(label);
    int i = 1;
    do{
      node = graph.lookup(label);
      child_srm = node.neighbor();
      label = child_srm.read();
      System.out.println(label);
      if(i == 3) break;
      i++;
    }while(true);
       
    // move across three more nodes
    i = 1;
    do{
      child_srm.step();
      label = child_srm.read();
      System.out.println(label);
      if(i == 3) break;
      i++;
    }while(true);

  }
}

