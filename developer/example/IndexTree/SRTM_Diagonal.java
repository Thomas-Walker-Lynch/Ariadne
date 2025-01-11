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

  This is an infinite object, with each node having an infinite number
  of children, and their being an infinite number of generations,
  i.e. levels in the tree.

  Given a label, `inc_across` will turn it into the label for the
  right neighbor sibling, while `inc_down` will turn it into the label
  for the leftmost child.

  We compute each next diagonal based on the prior diagonal and a list
  a 'child_srtm_list'.  We should not modify values in the diagonal,
  because the calling program might still be using it.  Hence,
  labels on the diagonal are copies before being modified.

  Because we want to do two independent modifications of the same base
  label, we will need to copy it again before at least one of the
  modifications. 

  Initially:

    diagonal_0 is the root node, '[]'.
    diagonal_1 is null.
    child_srtm_list_0 is empty.
    child_srtm_list_1 is null.

  Each time SRTM_Diagonal is stepped

    1. make an empty diagonal_1, and an empty child_srtm_list_1.

    2. bind diagonl_0 to an SRTM, then for each cell:
       2.1 read label_0.
       2.2 copy label_0 to label_2.
       2.3 inc_down label_1
       2.4 copy label_1 to label_2.
       2.5 append label_1 to diagonal_2.
       2.6 bind a Child_SRTM to label_2. 
       2.7 append the Child_SRTM to child_srtm_list_1

    3. Given child_srtm_list_0, for each child_srtm:

       3.1 read label_0
       3.3 inc_across label_0, which will modify it in place to become
           the right neighbor sibling label.
       3.3 append label_0 to diagonal_1

   4, Update state:
       3.1 Join child_srtm_list_1 to the end of child_srtm_list_0.
       3.2 Write over the diagonal_0 reference with the diagonal_1 reference.

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

  private final List<Label> diagonal; // the read value
  private final List<SRTM_Child> child_srtm_list;

  private final TopoIface topo_infinite = new Topo_Infinite();

  // Constructor(s)
  //

  // the diagonal will never be null nor empty
  protected SRTM_Diagonal(){

    diagonal = new ArrayList<>();
    child_srtm_list_0 = new ArrayList<>();
    child_srtm_list_1 = new ArrayList<>();
    
    diagonal.add(List.root());
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

      // Step 1: Create a new diagonal_1 and child_srtm_list_1
      List<Label> diagonal_1 = new ArrayList<>();
      List<SRTM_Child> child_srtm_list_1 = new ArrayList<>();

      // Step 2: Process diagonal_0 using an SRTM
      SRTM_Label diagonal_srtm = SRTM_Label.make(diagonal);
      if( diagonal_srtm.can_read() ){
        do{
          // 2.1 Read label_0 from diagonal_0
          Label label_0 = diagonal_srtm.read();

          // 2.2 Copy label_0 to label_1 and inc_down it
          Label label_1 = label_0.copy();
          label_1.inc_down();

          // 2.3 Append label_1 to diagonal_1
          diagonal_1.add(label_1);

          // 2.4 Copy label_1 to label_2 and bind it to a Child_SRTM
          Label label_2 = label_1.copy();
          SRTM_Child child_srtm = SRTM_Child.make(label_2);

          // 2.5 Append the Child_SRTM to child_srtm_list_1
          child_srtm_list_1.add(child_srtm);

          if( !diagonal_srtm.can_step() ) break;
          diagonal_srtm.step();
        }while( true );
      }

      // Step 3: Process child_srtm_list_0 using an SRTM
      SRTM_Child child_srtm = SRTM_Child.make(child_srtm_list);
      if( child_srtm.can_read() ){
        do{
          // 3.1 Read the label from the SRTM and inc_across it in place
          Label sibling_label = child_srtm.read().read();
          sibling_label.inc_across();

          // 3.2 Append sibling_label to diagonal_1
          diagonal_1.add(sibling_label);

          if( !child_srtm.can_step() ) break;
          child_srtm.step();
        }while( true );
      }

      // Step 4: Replace diagonal_0 with diagonal_1
      diagonal = diagonal_1;

      // Step 5: Update the state for the next step
      SRTM_Label child_srtm_1 = SRTM_Label.make(child_srtm_list_1);
      if( child_srtm_1.can_read() ){
        do{
          child_srtm_list_0.add(child_srtm_1.read());
          if( !child_srtm_1.can_step() ) break;
          child_srtm_1.step();
        }while( true );
      }

    }

}
