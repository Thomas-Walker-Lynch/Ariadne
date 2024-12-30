import com.ReasoningTechnology.Mosaic.Mosaic_IO;
import com.ReasoningTechnology.Mosaic.Mosaic_Quantifier;
import com.ReasoningTechnology.Mosaic.Mosaic_Testbench;

import com.ReasoningTechnology.Ariadne.Ariadne_Label;
import com.ReasoningTechnology.Ariadne.Ariadne_Util;

import java.util.List;
import java.util.Arrays;

public class Util_0 {

  public class TestSuite {

    public Boolean print_list_0(Mosaic_IO io) {
      Boolean[] conditions = new Boolean[3];
      int i = 0;

      // Test with a non-empty list and a prefix
      List<Ariadne_Label> itemList1 = Arrays.asList(new Ariadne_Label("first"), new Ariadne_Label("second"), new Ariadne_Label("third"));
      Ariadne_Util.print_list("Items:", itemList1);

      // Capture and check stdout content
      String stdoutContent1 = io.get_out_content();
      conditions[i++] = stdoutContent1.equals("Items: 'first', 'second', 'third'.\n"); // Expect correct format with prefix

      // Clear streams for the next test
      io.clear_buffers();

      // Test with an empty list (no output expected)
      List<Ariadne_Label> itemList2 = Arrays.asList();
      Ariadne_Util.print_list("Empty:", itemList2);
      String stdoutContent2 = io.get_out_content();
      conditions[i++] = stdoutContent2.isEmpty(); // Expect no output for empty list

      // Clear streams for the next test
      io.clear_buffers();

      // Test with a null list (no output expected)
      Ariadne_Util.print_list("Null:", null);
      String stdoutContent3 = io.get_out_content();
      conditions[i++] = stdoutContent3.isEmpty(); // Expect no output for null list

      // Return true if all conditions are met
      return Mosaic_Quantifier.all(conditions);
    }
  }

  public static void main(String[] args) {
    TestSuite suite = new Util_0().new TestSuite();
    int result = Mosaic_Testbench.run(suite); 
    System.exit(result);
  }

}
