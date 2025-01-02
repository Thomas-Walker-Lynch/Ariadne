import com.ReasoningTechnology.Ariadne.Ariadne_SRM;
import com.ReasoningTechnology.Ariadne.Ariadne_SRMI_Array;

import java.util.Arrays;
import java.util.List;

public class Example_SRMI_Array {
  public static void main( String[] args ) {
    // Create a list
    List<String> labels = Arrays.asList( "A" ,"B" ,"C" ,"D" );

    // Attach SRMI to the list
    Ariadne_SRMI_Array<String> srm = Ariadne_SRMI_Array.attach( labels );

    // Use the SRMI
    System.out.println( "Topology: " + srm.topology() );
    System.out.println( "Status: " + srm.status() );

    // Traverse the list
    while( srm.status() != Ariadne_SRM.Status.RIGHTMOST ) {
      System.out.println( "Reading: " + srm.read() );
      srm.step();
    }

    // Final item
    System.out.println( "Reading: " + srm.read() );
    System.out.println( "Status: " + srm.status() );
  }
}
