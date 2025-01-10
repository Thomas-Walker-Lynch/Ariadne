package com.ReasoningTechnology.Ariadne;

public class IndexTree_Graph{

  public static IndexTree_Graph make(){
    return new IndexTree_Graph();
  }
  protected IndexTree_Graph(){
  }

  public IndexTree_SRTM_Child start(){
    IndexTree_Label root_label = IndexTree_Label.root();
    return IndexTree_SRTM_Child.make(root_label);
  }

  IndexTree_Node lookup(IndexTree_Label label){
    return IndexTree_Node.make(label);
  }

}

