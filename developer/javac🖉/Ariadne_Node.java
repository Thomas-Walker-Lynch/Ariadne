package com.ReasoningTechnology.Ariadne;
import java.util.HashMap;

public class Ariadne_Node extends HashMap<String, Object>{

  private static String mark_property_name = "mark";
  private static String neighbor_property_name = "neighbor";

  public Ariadne_Node(){
    super();
    this.put(neighbor_property_name, new Ariadne_LabelList());
  }

  public void mark(Ariadne_Token token){
    if(this.get(mark_property_name) == null){
      this.put(mark_property_name, new Ariadne_TokenSet());
    }
    ((Ariadne_TokenSet) this.get(mark_property_name)).add(token);
  }

  public boolean has_mark(Ariadne_Token token){
    Ariadne_TokenSet mark =(Ariadne_TokenSet) this.get(mark_property_name);
    return mark != null && mark.contains(token);
  }

  public Ariadne_LabelList neighbor_LabelList(){
    return(Ariadne_LabelList) this.get(neighbor_property_name);
  }

}
