package com.ReasoningTechnology.Ariadne;

import java.util.ArrayList;
import java.util.Set;

public class Ariadne_SRMT_Set extends Ariadne_SRMTI_Array{

  // Static factory method
  public static  Ariadne_SRMT_Set make(Set set){
    return new Ariadne_SRMT_Set( set );
  }

  protected Ariadne_SRMT_Set(Set set){
    super( new ArrayList(set) );
  }

}
