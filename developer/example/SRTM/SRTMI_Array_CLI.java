import com.ReasoningTechnology.Ariadne.Ariadne_SRTM;
import com.ReasoningTechnology.Ariadne.Ariadne_SRTMI_Array;

import java.util.Arrays;
import java.util.List;

public class SRTMI_Array_CLI {
  public static void main( String[] args ){
    // Create an Array
    List<String> label_array = Arrays.asList( "A", "B", "C", "D" );

    // Attach SRTMI to the array
    Ariadne_SRTMI_Array<String> srm = Ariadne_SRTMI_Array.make(label_array);
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
