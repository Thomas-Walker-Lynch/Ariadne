/*
Donald Knuth pointed out in the Art of Computer Programming, that there
is a mid-test loop missing from most languages. The mid-test loop works
well with inclusive bound loops, as is needed with the SRM.

*/
import com.ReasoningTechnology.Ariadne.Ariadne_SRM;
import java.math.BigInteger;

public class Example_CountingNumber{

  protected static void print_ten(CountingNumber n){
    System.out.println("Iterating through Counting Numbers:");
    if( !n.mounted() ) return;
    if(n.topology() == Ariadne_SRM.Topology.SEGMENT){

      do{
        System.out.println("Current Number: " + n.read());
        if( n.status() ==  Ariadne_SRM.Status.RIGHTMOST ) break;
        n.step();
      }while(true);

    }else if(n.topology() == Ariadne_SRM.Topology.INFINITE_RIGHT){

      int i = 1;
      do{
        System.out.println("Current Number: " + n.read());
        if( i == 10 ) break;
        n.step();
        i++;
      }while(true);

    } else {
      System.out.println("Unrecognized tape topology.");
    }
  }

  public static void main(String[] args){
    print_ten(CountingNumber.make(BigInteger.TEN));
    print_ten(CountingNumber.make());

  }
}
