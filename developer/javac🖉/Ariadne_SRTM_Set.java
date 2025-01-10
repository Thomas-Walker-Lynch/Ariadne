package com.ReasoningTechnology.Ariadne;

import java.util.ArrayList;
import java.util.Set;

public class Ariadne_SRTM_Set extends Ariadne_SRTMI_Array{

  // Static factory method
  public static  Ariadne_SRTM_Set make(Set set){
    return new Ariadne_SRTM_Set( set );
  }

  protected Ariadne_SRTM_Set(Set set){
    super( new ArrayList(set) );
  }

}
