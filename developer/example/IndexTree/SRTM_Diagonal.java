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

How diagonalization works:

  The `diagonal` list is the read value. It is initialized to the
  label for the root node, '[]'.

  Each time SRTM_Diagonal is stepped

    1. For each given label on the diagonal, inc_down is called, thus
      turning the label into the label for the leftmost child of the
      given node's child list.

    2.Each given SRTM_Child kept on the incomplete_list is `inc_across`ed.
      This turns each label into the label for its right neighbor.

      The SRTM_Child.read() value is then appended to the `diagonal`.

*/

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.ReasoningTechnology.Ariadne.Ariadne_Test;
import com.ReasoningTechnology.Ariadne.Ariadne_SRTM;
import com.ReasoningTechnology.Ariadne.Ariadne_SRTM_Label;
import com.ReasoningTechnology.Ariadne.Ariadne_Node;
import com.ReasoningTechnology.Ariadne.Ariadne_Label;

public class SRTM_Diagonal extends Ariadne_SRTM_Label{

  // Static
  //

  public static SRTM_Diagonal make(){
    return new SRTM_Diagonal();
  }

  // Instance data
  //

  private final List<Label> unopened_list;
  private final List<SRTM_Child> incomplete_list;
  // returned by read()
  private List<Label> diagonal;

  private final TopoIface topo_infinite = new Topo_Infinite();

  // Constructor(s)
  //

  // the diagonal will never be null nor empty
  protected SRTM_Diagonal(){
    unopened_node_list = new ArrayList<>();
    incomplete_child_list_srm = 
    diagonal = new ArrayList<>();
    set_leftmost_diagonal();
    set_topology(state_infinite);
  }

  private void set_leftmost_diagonal(){
    Ariadne_Label root_label = Label.root();
    diagonal.add(root_label);
    unopened_node_list.add(root_label);
  }

  private class Topo_Infinite implements TopoIface{
    @Override public boolean can_read(){
      return true;
    }
    @Override public BigInteger read(){
      return diagonal;
    }
    @Override public boolean can_step(){
      return true;
    }
    @Override public void step(){
      diagonal = new ArrayList<>();
      Ariadne_SRTM_List unopened = Ariadne_SRTM_List.make(list_of


    }
    @Override public Topology topology(){
      return Topology.INFINITE;
    }
  }

}
