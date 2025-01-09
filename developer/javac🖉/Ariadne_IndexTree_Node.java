package com.ReasoningTechnology.Ariadne;
import java.util.Arrays;

public class Ariadne_IndexTree_Node extends Ariadne_Node{

  public static Ariadne_IndexTree_Node make(Ariadne_IndexTree_Label label){
    return new Ariadne_IndexTree_Node(label);
  }

  private final Ariadne_IndexTree_Label first_child_label;
 
  public Ariadne_IndexTree_Node(Ariadne_IndexTree_Label label){
    super(label);
    first_child_label = label.copy();
    first_child_label.inc_down();
  }

  //  public Ariadne_IndexTree_Child_SRM neighbor(){
  public Ariadne_IndexTree_Child_SRM neighbor(){
    return Ariadne_IndexTree_Child_SRM.make(first_child_label);
  }

}
