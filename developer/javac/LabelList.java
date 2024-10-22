// LabelList.java
package com.ReasoningTechnology.Ariadne;
import java.util.List; 
import java.util.ArrayList;

public class LabelList extends ArrayList<Label> {
  // Constructor
  public LabelList(){
    super();
  }
  public LabelList(List<Label> labels){
    super();  // Initialize the parent class
    if (labels != null) {
      this.addAll(labels);  // Copy all elements from the provided list
    }
  }

}
