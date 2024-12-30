import com.ReasoningTechnology.Mosaic.Mosaic_IO;
import com.ReasoningTechnology.Mosaic.Mosaic_Quantifier;
import com.ReasoningTechnology.Mosaic.Mosaic_Testbench;

import com.ReasoningTechnology.Ariadne.Ariadne_Token;

public class Token_0 {

  public class TestSuite {

    public Boolean token_creation_0(Mosaic_IO io) {
      Boolean[] conditions = new Boolean[4];
      int i = 0;

      // Test input
      Ariadne_Token token1 = new Ariadne_Token("error");
      Ariadne_Token token2 = new Ariadne_Token("warning");
      Ariadne_Token token3 = new Ariadne_Token("error");

      // Check that the value is correctly set
      conditions[i++] = token1.get().equals("error"); // Expect true
      conditions[i++] = token1.toString().equals("error"); // Expect true (toString should match value)
      conditions[i++] = token1.equals(token3); // Expect true, as contents are identical
      conditions[i++] = !token1.equals(token2); // Expect false, as values differ

      // Return true if all conditions are met
      return Mosaic_Quantifier.all(conditions);
    }

    public Boolean token_hashCode_0(Mosaic_IO io) {
      Boolean[] conditions = new Boolean[1];
      int i = 0;

      Ariadne_Token token1 = new Ariadne_Token("error");
      Ariadne_Token token2 = new Ariadne_Token("error");

      // Check that two identical tokens have the same hashCode
      conditions[i++] = token1.hashCode() == token2.hashCode(); // Expect true

      // Return true if all conditions are met
      return Mosaic_Quantifier.all(conditions);
    }
  }

  public static void main(String[] args) {
    TestSuite suite = new Token_0().new TestSuite();
    int result = Mosaic_Testbench.run(suite); 
    System.exit(result);
  }
}
