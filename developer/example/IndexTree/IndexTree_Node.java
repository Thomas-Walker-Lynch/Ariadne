package com.ReasoningTechnology.Ariadne;
import java.util.Arrays;

public class IndexTree_Node extends Ariadne_Node{

  public static IndexTree_Node make(IndexTree_Label label){
    return new IndexTree_Node(label);
  }

  private final IndexTree_Label first_child_label;
 
  public IndexTree_Node(IndexTree_Label label){
    super(label);
    first_child_label = label.copy();
    first_child_label.inc_down();
  }

  //  public IndexTree_SRMT_Child neighbor(){
  public IndexTree_SRMT_Child neighbor(){
    return IndexTree_SRMT_Child.make(first_child_label);
  }

}
