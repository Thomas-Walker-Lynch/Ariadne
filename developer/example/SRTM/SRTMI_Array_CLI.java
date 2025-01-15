import com.ReasoningTechnology.Ariadne.Ariadne_ND_SR_TM;
import com.ReasoningTechnology.Ariadne.Ariadne_ND_SR_TM_Array;

import java.util.Arrays;
import java.util.List;

public class ND_SR_TM_Array_CLI {
  public static void main( String[] args ){
    // Create an Array
    List<String> label_array = Arrays.asList( "A", "B", "C", "D" );

    // Attach ND_SR_TM to the array
    Ariadne_ND_SR_TM_Array<String> srm = Ariadne_ND_SR_TM_Array.make(label_array);
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
