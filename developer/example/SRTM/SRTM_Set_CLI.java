import com.ReasoningTechnology.Ariadne.Ariadne_ND_SR_TM_Set;

import java.util.HashSet;
import java.util.Set;

public class ND_SR_TM_Set_CLI {
  public static void main( String[] args ){
    // Create a Set
    Set<String> label_set = new HashSet<>();
    label_set.add("A");
    label_set.add("B");
    label_set.add("C");

    // Attach ND_SR_TM to the set and traverse
    Ariadne_ND_SR_TM_Set<String> srm = Ariadne_ND_SR_TM_Set.make(label_set);
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
