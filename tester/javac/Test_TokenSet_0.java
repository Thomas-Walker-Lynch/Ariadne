import com.ReasoningTechnology.Ariadne.Token;
import com.ReasoningTechnology.Ariadne.TokenSet;
import com.ReasoningTechnology.Mosaic.*;

public class Test_TokenSet_0 {

  public class TestSuite {

    public Boolean tokenSet_creation_0(IO io) {
      Boolean[] conditions = new Boolean[2];
      int i = 0;

      // Test default constructor (expecting an empty TokenSet)
      TokenSet tokenSet = new TokenSet();
      conditions[i++] = tokenSet.isEmpty(); // Expect true for empty set

      // Add a token and verify presence
      Token token = new Token("error");
      tokenSet.add(token);
      conditions[i++] = tokenSet.size() == 1 && tokenSet.contains(token); // Expect true for correct size and content

      // Return true if all conditions are met
      return MU.all(conditions);
    }

    public Boolean tokenSet_uniqueness_0(IO io) {
      Boolean[] conditions = new Boolean[1];
      int i = 0;

      TokenSet tokenSet = new TokenSet();
      Token token1 = new Token("error");
      Token token2 = new Token("error");

      // Add two tokens with identical values and verify only one is stored
      tokenSet.add(token1);
      tokenSet.add(token2);
      conditions[i++] = tokenSet.size() == 1 && tokenSet.contains(token1) && tokenSet.contains(token2); // Expect true for single entry despite duplicate addition

      // Return true if all conditions are met
      return MU.all(conditions);
    }
  }

  public static void main(String[] args) {
    TestSuite suite = new Test_TokenSet_0().new TestSuite();
    int result = TestBench.run(suite); 
    System.exit(result);
  }

}
