import java.util.List;

public class ND_SR_TM_Diagonal_CLI{

  public static void main(String[] args){
    System.out.println("Starting IndexTree ND_SR_TM Example");

    // Instantiate the IndexTree Diagonal ND_SR_TM
    Graph g = Graph.make();
    ND_SR_TM_Child start_srtm = g.start();
    if( !start_srtm.can_read() ){
      System.out.println("Graph provides no start nodes. Thought you might want to know.");
      return;
    }

    do{
      Label start_label = start_srtm.read();
      System.out.println("Graph diagonalization starting from: " + start_label);
      ND_SR_TM_Diagonal srtm = ND_SR_TM_Diagonal.make(start_label);
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

