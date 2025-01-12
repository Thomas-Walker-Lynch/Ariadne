import java.util.List;
import com.ReasoningTechnology.Ariadne.Ariadne_SRTM_List;

public class Example_SRTM_Diagonal {

  public static void main(String[] args){
    System.out.println("Starting IndexTree SRTM Example");

    // Instantiate the IndexTree Diagonal SRTM
    SRTM_Diagonal srtm = SRTM_Diagonal.make();

    int step_count = 0;
    do{
      System.out.println("Diagonal " + step_count + ":");

      // Read and print the current diagonal
      List<Label> diagonal = srtm.read();
      Ariadne_SRTM_List<Label> diagonal_srtm = Ariadne_SRTM_List.make(diagonal);

      if( diagonal_srtm.can_read() ){
        do{
          Label label = diagonal_srtm.read();
          System.out.print(label);

          if( !diagonal_srtm.can_step() ) break;
          System.out.println(" ");
          diagonal_srtm.step();

        }while(true);
      }

      step_count++;
      if( step_count == 5 ) break; // Stop after 5 diagonals

      srtm.step();
    }while( true );
  }

}

