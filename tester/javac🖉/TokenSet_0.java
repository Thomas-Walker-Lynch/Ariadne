import com.ReasoningTechnology.Mosaic.Mosaic_IO;
import com.ReasoningTechnology.Mosaic.Mosaic_Util;
import com.ReasoningTechnology.Mosaic.Mosaic_Testbench;

import com.ReasoningTechnology.Ariadne.Ariadne_Token;
import com.ReasoningTechnology.Ariadne.Ariadne_TokenSet;

public class TokenSet_0 {

  public class TestSuite {

    public Boolean tokenSet_creation_0(Mosaic_IO io) {
      Boolean[] conditions = new Boolean[2];
      int i = 0;

      // Test default constructor (expecting an empty TokenSet)
      Ariadne_TokenSet tokenSet = new Ariadne_TokenSet();
      conditions[i++] = tokenSet.isEmpty(); // Expect true for empty set

      // Add a token and verify presence
      Ariadne_Token token = new Ariadne_Token("error");
      tokenSet.add(token);
      conditions[i++] = tokenSet.size() == 1 && tokenSet.contains(token); // Expect true for correct size and content

      // Return true if all conditions are met
      return Mosaic_Util.all(conditions);
    }

    public Boolean tokenSet_uniqueness_0(Mosaic_IO io) {
      Boolean[] conditions = new Boolean[1];
      int i = 0;

      Ariadne_TokenSet tokenSet = new Ariadne_TokenSet();
      Ariadne_Token token1 = new Ariadne_Token("error");
      Ariadne_Token token2 = new Ariadne_Token("error");

      // Add two tokens with identical values and verify only one is stored
      tokenSet.add(token1);
      tokenSet.add(token2);
      conditions[i++] = tokenSet.size() == 1 && tokenSet.contains(token1) && tokenSet.contains(token2); // Expect true for single entry despite duplicate addition

      // Return true if all conditions are met
      return Mosaic_Util.all(conditions);
    }
  }

  public static void main(String[] args) {
    TestSuite suite = new TokenSet_0().new TestSuite();
    int result = Mosaic_Testbench.run(suite); 
    System.exit(result);
  }
}
