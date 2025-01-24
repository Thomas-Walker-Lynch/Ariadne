import java.util.LinkedList;

import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_NX;
import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_NX_List;

public class TM_SR_NX_List_CLI {
  public static void main( String[] args ){
    // Create a linked list
    LinkedList<String> label_list = new LinkedList<>();
    label_list.add( "A" );
    label_list.add( "B" );
    label_list.add( "C" );

    // Attach TM_SR_NX to the linked list and traverse
    Ariadne_TM_SR_NX_List<String> srm = Ariadne_TM_SR_NX_List.make(label_list);
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
