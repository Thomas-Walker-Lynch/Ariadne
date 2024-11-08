
public class Test_Util_0 {

  public class TestSuite {

    public Boolean print_list_0(IO io) {
      Boolean[] conditions = new Boolean[3];
      int i = 0;

      // Test with a non-empty list and a prefix
      List<Label> itemList1 = Arrays.asList(new Label("first"), new Label("second"), new Label("third"));
      Util.print_list("Items:", itemList1);

      // Capture and check stdout content
      String stdoutContent1 = io.get_out_content();
      conditions[i++] = stdoutContent1.equals("Items: 'first', 'second', 'third'.\n"); // Expect correct format with prefix

      // Clear streams for the next test
      io.clear_buffers();

      // Test with an empty list (no output expected)
      List<Label> itemList2 = Arrays.asList();
      Util.print_list("Empty:", itemList2);
      String stdoutContent2 = io.get_out_content();
      conditions[i++] = stdoutContent2.isEmpty(); // Expect no output for empty list

      // Clear streams for the next test
      io.clear_buffers();

      // Test with a null list (no output expected)
      Util.print_list("Null:", null);
      String stdoutContent3 = io.get_out_content();
      conditions[i++] = stdoutContent3.isEmpty(); // Expect no output for null list

      // Return true if all conditions are met
      return MU.all(conditions);
    }
  }

  public static void main(String[] args) {
    TestSuite suite = new Test_Util_0().new TestSuite();
    int result = TestBench.run(suite); 
    System.exit(result);
  }

}
