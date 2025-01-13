import java.util.List;

public class SRTM_Diagonal_CLI{

  public static void main(String[] args){
    System.out.println("Starting IndexTree SRTM Example");

    // Instantiate the IndexTree Diagonal SRTM
    SRTM_Diagonal srtm = SRTM_Diagonal.make();
    int step_count = 0;
    if( srtm.can_read() ){
      do{
        System.out.println(step_count + ": " + srtm.read());
        if( !srtm.can_step() ) break;
        if( step_count == 4 ) break; // Stop after 5 diagonals
        step_count++;
        srtm.step();
      }while(true);
    }
  }

}

