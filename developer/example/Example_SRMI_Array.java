import com.ReasoningTechnology.Ariadne.Ariadne_SRM;
import com.ReasoningTechnology.Ariadne.Ariadne_SRMI_Array;

import java.util.Arrays;
import java.util.List;

public class Example_SRMI_Array {
  public static void main( String[] args ){
    // Create a list
    List<String> label_list = Arrays.asList( "A" ,"B" ,"C" ,"D" );

    // Attach SRMI to the list
    Ariadne_SRMI_Array<String> srm = Ariadne_SRMI_Array.make( label_list );

    // Use the SRMI
    System.out.println( "Topology: " + srm.topology() );
    System.out.println( "Location: " + srm.location() );

    // Traverse the list
    while( srm.location() != Ariadne_SRM.Location.RIGHTMOST ){
      System.out.println( "Reading: " + srm.access() );
      srm.step();
    }

    // Final item
    System.out.println( "Reading: " + srm.access() );
    System.out.println( "Location: " + srm.location() );
  }
}
