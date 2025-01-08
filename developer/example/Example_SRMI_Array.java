import com.ReasoningTechnology.Ariadne.Ariadne_SRM;
import com.ReasoningTechnology.Ariadne.Ariadne_SRMI_Array;

import java.util.Arrays;
import java.util.List;

public class Example_SRMI_Array {
  public static void main( String[] args ){
    // Create an Array
    List<String> label_array = Arrays.asList( "A", "B", "C", "D" );

    // Attach SRMI to the array
    Ariadne_SRMI_Array<String> srm = Ariadne_SRMI_Array.make( label_array );
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
