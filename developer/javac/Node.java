package com.ReasoningTechnology.Ariadne;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Node extends HashMap<String, Object>{

  private static String mark_property_name = "mark";
  private static String neighbor_property_name = "neighbor";

  public Node(){
    super();
    // Initialize the neighbor list in the constructor
    this.put(neighbor_property_name, new LabelList());
  }

  public void mark(Token token){
    if( this.get(mark_property_name) == null ){
      this.put(mark_property_name, new TokenSet());
    }
    ((TokenSet)this.get(mark_property_name)).add(token);
  }

  public boolean has_mark(Token token){
    TokenSet mark = (TokenSet)this.get(mark_property_name);
    return mark != null && mark.contains(token);
  }

  // Return the neighbor list (always exists after initialization)
  public LabelList neighbor_LabelList(){
    return (LabelList)this.get(neighbor_property_name);
  }

}
