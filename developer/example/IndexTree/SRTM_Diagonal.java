/*
An Index Tree is infinite both in depth and breadth. As a tree is a
graph, and Index Tree is an infinite graph. The label for each node in
an index tree is an array of integers that explain how to traverse the
tree to arrive at the node.  `[]` is the root node.  `[[0] ,[1] ,[2]
...] is a list of the children to the root node, etc.

  All parts of a node can be computed from its label. Hence
  the SRM_Diagonal only manipulates labels.

  Label::inc_down turns a given label into the leftmost
  child lavel.

  Label::inc_across turns a child label into its right neighbor
  sibling label.

  An infinite child list is represented by an SRM_Child.  The
  SRM_Child is made from the label for the leftmost child in the
  list. From each child label it is possible to generate the label for
  the right neighbor child. This is be done by calling `inc_across()`.

Diagonalization can be used to traverse the infinite Index Tree, while
always enumerating a next layer of nodes nearest the root.  This
differs from depth first, which would never return to visit the child
to the right of the leftmost child, and from breadth first, which
would never descend to the grandchildren level of the tree.

/*
How diagonalization works:

  This is an infinite object, with each node having an infinite number
  of children, and there being an infinite number of generations,
  i.e., levels in the tree.

  Given a label, `inc_across` will turn it into the label for the
  right neighbor sibling, while `inc_down` will turn it into the label
  for the leftmost child.

  We compute each next diagonal based on the prior diagonal and a list
  called 'child_srtm_list'. We do not modify labels in the diagonal
  directly because the calling program might still be using it. Instead,
  labels on the diagonal are copied before being modified.

  The diagonalization algorithm systematically alternates between
  expanding depth (via `inc_down`) and breadth (via `inc_across`),
  ensuring that every node in the infinite tree is eventually included
  in a diagonal.

  Initially:

    diagonal is the root node, '[]'.
    child_srtm_list is empty.

  Each time SRTM_Diagonal is stepped:

    1. Create a new diagonal_1 and expand the child_srtm_list:

       1.1 Bind diagonal (current diagonal) to an SRTM.
       1.2 For each label in diagonal:
           1.2.1 Copy the label.
           1.2.2 Apply `inc_down` to the copy to get the leftmost child.
           1.2.3 Append this child label to child_srtm_list as a new SRTM_Child.

    2. Populate diagonal_1 using child_srtm_list:

       2.1 Bind child_srtm_list to an SRTM.
       2.2 For each STRM in the list:
           2.2.1 Read the label from the SRTM.
           2.2.2 Append a copy of this label to diagonal_1.
           2.2.3 if can_step the STRM, the step the head to the next sibling.
                 else remove SRTM from the child_srtm_list.  (This removal
                 will never happen on an infinite topology.)

    3. Update the state for the next step:

       3.1 Replace diagonal with diagonal_1.

  This algorithm ensures that any given node in the infinite index
  tree is eventually reached in a finite number of step. It traverses
  the tree a diagonal manner that alternates between depth and breadth
  expansion.
*/

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.ReasoningTechnology.Ariadne.Ariadne_Test;
import com.ReasoningTechnology.Ariadne.Ariadne_SRTM;
import com.ReasoningTechnology.Ariadne.Ariadne_SRTM_Label;
import com.ReasoningTechnology.Ariadne.Ariadne_SRTM_List;
import com.ReasoningTechnology.Ariadne.Ariadne_Node;
import com.ReasoningTechnology.Ariadne.Ariadne_Label;

public class SRTM_Diagonal extends Ariadne_SRTM{

  // Static
  //

  public static SRTM_Diagonal make(){
    return new SRTM_Diagonal();
  }

  // Instance data
  //

  private List<Label> diagonal; // the read value
  private final List<SRTM_Child> child_srtm_list;

  private final TopoIface topo_infinite_right = new Topo_InfiniteRight();

  // Constructor(s)
  //

  // the diagonal will never be null nor empty
  protected SRTM_Diagonal(){
    diagonal = new ArrayList<>();
    diagonal.add(Label.root());
    child_srtm_list = new ArrayList<>();
    set_topology(topo_infinite_right);
  }

  // Implementation of instance interface
  //

  @Override
  public String toString(){
    StringBuilder formatted = new StringBuilder("SRTM_Diagonal(");
    Ariadne_SRTM_List<Label> diagonal_srtm = Ariadne_SRTM_List.make(diagonal);

    if( diagonal_srtm.can_read() ){
      do{
        formatted.append(diagonal_srtm.read().toString());
        if( !diagonal_srtm.can_step() ) break;
        diagonal_srtm.step();
        formatted.append(" ,");
      }while(true);
    }

    formatted.append(")");
    return formatted.toString();
  }


  @Override
  @SuppressWarnings("unchecked")
  public List<Label> read(){
    return (List<Label>)super.read(); // Cast to ensure type consistency
  }

  private class Topo_InfiniteRight implements TopoIface{
    @Override public boolean can_read(){
      return true;
    }
    @Override public List read(){
      return diagonal;
    }
    @Override public boolean can_step(){
      return true;
    }

    @Override public void step(){

      List<Label> diagonal_1 = new ArrayList<>();

      // inc_down from each node on diagonal_0 -> entry on child_strm list
      Ariadne_SRTM_List<Label> diagonal_srtm = Ariadne_SRTM_List.make(diagonal);
      if( diagonal_srtm.can_read() ){
        do{
          Node node = Node.make(diagonal_srtm.read());
          child_srtm_list.add(node.neighbor()); // graph node neighbor == tree node child
          if( !diagonal_srtm.can_step() ) break;
          diagonal_srtm.step();
        }while(true);
      }

      // add to diagonal_1 from each on entry on the child_strm list
      Ariadne_SRTM_List<SRTM_Child> child_srtm_srtm = Ariadne_SRTM_List.make(child_srtm_list);
      if( child_srtm_srtm.can_read() ){
        do{
          SRTM_Child child_srtm = child_srtm_srtm.read();
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

    @Override public Topology topology(){
      return Topology.INFINITE;
    }

  }
}

