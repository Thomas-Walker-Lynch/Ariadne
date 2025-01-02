import com.ReasoningTechnology.Ariadne.Ariadne_SRM;

import java.util.Arrays;
import java.util.List;

public class Example_Ariadne_SRM_1{

  public static class SimpleLockManagerDelegate implements Ariadne_LockManagerDelegate{

    @Override
    public void request(Object srm){
      System.out.println("LockManagerDelegate: Lock acquired for " + srm);
    }

    @Override
    public void relinquish(Object srm){
      System.out.println("LockManagerDelegate: Lock released for " + srm);
    }
  }

  public static void main(String[] args){
    // Create a LockManagerDelegate
    Ariadne_LockManagerDelegate delegate = new SimpleLockManagerDelegate();

    // Create an SRM instance with the delegate
    Ariadne_SRM<Integer> srm = Ariadne_SRM.make(delegate);

    // Define a list to iterate over
    List<Integer> list = Arrays.asList(10 ,20 ,30 ,40 ,50);

    // Mount the list on the SRM
    try{
      srm.mount();
      System.out.println("Mounted SRM. Status: " + srm.status());

      // Iterate through the list
      for(Integer value : list){
        System.out.println("Read value: " + value);
      }

      // Dismount the SRM
      srm.dismount();
      System.out.println("Dismounted SRM. Status: " + srm.status());
    }catch(IllegalStateException e){
      System.err.println("Error: " + e.getMessage());
    }

    // Demonstrate lenient methods
    System.out.println("\nUsing lenient methods:");
    srm.lenient_mount();
    System.out.println("Lenient mount complete. Status: " + srm.status());
    srm.lenient_dismount();
    System.out.println("Lenient dismount complete. Status: " + srm.status());
  }
}
