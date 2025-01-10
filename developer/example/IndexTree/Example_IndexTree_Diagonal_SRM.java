import java.math.BigInteger;
import java.util.Queue;

public class Example_IndexTree_Diagonal_SRTM {

  public static void main(String[] args){
    System.out.println("Starting IndexTree SRTM Example");

    // Instantiate the IndexTree Diagonal SRTM
    IndexTree_Diagonal_SRTM srm = IndexTree_Diagonal_SRTM.make();

    int step_count = 0;
    do{
      System.out.println("Step " + (step_count + 1) + ":");
      /*
      Queue<BigInteger[]> read_list = srm.read();
      if(!read_list.isEmpty()){
        for(BigInteger[] label : read_list){
          System.out.println("  Node Label: " + format_label(label));
        }
      }
      */
      if(step_count == 3) break; // Mid-loop test for inclusive bounds
      step_count++;
      srm.step();
    }while(true);
  }

  private static String format_label(BigInteger[] label){
    if(label.length == 0) return "[]";
    StringBuilder formatted = new StringBuilder("[");
    for(int i = 0; i < label.length; i++){
      formatted.append(label[i].toString());
      if(i < label.length - 1) formatted.append(" ,");
    }
    formatted.append("]");
    return formatted.toString();
  }
}
