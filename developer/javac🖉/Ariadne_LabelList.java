// LabelList.java
package com.ReasoningTechnology.Ariadne;
import java.util.List; 
import java.util.ArrayList;

public class Ariadne_LabelList extends ArrayList<Ariadne_Label>{
  // Constructor
  public Ariadne_LabelList(){
    super();
  }
  public Ariadne_LabelList(List<Ariadne_Label> labels){
    super();  // Initialize the parent class
    if(labels != null){
      this.addAll(labels);  // Copy all elements from the provided list
    }
  }

}
