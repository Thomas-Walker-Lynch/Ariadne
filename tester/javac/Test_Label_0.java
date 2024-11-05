import com.ReasoningTechnology.Ariadne.Label;
import com.ReasoningTechnology.Mosaic.*;

public class Test_Label_0 {

  public class TestSuite {

    public Boolean label_creation_0(IO io) {
      Boolean[] conditions = new Boolean[4];
      int i = 0;

      // Test input
      Label label1 = new Label("test");
      Label label2 = new Label("");
      Label label3 = new Label("test");

      // Check that the value is correctly set
      conditions[i++] = label1.get().equals("test"); // Expect true
      conditions[i++] = label2.isEmpty(); // Expect true
      conditions[i++] = label1.equals(label3); // Expect true, as contents are identical
      conditions[i++] = label1.hashCode() == label3.hashCode(); // Expect true, as hash should match for equal labels

      // Return true if all conditions are met
      return MU.all(conditions);
    }

  }

  public static void main(String[] args) {
    TestSuite suite = new Test_Label_0().new TestSuite();
    int result = TestBench.run(suite); 
    System.exit(result);
  }

}
