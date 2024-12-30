
import com.ReasoningTechnology.Mosaic.Mosaic_IO;
import com.ReasoningTechnology.Mosaic.Mosaic_Quantifier;
import com.ReasoningTechnology.Mosaic.Mosaic_Testbench;

import com.ReasoningTechnology.Ariadne.Ariadne_Label;


public class Label_0 {

  public class TestSuite {

    public Boolean label_creation_0(Mosaic_IO io) {
      Boolean[] conditions = new Boolean[4];
      int i = 0;

      // Test input
      Ariadne_Label label1 = new Ariadne_Label("test");
      Ariadne_Label label2 = new Ariadne_Label("");
      Ariadne_Label label3 = new Ariadne_Label("test");

      // Check that the value is correctly set
      conditions[i++] = label1.get().equals("test"); // Expect true
      conditions[i++] = label2.isEmpty(); // Expect true
      conditions[i++] = label1.equals(label3); // Expect true, as contents are identical
      conditions[i++] = label1.hashCode() == label3.hashCode(); // Expect true, as hash should match for equal labels

      // Return true if all conditions are met
      return Mosaic_Quantifier.all(conditions);
    }

  }

  public static void main(String[] args) {
    TestSuite suite = new Label_0().new TestSuite();
    int result = Mosaic_Testbench.run(suite); 
    System.exit(result);
  }

}
