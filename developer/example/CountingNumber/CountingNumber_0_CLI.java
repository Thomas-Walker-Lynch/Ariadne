import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_NX;
import java.math.BigInteger;

public class CountingNumber_0_CLI{

  protected static void print_ten(CountingNumber n){
    System.out.println("Iterating through Counting Numbers:");

    if( !n.can_read() ) return;

    if( n.topology() == Ariadne_TM_SR_NX.Topology.SEGMENT ){
      do{
        System.out.println("Current Number: " + n.read());
        if( !n.can_step() ) break;
        n.step();
      }while( true );

    }else if( n.topology() == Ariadne_TM_SR_NX.Topology.INFINITE ){
      int count = 0;
      do{
        System.out.println("Current Number: " + n.read());
        if( count == 9 ) break;
        n.step();
        count++;
      }while( true );

    }else{
      System.out.println("Unrecognized or invalid tape state.");
    }
  }

  public static void main(String[] args){
    print_ten( CountingNumber.make(BigInteger.TEN) ); // Finite segment up to 10
    print_ten( CountingNumber.make() );              // Infinite tape, stopping after 10 steps
  }

}
