import com.ReasoningTechnology.Ariadne.Ariadne_SRM;
import java.math.BigInteger;

public class Example_CountingNumber_0 {

  protected static void print_ten(CountingNumber n) {
    System.out.println("Iterating through Counting Numbers:");
    if (!n.can_read()) return;

    if (n.topology() == Ariadne_SRM.Topology.SEGMENT) {
      if (n.can_read()) {
        do {
          System.out.println("Current Number: " + n.read());
          if (!n.can_step()) break;
          n.step();
        } while (true);
      }
    } else if (n.topology() == Ariadne_SRM.Topology.INFINITE_RIGHT) {
      int i = 1;
      if (n.can_read()) {
        do {
          System.out.println("Current Number: " + n.read());
          if (i == 10) break;
          n.step();
          i++;
        } while (true);
      }
    } else {
      System.out.println("Unrecognized tape topology.");
    }
  }

  public static void main(String[] args) {
    print_ten(CountingNumber.make(BigInteger.TEN));
    print_ten(CountingNumber.make());
  }
}
