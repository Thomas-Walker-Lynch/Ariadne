import java.math.BigInteger;

public class Example_Ariadne_SRM {

  public static void main(String[] args) {
    CountingNumber counting_number = CountingNumber.make();

    System.out.println("Initial Status: " + counting_number.status());
    System.out.println("Initial Read Value: " + counting_number.read());

    for( int step_index = 0 ;step_index < 10 ;step_index++ ){
      counting_number.step();
      System.out.println(
        "Step: " + step_index 
       +", Status: " + counting_number.status()
       +", Read Value: " + counting_number.read()
      );
    }
  }
}
