import java.util.Arrays;
import java.util.List;

import com.ReasoningTechnology.Mosaic.Mosaic_IO;
import com.ReasoningTechnology.Mosaic.Mosaic_Util;
import com.ReasoningTechnology.Mosaic.Mosaic_Testbench;

import com.ReasoningTechnology.Ariadne.Ariadne_Label;
import com.ReasoningTechnology.Ariadne.Ariadne_LabelList;

public class LabelList_0 {

  public class TestSuite {

    public Boolean labelList_creation_0(Mosaic_IO io) {
      Boolean[] conditions = new Boolean[2];
      int i = 0;

      // Test the default constructor
      Ariadne_LabelList emptyList = new Ariadne_LabelList();
      conditions[i++] = emptyList.isEmpty(); // Expect true for an empty list

      // Test the constructor with a list of labels
      List<Ariadne_Label> labels = Arrays.asList(new Ariadne_Label("label1"), new Ariadne_Label("label2"));
      Ariadne_LabelList labelList = new Ariadne_LabelList(labels);
      conditions[i++] = labelList.size() == 2 && labelList.get(0).get().equals("label1") && labelList.get(1).get().equals("label2"); // Expect true for correct size and contents

      // Return true if all conditions are met
      return Mosaic_Util.all(conditions);
    }

  }

  public static void main(String[] args) {
    TestSuite suite = new LabelList_0().new TestSuite();
    int result = Mosaic_Testbench.run(suite); 
    System.exit(result);
  }
}
