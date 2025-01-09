package com.ReasoningTechnology.Ariadne;

import java.util.ArrayList;
import java.util.Set;

public class Ariadne_SRM_Set extends Ariadne_SRMI_Array{

  // Static factory method
  public static  Ariadne_SRM_Set make(Set set){
    return new Ariadne_SRM_Set( set );
  }

  // Constructor
  protected Ariadne_SRM_Set(Set set){
    super( new ArrayList(set) );
  }

}
