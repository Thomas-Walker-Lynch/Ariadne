/*
Diagonal traversal is guaranteed to reach any given node in the IndexTree
in a finite number of steps. Neither depth first, nor breadth first
can do this. This guarantee also applies to a pruned IndexTree.

This implementation is nearly ready, to be included in the Ariadne
library for generalized diagonal traversal. I have abstracted out all
references to the IndexTree. It still needs to remove child lists that
have been completely reversed from the child_tm_list.  This was not
needed for the IndexTree because it is infinite, so they will never be
completely traversed. Also needed, is to stop when a node does not
have a child list. Again, this is not needed here because the
IndexTree has infinite depth.  When the generalized diagonal iterator
is ready, it could be used in place of this one.

*/

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.ReasoningTechnology.Ariadne.Ariadne_Test;
import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_NX;
import com.ReasoningTechnology.Ariadne.Ariadne_TM_SR_NX_List;

public class TM_SR_NX_Diagonal extends Ariadne_TM_SR_NX<List<Label>> {

  // Static
  //

  public static TM_SR_NX_Diagonal make(Label start_node) {
    return new TM_SR_NX_Diagonal(start_node);
  }

  // Instance data
  //

  private List<Label> diagonal = new ArrayList<>();
  private final List<TM_SR_NX_Child> child_srtm_list = new ArrayList<>();

  // Constructor(s)
  //

  protected TM_SR_NX_Diagonal(Label start_node) {
    if (start_node == null) {
      set_topology(topo_null);
      return;
    }
    set_topology(topo_infinite_right);
    diagonal.add(start_node);
  }

  // Instance interface implementation
  //

  @Override
  public List<Label> read() {
    return diagonal;
  }

  private class TopoInfiniteRight implements TopoIface<List<Label>> {
      @Override public boolean can_read(){
        return true;
      }
      @Override public List<Label> read(){
        return diagonal;
      }
      @Override public boolean can_step(){
        return true;
      }

    @Override public void step(){

      List<Label> diagonal_1 = new ArrayList<>();

      // inc_down from each node on diagonal_0 -> entry on child_srtm list
      Ariadne_TM_SR_NX_List<Label> diagonal_srtm = Ariadne_TM_SR_NX_List.make(diagonal);
      if( diagonal_srtm.can_read() ){
        do{
          Node node = Node.make(diagonal_srtm.read());
          child_srtm_list.add(node.neighbor()); // graph node neighbor == tree node child
          if( !diagonal_srtm.can_step() ) break;
          diagonal_srtm.step();
        }while(true);
      }

      // add to diagonal_1 from each on entry on the child_srtm list
      Ariadne_TM_SR_NX_List<TM_SR_NX_Child> child_srtm_srtm = Ariadne_TM_SR_NX_List.make(child_srtm_list);
      if( child_srtm_srtm.can_read() ){
        do{
          TM_SR_NX_Child child_srtm = child_srtm_srtm.read();
          Label label = child_srtm.read();
          diagonal_1.add(label.copy());
          child_srtm.step();
          if( !child_srtm_srtm.can_step() ) break;
          child_srtm_srtm.step();
        }while(true);
      }

      // Update the state for the next step
      diagonal = diagonal_1;
    }

    @Override public Topology topology() {
      return Topology.INFINITE;
    }
  }
  private final TopoIface<List<Label>> topo_infinite_right = new TopoInfiniteRight();

  // good citizen
  //
  
  @Override public String toString() {
    StringBuilder formatted = new StringBuilder("TM_SR_NX_Diagonal(");
    Ariadne_TM_SR_NX_List<Label> diagonal_srtm = Ariadne_TM_SR_NX_List.make(diagonal);

    if (diagonal_srtm.can_read()) {
      do {
        formatted.append(diagonal_srtm.read());
        if (!diagonal_srtm.can_step()) break;
        diagonal_srtm.step();
        formatted.append(" ,");
      } while (true);
    }

    formatted.append(")");
    return formatted.toString();
  }

}
