import com.ReasoningTechnology.Ariadne.Ariadne_SRM;
import com.ReasoningTechnology.Ariadne.Ariadne_SRM_List;

import java.util.LinkedList;

public class Example_SRM_List {
  public static void main(String[] args){
    // Create a linked list
    LinkedList<String> label_list = new LinkedList<>();
    label_list.add("A");
    label_list.add("B");
    label_list.add("C");

    // Attach SRM to the linked list
    Ariadne_SRM_List<String> srm = Ariadne_SRM_List.make(label_list);

    // Use the SRM
    System.out.println( "Topology: " + srm.topology() );
    System.out.println( "Initial Location: " + srm.location() );

    // Traverse the list
    while( srm.location() != Ariadne_SRM.Location.RIGHTMOST ){
      System.out.println("Reading: " + srm.access());
      srm.step();
    }

    // Final item
    System.out.println(" Reading: " + srm.access() );
    System.out.println(" Final Location: " + srm.location() );

    // Rewind the SRM and traverse again
    srm.rewind();
    System.out.println( "After rewind: " + srm.access() );
  }
}
