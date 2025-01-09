package com.ReasoningTechnology.Ariadne;

public class Ariadne_IndexTree_Graph{

  public static Ariadne_IndexTree_Graph make(){
    return new Ariadne_IndexTree_Graph();
  }
  protected Ariadne_IndexTree_Graph(){
  }

  public Ariadne_IndexTree_Child_SRM start(){
    Ariadne_IndexTree_Label root_label = Ariadne_IndexTree_Label.root();
    return Ariadne_IndexTree_Child_SRM.make(root_label);
  }

  Ariadne_IndexTree_Node lookup(Ariadne_IndexTree_Label label){
    return Ariadne_IndexTree_Node.make(label);
  }

}

