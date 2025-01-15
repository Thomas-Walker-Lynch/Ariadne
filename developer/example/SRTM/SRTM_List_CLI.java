import com.ReasoningTechnology.Ariadne.Ariadne_ND_SR_TM;
import com.ReasoningTechnology.Ariadne.Ariadne_ND_SR_TM_List;

import java.util.LinkedList;

public class ND_SR_TM_List_CLI {
  public static void main( String[] args ){
    // Create a linked list
    LinkedList<String> label_list = new LinkedList<>();
    label_list.add( "A" );
    label_list.add( "B" );
    label_list.add( "C" );

    // Attach ND_SR_TM to the linked list and traverse
    Ariadne_ND_SR_TM_List<String> srm = Ariadne_ND_SR_TM_List.make(label_list);
    if( srm.can_read() ){
      do{
        System.out.println( "Reading: " + srm.read() );  
        System.out.println( "Topology: " + srm.topology() );
        if( !srm.can_step() ) break;
        srm.step();
      }while(true);
    }
  }
}
