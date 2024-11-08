
public class Test_Token_0 {

  public class TestSuite {

    public Boolean token_creation_0(IO io) {
      Boolean[] conditions = new Boolean[4];
      int i = 0;

      // Test input
      Token token1 = new Token("error");
      Token token2 = new Token("warning");
      Token token3 = new Token("error");

      // Check that the value is correctly set
      conditions[i++] = token1.get().equals("error"); // Expect true
      conditions[i++] = token1.toString().equals("error"); // Expect true (toString should match value)
      conditions[i++] = token1.equals(token3); // Expect true, as contents are identical
      conditions[i++] = !token1.equals(token2); // Expect false, as values differ

      // Return true if all conditions are met
      return MU.all(conditions);
    }

    public Boolean token_hashCode_0(IO io) {
      Boolean[] conditions = new Boolean[1];
      int i = 0;

      Token token1 = new Token("error");
      Token token2 = new Token("error");

      // Check that two identical tokens have the same hashCode
      conditions[i++] = token1.hashCode() == token2.hashCode(); // Expect true

      // Return true if all conditions are met
      return MU.all(conditions);
    }
  }

  public static void main(String[] args) {
    TestSuite suite = new Test_Token_0().new TestSuite();
    int result = TestBench.run(suite); 
    System.exit(result);
  }

}
