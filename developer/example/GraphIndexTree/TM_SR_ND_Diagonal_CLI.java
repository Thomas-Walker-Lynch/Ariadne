import java.util.List;

public class TM_SR_ND_Diagonal_CLI{

  public static void main(String[] args){
    System.out.println("Starting IndexTree TM_SR_ND Example");

    // Instantiate the IndexTree Diagonal TM_SR_ND
    Graph g = Graph.make();
    TM_SR_ND_Child start_srtm = g.start();
    if( !start_srtm.can_read() ){
      System.out.println("Graph provides no start nodes. Thought you might want to know.");
      return;
    }

    do{
      Label start_label = start_srtm.read();
      System.out.println("Graph diagonalization starting from: " + start_label);
      TM_SR_ND_Diagonal srtm = TM_SR_ND_Diagonal.make(start_label);
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

