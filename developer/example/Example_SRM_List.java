import com.ReasoningTechnology.Ariadne.Ariadne_SRM;
import com.ReasoningTechnology.Ariadne.Ariadne_SRM_List;

import java.util.LinkedList;

public class Example_SRM_List {
  public static void main(String[] args) {
    // Create a linked list
    LinkedList<String> labels = new LinkedList<>();
    labels.add("A");
    labels.add("B");
    labels.add("C");

    // Attach SRM to the linked list
    Ariadne_SRM_List<String> srm = Ariadne_SRM_List.attach(labels);

    // Use the SRM
    System.out.println("Topology: " + srm.topology());
    System.out.println("Initial Status: " + srm.status());

    // Traverse the list
    while (srm.status() != Ariadne_SRM.Status.RIGHTMOST) {
      System.out.println("Reading: " + srm.read());
      srm.step();
    }

    // Final item
    System.out.println("Reading: " + srm.read());
    System.out.println("Final Status: " + srm.status());

    // Reset the SRM and traverse again
    srm.reset();
    System.out.println("After reset: " + srm.read());
  }
}
