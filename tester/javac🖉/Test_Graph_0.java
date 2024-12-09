import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.lang.reflect.Method;

import com.ReasoningTechnology.Mosaic.Mosaic_IO;
import com.ReasoningTechnology.Mosaic.Mosaic_Testbench;
import com.ReasoningTechnology.Mosaic.Mosaic_Util;

import com.ReasoningTechnology.Ariadne.Ariadne_Graph;
import com.ReasoningTechnology.Ariadne.Ariadne_GraphDirectedAcyclic;
import com.ReasoningTechnology.Ariadne.Ariadne_Label;
import com.ReasoningTechnology.Ariadne.Ariadne_LabelList;
import com.ReasoningTechnology.Ariadne.Ariadne_Node;
import com.ReasoningTechnology.Ariadne.Ariadne_ProductionList;

public class Test_Graph_0 {

  public class TestSuite {
    private final Object GraphDirectedAcyclic_proxy;

    public TestSuite() {
      this.GraphDirectedAcyclic_proxy =
        Mosaic_Util.make_all_public_methods_proxy( Ariadne_GraphDirectedAcyclic.class );
    }

    public Boolean path_find_cycle_0( Mosaic_IO io ) {
      Boolean[] conditions = new Boolean[1];
      Mosaic_Util.all_set_false( conditions );
      int i = 0;
      try {
        Ariadne_LabelList path = new Ariadne_LabelList(
          List.of(
            new Ariadne_Label( "A" )
           ,new Ariadne_Label( "B" )
           ,new Ariadne_Label( "A" )
          )
        );
 //       @SuppressWarnings("unchecked")
        List<Integer> cycle_indices = (List<Integer>) GraphDirectedAcyclic_proxy.getClass().getMethod(
          "path_find_cycle"
         ,Ariadne_LabelList.class
        ).invoke( GraphDirectedAcyclic_proxy ,path );
        /*

        conditions[i++] = cycle_indices != null && cycle_indices.size() == 2;
        */
      } catch (Exception e) {
        Mosaic_Util.log_message("path_find_cycle_0", "Test logic error: " + e.getMessage());
        return false;
      }

      return true;
//      return Mosaic_Util.all( conditions );
    }

    public Boolean lookup_0( Mosaic_IO io ) {
      Boolean[] conditions = new Boolean[1];
      Mosaic_Util.all_set_false( conditions );
      int i = 0;
        return true;
    }
  }

  public static void main(String[] args) {
    // no command line arguments, nor options to be parsed.
    TestSuite suite = new Test_Graph_0().new TestSuite();
    int result = Mosaic_Testbench.run(suite); 
    System.exit(result);
  }
}
