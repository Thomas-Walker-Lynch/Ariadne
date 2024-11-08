
public class Test_LabelList_0 {

  public class TestSuite {

    public Boolean labelList_creation_0(IO io) {
      Boolean[] conditions = new Boolean[2];
      int i = 0;

      // Test the default constructor
      LabelList emptyList = new LabelList();
      conditions[i++] = emptyList.isEmpty(); // Expect true for an empty list

      // Test the constructor with a list of labels
      List<Label> labels = Arrays.asList(new Label("label1"), new Label("label2"));
      LabelList labelList = new LabelList(labels);
      conditions[i++] = labelList.size() == 2 && labelList.get(0).get().equals("label1") && labelList.get(1).get().equals("label2"); // Expect true for correct size and contents

      // Return true if all conditions are met
      return MU.all(conditions);
    }

  }

  public static void main(String[] args) {
    TestSuite suite = new Test_LabelList_0().new TestSuite();
    int result = TestBench.run(suite); 
    System.exit(result);
  }

}
