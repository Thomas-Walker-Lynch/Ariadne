import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_NX_Set;

import java.util.HashSet;
import java.util.Set;

public class TM_SR_NX_Set_CLI {
  public static void main( String[] args ){
    // Create a Set
    Set<String> label_set = new HashSet<>();
    label_set.add("A");
    label_set.add("B");
    label_set.add("C");

    // Attach TM_SR_NX to the set and traverse
    Ariadne_TM_SR_NX_Set<String> srm = Ariadne_TM_SR_NX_Set.make(label_set);
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
