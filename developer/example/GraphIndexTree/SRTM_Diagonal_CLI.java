import java.util.List;

public class SRTM_Diagonal_CLI{

  public static void main(String[] args){
    System.out.println("Starting IndexTree SRTM Example");

    // Instantiate the IndexTree Diagonal SRTM
    Graph g = Graph.make();
    SRTM_Child start_srtm = g.start();
    if( !start_srtm.can_read() ){
      System.out.println("Graph provides no start nodes. Thought you might want to know.");
      return;
    }

    do{
      Label start_label = start_srtm.read();
      System.out.println("Graph diagonalization starting from: " + start_label);
      SRTM_Diagonal srtm = SRTM_Diagonal.make(start_label);
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
      if( !start_srtm.can_step() ) break;
      System.out.println();
      start_srtm.step();
    }while(true);
      
  }

}

