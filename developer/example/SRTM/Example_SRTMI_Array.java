import com.ReasoningTechnology.Ariadne.Ariadne_SRMT;
import com.ReasoningTechnology.Ariadne.Ariadne_SRMTI_Array;

import java.util.Arrays;
import java.util.List;

public class Example_SRMTI_Array {
  public static void main( String[] args ){
    // Create an Array
    List<String> label_array = Arrays.asList( "A", "B", "C", "D" );

    // Attach SRMTI to the array
    Ariadne_SRMTI_Array<String> srm = Ariadne_SRMTI_Array.make( label_array );
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
